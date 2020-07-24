package audio;

import java.io.IOException;
import java.io.InputStream;

//import javax.sound.sampled.UnsupportedAudioFileException;

import main.Main;

import com.jogamp.openal.sound3d.AudioSystem3D;
import com.jogamp.openal.sound3d.Buffer;
import com.jogamp.openal.sound3d.Source;

public class ALSoundClip extends SoundClip {

	private Source source;
	private double pan;
	
	public ALSoundClip(String f) throws IOException, com.jogamp.openal.UnsupportedAudioFileException {
		InputStream audioSrc = Main.getInputStream(f);

		source = AudioSystem3D.loadSource(audioSrc);
		source.setSourceRelative(true);
	}

	public ALSoundClip(Source generateSource) {
		this.source=generateSource;
		this.source.setSourceRelative(false);
	}

	@Override
	public void play() {
		source.play();
		
	}

	@Override
	public void stop() {
		source.stop();
		
	}

	@Override
	public void setLooping(boolean looping) {
		source.setLooping(looping);
		
	}

	@Override
	public void setGain(double gain) {
		unscaledGain = gain;
		source.setGain((float)(gain * Audio.globalGain));
		
	}

	@Override
	public double getGain() {
		return source.getGain();
	}

	@Override
	void delete() {
		source.delete();
	}

	@Override
	public void setPan(double pan) {
		if(source.getBuffer().getNumChannels()>1)
			System.err.println("Don't pan stereo samples");
		pan=Math.min(1, Math.max(pan, -1));
		source.setPosition((float)pan, 0,1);
		this.pan=pan;
		
	}

	@Override
	public void setPitch(double frequency) {
		source.setPitch((float)frequency);
		
	}

	@Override
	public double getPitch() {
		return source.getPitch();
	}

	@Override
	public double getPan() {
		return pan;
	}

	@Override
	public double getSecondLength() {	
		Buffer b = source.getBuffer();
		int bytes = b.getSize();
		int bytesPerSample = b.getBitDepth()*b.getNumChannels()/8;
		int samplesPerSecond = b.getFrequency();
		return (bytes/bytesPerSample)/samplesPerSecond;
	}
}
