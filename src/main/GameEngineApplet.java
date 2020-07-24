package main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**GameEngineApplet is a top level container suitable for containing the game.
 * If the game should be startable as an applet, then it is necessary to create a subclass in your game project.*/
public abstract class GameEngineApplet extends Applet implements GameEngineTopContainer {
	private static final long serialVersionUID = 6435713813552345290L;
	protected Component component;
	
	@Override 
	public final void init(){
		Thread t = new Thread(){public void run() {
			initializeApplet();
			new Main(GameEngineApplet.this);}};
		t.start();
	}
	/**This method is called in a new thread started in the init method.
	 * You should attach a GameStartListener to Scene in this method,
	 * and set the desired fields in Main to customize the startup sequence.*/
	public abstract void initializeApplet();
		
	
	@Override
	public void setContent(Component c) {
		if(!(getLayout() instanceof BorderLayout))
			setLayout(new BorderLayout());
		if(component!=null)
			remove(component);
		if(c!=null)
			add(c,BorderLayout.CENTER);
		component = c;
		validate();
	}
	
	@Override
	public InputStream getInputStream(String fileName) throws IOException {
		return new URL(getCodeBase(),fileName).openStream();
	};
	
}
