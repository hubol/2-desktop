package main;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**GameEngineFrame is a top level container suitable for containing the game.*/
public final class GameEngineFrame extends Frame implements GameEngineTopContainer  {
	private static final long serialVersionUID = 2135505492369597569L;
	Component component;
	
	public GameEngineFrame(){
		setCursor(Main.noCursor);
	    setUndecorated(true);
	    setResizable(true);
	    setTitle(Main.title);
	    
	    if (Main.icon != null){
			try {
				setIconImage(ImageIO.read(new File(Main.DIRECTORY, Main.icon)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    }
	    
	    addWindowListener(new WindowAdapter()
	    {public void windowClosing(WindowEvent e)
	        {dispose();}
	    	});
	}

	@Override
	public void setContent(Component c) {
		if(component!=null)
			remove(component);
		if(c!=null)
			add(c);
		component = c;
		pack();
		if(!isVisible())
			setVisible(true);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void dispose()
		{
		System.out.println("System exit");
		super.dispose();
		System.exit(0);
		}

	@Override
	public InputStream getInputStream(String fileName) throws IOException {
		//System.out.println(Main.DIRECTORY);
		return new FileInputStream(new File(Main.DIRECTORY, fileName));
	};
	
}
