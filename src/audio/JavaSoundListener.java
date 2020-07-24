package audio;

public interface JavaSoundListener
	{
	/**The indicated clip stops in milliseconds.<br/>
	 * Note: This method is called from a different thread, so excercise caution.
	 * This method must execute quickly.
	 * Sounds played from this method will be played immediately with no delay.*/
	public void soundStop(JavaSoundClip c, int milliseconds);
	
	/**Used as a position listener.<br/>
	 * Note: This method is called from a different thread, so excercise caution.
	 * This method must execute quickly.
	 * Sounds played from this method will be played immediately with no delay.*/
	public void soundAction(JavaSoundClip c, int framePosition);
	}
