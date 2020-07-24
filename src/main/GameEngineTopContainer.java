package main;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.InputStream;

/**GameEngineTopContainer defines the necessary functionality for a class serving as a top level container for the game.
 * This will typically be an Applet, Frame or Panel.*/
public interface GameEngineTopContainer{
	/**The container will have a single component, set by this method.
	 * The component is either the loading panel or the game canvas.*/
	public void setContent(Component c);
	/**Register a componentlistener to detect when the container changes size.*/
	public void addComponentListener(ComponentListener l);
	/**Get the current (outer) bounds of the container.*/
	public Rectangle getBounds();
	/**Set the current(outer) bounds of the container.*/
	public void setBounds(Rectangle r);
	/**Get an input stream for the relative file name.
	 * The input stream should be obtained from a local file if run as an application,
	 * and from a server URL if run as an applet.*/
	public InputStream getInputStream(String fileName) throws IOException;
}
