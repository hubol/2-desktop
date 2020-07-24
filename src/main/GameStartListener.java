package main;
/**The GameStartListener is designed to receive an event when the loading has completed.
 * You should instantiate an implementation of this interface as soon as possible, 
 * and attach it to Scene with the setGameStarter method.*/
public interface GameStartListener {
	/**Called when the loading is complete. The canvas will then be visible,
	 * and sprites and audio will be loaded and available.*/
	public void gameInitialized();
}
