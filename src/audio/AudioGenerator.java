package audio;

import java.nio.ByteBuffer;

import com.jogamp.openal.sound3d.AudioSystem3D;
import com.jogamp.openal.sound3d.Buffer;

public class AudioGenerator {
	
	/**make a sine wave with frequency and length
	 * @param sf the sine wave frequency
	 * @param f the sampling rate*/
	public static SoundClip makeSine(int sf, int f){
	    byte[] bytes = new byte[f];//1 second
	    
	    for(int i=0; i<bytes.length; i++){
	    	bytes[i] = (byte)(127+127*Math.sin(i*2*Math.PI*sf/f));
	    }
	    
		return finish(bytes,f);
	}

	public static SoundClip makeNoise(int f){
		//Decompress into a byte stream
	    byte[] bytes = new byte[f];//1 second
	    
	    for(int i=0; i<bytes.length; i++){
	    	bytes[i] = (byte)(255*Math.random());
	    }
	    
	    return finish(bytes,f);
	}
	
	public static SoundClip finish(byte[] bytes, int f){
		if(Audio.useOpenAL)  {
			ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
			//stuff
			Buffer buffer = AudioSystem3D.generateBuffers(1)[0];
			//Configure the format
			buffer.configure(byteBuffer, Buffer.FORMAT_MONO8, f);
			//Wrap the audio buffer into a source object
			return new ALSoundClip(AudioSystem3D.generateSource(buffer));
		}
		else
			return new JavaSoundClip("Generated", bytes, f, 8, 1, Audio.getLine());
	}

}
