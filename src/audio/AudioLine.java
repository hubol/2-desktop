package audio;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import main.Scene;
/**
 * This class wraps a single SourceDataLine and continously feeds it with sound data in the form of bytes.
 * The AudioLine is a pure java software sample mixer which gets playback audio data from StupidClips.
 * The clips should always be given a reference to the AudioLine instance upon creation, as the classes are tightly bound.
 * 
 * A clip cannot be played on multiple AudioLines due to the naïve linked list implementation. 
 * This should however not be a problem. 
 * 
 * The AudioLine runs in a separate thread which continously fills a small buffer with a mix of whichever clips are
 * playing at the time.
 * 
 * Sound data is expected to be signed, little endian. However conversion from unsigned 8 bit takes place on the fly.
 * */
public class AudioLine extends Thread
	{
	/**The audio output. Details of what goes on beyond this is left to JavaSound.*/
	SourceDataLine line;
	/**The format that the audio output uses. All clips are converted to this format in real time.*/
	AudioFormat format;
	/**The total number of frames that have been put in the buffer since the start of the program. 
	 * A frame contains 2 samples, since this is a stereo output.*/
	long totalBufferFrames;
	/**The number of bytes in the SourceDataLine's buffer.*/
	int bufByteSize;
	/**The number of frames that are put into the buffer at a time.
	 * As such, from 1 to 2 times this number of frames will pass 
	 * from a sound play is called until the data is sent to the output.*/
	int frameLag;
	/**The number of frames per second.*/
	int frameRate;
	/**The number of bits per sample. Will be 16.*/
	int bitsPerSample;
	/**Number of channels. Will be equal to 2.*/
	int channels;
	
	/**First element in list of clips being played back.*/
	JavaSoundClip listFirst;
	private LinkedList<JavaSoundClip> playQueue;
	
	/**Construct a new AudioLine with the given sample rate. 
	 * Strange sample rates may throw Exceptions. 
	 * Otherwise, this should be supported by any computer with sound.*/
	public AudioLine(int sampleRate, int bufferMultiplier) throws LineUnavailableException
		{
		this.frameRate = sampleRate;
		this.bitsPerSample = 16;
		this.channels=2;
		playQueue = new LinkedList<JavaSoundClip>();
		
		//Start with the empty list for playback
		listFirst=null;
		
		//Set up the output line
		format = new AudioFormat(sampleRate,bitsPerSample, 2, true, false);
		line = AudioSystem.getSourceDataLine(format);
		bufByteSize = 1024*bufferMultiplier*sampleRate/44100;
		frameLag = bufByteSize*8/bitsPerSample/channels;
		line.open(format,bufByteSize);
		line.start();
		totalBufferFrames=0;
		write(frameLag);
		
		//The program should not keep alive because of the AudioLine. 
		setDaemon(true);
		setPriority(MAX_PRIORITY);
		//Start processing.
		start();
		}

	@Override
	public void run()
		{
		//Write continously
		while(true)
			{
			//This works because write blocks until all the data has been written.
			//The data can only be written when there is enough free space in the buffer.
			write(frameLag);
			}
		}
	/**Return average millisecond delay from a sound play call until the sound actually starts.*/
	public int getAverageDelay()
		{
		return frameLag*1500/frameRate;
		}

	private void write(int frames)
		{
		//Poll playback from the queue
		JavaSoundClip toBePlayed;
		while ((toBePlayed=poll())!=null)
			{
			playNow(toBePlayed);
			}
		
		//Do the mixing in a 32 bit buffer.
		int[] buf = new int[frames*channels];
		
		//This simply refers to the data array for each individual sample we are processing below.
		byte[] add;
		//The playback position in the data array
		int pos;
		//The actual, processed, signed amplitude
		int value;
		
		//Do 32 bit mixing with 16 bit input, clamp to 16 bits
		for(int i=0; i<buf.length; i+=bitsPerSample*channels/16)
			{
			//Process each sample that is playing now
			JavaSoundClip c = listFirst;
			while (c!=null)
				{
				add = c.dataArray;
				pos = c.bitsPerSample/8*c.channels*(int)c.currentFramePos;
				
				try
					{
					//Get left amplitude
					if(c.bitsPerSample==16)
						value = joinShort(add[pos+1],add[pos]);
					else
						value = ((add[pos]&0xFF)<<8)+Short.MIN_VALUE;
					//Put left channel in the buffer
					buf[i]  += (int)(c.gain*value*(c.pan>0?1-c.pan:1));//add[pos];
					
					if(c.channels==2)
						{
						//Get right amplitude
						if(c.bitsPerSample==16)
							value = joinShort(add[pos+3],add[pos+2]);
						else
							value = ((add[pos+1]&0xFF)<<8)+Short.MIN_VALUE;
						}
					//Put right channel in the buffer
					buf[i+1]  += (int)(c.gain*value*(c.pan>0?1:1+c.pan));
					}
				catch (ArrayIndexOutOfBoundsException e)
					{
					if (Scene.console)
						System.out.println(pos+", "+c.currentFramePos+", "+c.playback+", "+
							c.stop+", "+c.numFrames+", "+c.dataArray.length);
					e.printStackTrace();//Should not happen
					}
				 
				//Increment
				c.currentFramePos+=c.frequency;
				
				if(c.positionListener!=null && c.currentFramePos>=c.actionPosition)
					{
					JavaSoundListener lis = c.positionListener;
					c.positionListener=null;
					lis.soundAction(c,(int) c.currentFramePos);
					}
				
				//This gets the next sample in any case:
				if(c.stop)//It was stopped by the program.
					{
					c = remove(c);
					}
				else if(c.currentFramePos>=c.numFrames)//Reached the end, not manually stopped
					{
					if(c.isLooping)//Loop around
						{
						c.currentFramePos=0;
						c = c.listNext;
						}
					else//Stop dead
						{
						//JavaSoundClip oldC = c;
						c = remove(c);
						}
					}
				else//Nothing in particular
					c = c.listNext;
				}
			//Clamp the values to signed 16 bit
			if (buf[i]>Short.MAX_VALUE) buf[i] = Short.MAX_VALUE;
			if (buf[i]<Short.MIN_VALUE) buf[i] = Short.MIN_VALUE;
			if (buf[i+1]>Short.MAX_VALUE) buf[i+1] = Short.MAX_VALUE;
			if (buf[i+1]<Short.MIN_VALUE) buf[i+1] = Short.MIN_VALUE;
			}
		//Take a note that we sent more frames to the output 
		totalBufferFrames+=frames;
		
		//Flatten to byte array
		byte[] bytes = new byte[buf.length*2];
		for(int i=0; i<buf.length; i++)
			{
			bytes[i*2]  = (byte) (buf[i]&0xFF);
			bytes[i*2+1]= (byte)((buf[i]&0xFF00)>>8);
			}
		//Actually write. This blocks until it's done and we're ready for the next batch.
		line.write(bytes, 0, bytes.length);
		}
	
	/**This method will reset the clip's position, and, if it is not already in the list, 
	 * add the clip to the list of sounds being played.
	 * The method should only be called from the AudioLine thread, to avoid concurrency issues.*/
	private void playNow(JavaSoundClip clip)
		{
		//Restart sound
		clip.currentFramePos = 0;
		if(!clip.playback)
			{
			clip.playback = true;
			//Insert at beginning of list.
			if(listFirst!=null)
				listFirst.listPrev = clip;
			clip.listNext = listFirst;
			listFirst=clip;
			}
		}
	
	/**Remove the clip from the playback list. 
	 * Must only be called from the AudioLine thread.
	 * @return The next clip in the list. May be null.*/
	private JavaSoundClip remove(JavaSoundClip clip)
		{
		clip.stop = false;//It's being stopped, no need for further stopping.
		//Remove from list
		if(clip==listFirst)
			{
			listFirst = clip.listNext;
			if(listFirst!=null)
				listFirst.listPrev = null;
			}
		else
			{
			if (clip.listPrev!=null)
				clip.listPrev.listNext = clip.listNext;
			if (clip.listNext!=null)
				clip.listNext.listPrev = clip.listPrev;
			}
		JavaSoundClip r = clip.listNext;
		clip.listNext=null;
		clip.listPrev=null;
		clip.playback=false;
		clip.playCalled=false;
		return r;
		}
	/**Get the next clip from the queue of clips waiting to be played.*/
	private synchronized JavaSoundClip poll()
		{
		return playQueue.poll();
		}
	/**Start playback of the clip from position 0 next time a buffer write operation happens.
	 * It adds the clip to a queue of sounds waiting to be played. 
	 * It is safe to call this method from a different thread. 
	 * If the caller happens to be this thread, the sound is played immediately instead.*/
	public synchronized void startPlayback(JavaSoundClip clip)
		{
		clip.playCalled = true;
		clip.stop = false;
		if(Thread.currentThread()==this)//This happens when the method is called from a stop event or sound position event
			playNow(clip);
		else
			playQueue.add(clip);
		}
	/**Join 2 unsigned bytes into a signed short.*/
	public static short joinShort(byte msb, byte lsb)
		{
		return (short) (((msb&0xFF) << 8) | (lsb&0xFF));
		}
	}