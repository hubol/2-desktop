
package audio;

public abstract class SoundClip {
	
	public double unscaledGain;
	
	public abstract void play();
	public abstract void stop();
	public abstract void setLooping(boolean looping);
	public abstract void setGain(double gain);
	public abstract double getGain();
	abstract void delete();
	public abstract void setPan(double pan);
	public abstract void setPitch(double frequency);
	public abstract double getPitch();
	public abstract double getPan();
	public abstract double getSecondLength();
}
