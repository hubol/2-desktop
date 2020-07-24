package audio;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.LineUnavailableException;

import main.Main;
import main.Scene;

import com.jogamp.openal.sound3d.AudioSystem3D;
import com.jogamp.openal.sound3d.Buffer;
import com.jogamp.openal.sound3d.Context;
import com.jogamp.openal.sound3d.Device;

public class Audio {
	
	
	private Device dev;
	private Context ctx;
	private static AudioLine line;
	
	private static HashMap<String,SoundClip> sounds;
	private static ArrayList<SoundClip> soundList;
	private static ArrayList<Float> targetGainList;
	private static ArrayList<Float> gainFactorList;

	public static boolean useOpenAL = false;
	public static int audioBuffer = 8;
	
	public static double globalGain = 1;
	
	public Audio(){
		
		if (!Main.isApplet()){
			//InputStream file;
			useOpenAL=false;
			/*try {
				file=Main.getInputStream("UseOpenAL.txt");
				
				InputStreamReader w;
				
				w = new InputStreamReader(file);
				BufferedReader p = new BufferedReader(w);
				
				p.readLine();
				String s = p.readLine();
				
				double output;
				output = Calc.parseDouble(s);
				
				if (output>0)
					useOpenAL=true;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		if(useOpenAL){
			AudioSystem3D.init();
			dev = AudioSystem3D.openDevice(null);
		    ctx = AudioSystem3D.createContext(dev);
		    AudioSystem3D.makeContextCurrent(ctx);
		    if (Scene.console)
		    	System.out.println("AudioSystem3D started");
		}
		else{
			try {
				if (Scene.console)
					System.out.println("Initializing JavaSound");
				line = new AudioLine(44100,audioBuffer);
				
			}
			catch (LineUnavailableException e){
				throw new RuntimeException("Could not create java sound output!");
			}
		}
		sounds = new HashMap<String,SoundClip>();
		soundList = new ArrayList<SoundClip>();
		targetGainList = new ArrayList<Float>();
		gainFactorList = new ArrayList<Float>();
		
		
		String[] files = Main.readList("res/sounds/");
		
		for(int i=0; i<files.length; i++){
			Main.setLoadingProgress(.5+((double)i*(.45/(double)files.length)));
			Main.setLoadingText("Loading Sound "+(i+1)+"/"+(files.length));
			//Main.loadingPanel.revalidate();
			loadFile(files[i]);
		}
	}
	/**load up a sound after it's been generated*/
	public static void put(String name,SoundClip sound){
		sounds.put(name, sound);
		soundList.add(sound);
		targetGainList.add(1f);
		gainFactorList.add(1f);
		
	}
	/**delete a sound*/
	public static void delete(String name){
		if (soundExists(name)){
			SoundClip s = sounds.get(name);
			s.stop();
			int index = soundList.indexOf(s);
			targetGainList.remove(index);
			gainFactorList.remove(index);
			soundList.remove(index);
			sounds.remove(name);
			s.delete();
		}
	}
	
	/**check if a sound exists*/
	public static boolean soundExists(String name){
		return (sounds.get(name)!=null);
	}
	
	private static void loadFile(String file){
		load("sounds", file);
	}
	
	public static void loadMusic(String file){
		load("music", file);
	}
	
	public static void load(String dir, String file){
		try {
			String fileName=file; //name without directory
			String dirFile="res/"+dir+"/"+file; //add the directory name
			
			String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			fileName = fileName.substring(0, fileName.lastIndexOf(".")); //keep everything but extension
			
			if (!soundExists(fileName)){
				SoundClip src;
				if(extension.equals(".ogg")){
					src = loadOgg(dirFile);
				}
				else{
					src = loadWav(dirFile);
				}
				put(fileName,src);
				setGlobalGain((float)globalGain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//old loading:
	/*private static void loadFile(File file){
		try {
			String fileName = file.getName(); //strip directory
			String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			fileName = fileName.substring(0, fileName.lastIndexOf(".")); //keep everything but extension
			SoundClip src;
			if(extension.equals(".ogg")){
				src = loadOgg(file);
			}
			else{
				src = loadWav(file);
			}
			put(fileName,src);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
		
	
	private static SoundClip loadWav(String f) throws Exception{
		if(useOpenAL)
			return new ALSoundClip(f);
		else
			return JavaSoundClip.loadWav(f, line);
	}
	
	private static SoundClip loadOgg(String f) throws IOException{
		if(useOpenAL){
			InputStream audioSrc = Main.getInputStream(f);
			//add buffer for mark/reset support
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			
			//Create decompressor stream
		    OggInputStream oggInput = new OggInputStream(bufferedIn);
			//Decompress into a byte stream
		    ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024*256);//May need to change this size with large oggs
			byteOut.reset();
			byte copyBuffer[] = new byte[1024*4];
			boolean done = false;
			while (!done){
				int bytesRead = oggInput.read(copyBuffer, 0, copyBuffer.length);
				byteOut.write(copyBuffer, 0, bytesRead);
				done = (bytesRead != copyBuffer.length || bytesRead < 0);
			}
			//Wrap the byte buffer into the audio buffer
			ByteBuffer byteBuffer = ByteBuffer.wrap(byteOut.toByteArray());
			Buffer buffer = AudioSystem3D.generateBuffers(1)[0];
			//Configure the format
			buffer.configure(byteBuffer, (oggInput.getFormat()==OggInputStream.FORMAT_MONO16)?Buffer.FORMAT_MONO16:Buffer.FORMAT_STEREO16, oggInput.getRate());
			//Wrap the audio buffer into a SoundClip object
			return new ALSoundClip(AudioSystem3D.generateSource(buffer));
		}
		else
			return JavaSoundClip.loadOgg(f, line);
	}
	
	public static SoundClip get(String name){
		return sounds.get(name);
	}
	
	public static void setGlobalGain(float v){
		globalGain = v;
		for (int i=0; i<soundList.size(); i++){
			SoundClip s = soundList.get(i);
			s.setGain(s.unscaledGain);
		}
	}
	
	/**Call to fade the indicated sound to targetGain with increments of gainFactor each step*/
	public static void fade(String soundName,double targetGain, double gainFactor){
		int index = soundList.indexOf(sounds.get(soundName));
		targetGainList.set(index, (float)targetGain);
		gainFactorList.set(index, (float)gainFactor);
	}
	/**Invoked at FPS by Scene to perform fading*/
	public static void step(){
		for(int i=0; i<soundList.size(); i++){
			if (i < soundList.size()){
				SoundClip src = soundList.get(i);
				float currentGain = (float)src.getGain();
				float targetGain = targetGainList.get(i);
				float factor = gainFactorList.get(i);
				if(currentGain!=targetGain){
					if (currentGain<targetGain)
						currentGain+=factor;
					else
						currentGain-=factor;
					
					if(Math.abs(currentGain-targetGain)<factor)
						currentGain=targetGain;
					src.setGain(currentGain);
				}
			}
		}
	}
	
	public static void stopAll(){
		for(int i=0; i<soundList.size(); i++){
			if (i < soundList.size())
				soundList.get(i).stop();
		}
	}
	
	public static AudioLine getLine() {
		return line;
	}
		
}