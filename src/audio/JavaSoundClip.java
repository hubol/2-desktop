 package audio;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.Main;
import main.Scene;

/**
 * This class is designed to contain and play back sound sample data, 
 * in a way that is uncompromisingly cross-platform and plug-and-play.
 * 
 * It provides methods for loading from wav and ogg files.
 * Format support is limited to: 
 * 8 bit unsigned PCM
 * 16 bit signed PCM
 * 
 * Any sample rate is supported, but the clip is expected to deliver data to a single 16 bit fixed rate AudioLine stream.
 * Both mono and stereo samples are supported.
 * 
 * The class provides methods for operations that should be expected, i.e. 
 * frequency changes, panning, volume, looping etc.
 * */
public class JavaSoundClip extends SoundClip
	{
	/**Load a PCM waveform file. Format support is limited.*/
	public static JavaSoundClip loadWav(String f, AudioLine line) throws IOException, UnsupportedAudioFileException
		{
		InputStream audioSrc = Main.getInputStream(f);
		
		AudioInputStream wavInput = AudioSystem.getAudioInputStream(audioSrc);
		
		//Decompress into a byte stream
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024*256);
		byteOut.reset();
		byte copyBuffer[] = new byte[1024*4];
		boolean done = false;
		while (!done) 
			{
			int bytesRead = wavInput.read(copyBuffer, 0, copyBuffer.length);
			if(bytesRead==-1)
				{
				done=true;
				break;
				}
			byteOut.write(copyBuffer, 0, bytesRead);
			done = (bytesRead != copyBuffer.length || bytesRead < 0);
			}
		//Convert to array
		byte[] dataArray = byteOut.toByteArray();
		//Find format data
		AudioFormat format = wavInput.getFormat();
		//Wrap the array and format data into a clip object
		JavaSoundClip clip = new JavaSoundClip(f,dataArray,(int) format.getSampleRate(),format.getSampleSizeInBits(),format.getChannels(),line);
		if(format.getEncoding() == AudioFormat.Encoding.ULAW || format.getEncoding() == AudioFormat.Encoding.ALAW)
			System.err.println(format.getEncoding()+" not implemented: "+f);
		else if(format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED && format.getSampleSizeInBits()==8)
			System.err.println(format.getEncoding()+" 8 bit not implemented: "+f);
		else if(format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED && format.getSampleSizeInBits()==16)
			System.err.println(format.getEncoding()+" 16 bit not implemented: "+f);
		
		return clip;
		}
	/**Load an ogg vorbis file using pure java JOgg, JOrbis and EasyOgg*/
	public static JavaSoundClip loadOgg(String f, AudioLine line) throws IOException
		{
		InputStream audioSrc = Main.getInputStream(f);
		
		//Create decompressor stream
        OggInputStream oggInput = new OggInputStream(audioSrc);
		//Decompress into a byte stream
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024*256);
		byteOut.reset();
		byte copyBuffer[] = new byte[1024*4];
		boolean done = false;
		while (!done) 
			{
			int bytesRead = oggInput.read(copyBuffer, 0, copyBuffer.length);
			byteOut.write(copyBuffer, 0, bytesRead);
			done = (bytesRead != copyBuffer.length || bytesRead < 0);
			}
		//Convert to array
		byte[] dataArray = byteOut.toByteArray();
		//Wrap the array and format data into a clip object
		JavaSoundClip clip = new JavaSoundClip(f,dataArray,oggInput.getRate(),16,(oggInput.getFormat()==OggInputStream.FORMAT_STEREO16?2:1),line);
		return clip;
		}
	
	/**The frame rate, in Hz. Note that a frame may contain more than one sample.*/
	int frameRate;
	/**Bits per sample. Should be 8 or 16.*/
	int bitsPerSample;
	/**Number of channels. Should be 1 or 2, i.e. mono or stereo*/
	int channels;
	/**The raw byte data array*/
	byte[] dataArray;
	/**The total number of frames. A frame contains 'channels' number of samples.*/
	int numFrames;
	/**The playback frequency with which the sound will be played at its intended speed, 
	 * with respect to the provided AudioLine's format*/
	double stdFrequency;
	/**The ouput AudioLine. The AudioLine is responsible for fetching data and mixing samples.*/
	AudioLine myLine;
	
	/**The current playback position, in frames. 
	 * This is accurate to the buffer length of the output Audioline.*/
	double currentFramePos;
	/**Whether the clip currently in the list of sounds being played.*/
	boolean playback;
	/**Whether the clip should be removed from the list of sounds being played asap.*/
	boolean stop;
	/**Whether play was called since the last time the sound stopped*/
	boolean playCalled;
	/**Whether the clip will loop, if it is also playing*/
	boolean isLooping;
	
	/**The current playback speed. stdFrequency provides a default value.*/
	double frequency;
	/**The current pan. -1 is completely left, 1 is completely right, 0 is centered.*/
	double pan;
	/**The current gain with a multiplier of Audio.globalGain. May take any value, but 0 is completely silent and 1 is the original volume.*/
	double gain;
	
	/**The AudioLine uses a linked list implementation to keep track of which sounds are playing.
	 * These variables should only be modified by AudioLine. 8D */
	JavaSoundClip listNext,listPrev;
	/**The file name sans directory and extension*/
	String name;
	/**The indicated object is notified when the clip reaches actionPosition(in frames).*/
	JavaSoundListener positionListener;
	/**The positionListener is notified when the clip reaches the specified playback position(in frames).*/
	int actionPosition;
	
	/**@param name the full file name.*/
	public JavaSoundClip(String name, byte[] dataArray, int sampleRate, int bitsPerSample, int channels, AudioLine line)
		{
		int lastSeparatorIndex = Math.max(name.lastIndexOf('/'),name.lastIndexOf('\\'))+1, lastDotIndex = name.lastIndexOf('.');
		if(lastDotIndex==-1)
			 lastDotIndex = name.length();
		this.name = name.substring(lastSeparatorIndex, lastDotIndex);
		
		positionListener = null;
		
		this.channels = channels;
		this.dataArray = dataArray;
		this.frameRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		numFrames = dataArray.length*8/channels/bitsPerSample;
		
		myLine = line;
		stdFrequency = (double)sampleRate/line.frameRate;
		gain = 1;
		unscaledGain = 1;
		pan = 0;
		frequency=stdFrequency;
		
		currentFramePos=0;
		playback=false;
		stop=false;
		playCalled = false;
		isLooping=false;
		
		
		listNext=null;
		listPrev=null;
		}
	
	/**This will create a clone of the clip. The clone will use the same data array and thus take up very little memory.*/
	@Override
	public JavaSoundClip clone()
		{
		return new JavaSoundClip(name, dataArray, frameRate, bitsPerSample, channels, myLine);
		}
	/**Start playing from 0*/
	public synchronized void play()
		{
		myLine.startPlayback(this);
		}
	
	/**The JavaSoundListener's action event will be fired exactly when this sound reaches the position of playback(in frames).
	 * The listener is unregistered right before the action event is called.*/
	public void setPositionListener(JavaSoundListener listener, int position)
		{
		this.positionListener = listener;
		this.actionPosition = position;
		}
	
	/**Stop and set position to 0*/
	public synchronized void stop()
		{
		if(playCalled)
			stop = true;
		}
	/**Set frequency. 1 will always be the original speed of the loaded sample.*/
	public synchronized void setPitch(double frequency)
		{
		this.frequency = stdFrequency*frequency;
		}
	/**Set pan from -1 to 1, where -1 is left and +1 is right.*/
	public synchronized void setPan(double pan)
		{
		this.pan=pan;
		}
	/**Set gain. Ordinary values would lie between 0 (no sound) and 1 (original volume), but any values are permitted.*/
	public synchronized void setGain(double gain)
		{
		unscaledGain = gain;
		this.gain=gain * Audio.globalGain;
		}
	/**Set wether to loop playback when the sound playback reaches the end.*/
	public void setLooping(boolean looping)
		{
		isLooping = looping;
		}
	/**Get length in frames.*/
	public int getFrameLength()
		{
		return numFrames;
		}
	/**Get length in milliseconds, not taking frequency change into account.*/
	public int getMillisecondLength()
		{
		int bytes = dataArray.length;
		int bytesPerFrame = bitsPerSample*channels/8;
		int framesPerSecond = frameRate;
		return (int)(1000*(bytes/bytesPerFrame)/framesPerSecond);
		}
	/**Get length in seconds, not taking frequency change into account.*/
	public double getSecondLength()
		{
		int bytes = dataArray.length;
		int bytesPerFrame = bitsPerSample*channels/8;
		int framesPerSecond = frameRate;
		return (bytes/bytesPerFrame)/framesPerSecond;
		}
	/**Get the playback position in milliseconds. Is accurate to the buffer length of the AudioLine.
	 * The position is also ahead of what is actually heard because of the buffer length.*/
	public int getPosition()
		{
		return (int) (currentFramePos*1000/frameRate);
		}
	
	/**Get file name without directory or extension.*/
	public String getName()
		{
		return name;
		}

	public int getFrameRate()
		{
		return frameRate;
		}
	@Override
	public synchronized double getGain() {
		return gain;
	}
	@Override
	void delete() {
		//Do nothing
	}
	@Override
	public synchronized double getPitch() {
		return frequency/stdFrequency;
	}
	@Override
	public synchronized double getPan() {
		return pan;
	}
	public void stopped() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void finalize(){
		if (Scene.console)
			System.out.println(this.name+" deallocated");
	}
}