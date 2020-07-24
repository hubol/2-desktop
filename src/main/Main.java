package main;

import graphics.Sprite;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.media.nativewindow.AbstractGraphicsDevice;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
//import javax.media.opengl.awt.GLJPanel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import audio.Audio;

import graphics.AwesomeAnimator;

/**
 * This class sets up what we need in general.
 * If your program should be able to start as an application, then it is necessary to call this class' main method
 * after you have attached a GameStartListener to Scene and set the desired static fields in this class to customize
 * the startup procedure.  
 */

@SuppressWarnings("serial")
public final class Main
	{
	public static int preferredWidth =	640;
	public static int preferredHeight=	480;
	
	public static int preferredProjectionWidth = 640, preferredProjectionHeight = 480;
	
	public static Color loadText = Color.WHITE, loadFront = Color.GRAY, loadBack = Color.BLACK;
	public static Font loadFont = new Font("Courier New", Font.PLAIN, 12);
	
	public  static int FPS=30;
	
	public static double aspect = (double)preferredWidth/(double)preferredHeight;
	
	public  static boolean isFullscreen=false;
	public  static boolean ignoreResize = false;
	
	/**The dimension of the non-fullscreen window*/
	public static Rectangle windowedBounds;
	public static String title="super game deluxe 2012 GOTY edition";
	public static String loaderImageFileName = "";
	public static String icon = null;
	
	public static boolean started = false;
	
	public static Cursor noCursor;
	static{
		// create a blank cursor
	    Toolkit t = Toolkit.getDefaultToolkit();
	    Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	    noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
	}
	
	public static BufferedImage loadImage;
	
	public static String loadingText = "";
	
	public static Main main;
	public static Audio audio;
	public static Input input;
	public static Scene scene;
	public static AwesomeAnimator animator;
	public static AbstractGraphicsDevice device;
	public static GLProfile profile;
	public static GLCapabilities caps;
	public static GLCanvas canvas;
	public static GameEngineTopContainer topContainer;
	//private static JLabel loadingLabel;
	private static double loadingProgress, atProgress;
	public static JPanel loadingPanel;
	
	public static String DIRECTORY;
	public static boolean RUNNINGFROMJAR;
	
	/**Main method. Should be called after initial setup when run as an application.*/
	public static void main(String[] args){
	System.out.println("MAXIMUM MEMORY: "+Runtime.getRuntime().maxMemory());
	retrieveJarDirectory();
	if(/*RuntimeFixer.validateProcess()*/true)
		{
		GameEngineFrame frame = new GameEngineFrame();
	    main = new Main(frame);
		}
	}
	
	public static boolean retrieveRunningFromJar(){
		return RUNNINGFROMJAR = Main.class.getResource("Main.class").toString().startsWith("jar") || Main.class.getResource("Main.class").toString().startsWith("rsrc");
	}
	
	public static void retrieveJarDirectory(){
		if (retrieveRunningFromJar()){
			//Find the directory we're _actually_ running from(double clicky jar on linux yields a nonsense working directory):
			try{DIRECTORY=URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");}
			catch (UnsupportedEncodingException e) {e.printStackTrace();}//That reaaally shouldn't happen. Ever.
			int fPos  = DIRECTORY.lastIndexOf("/");
			int bPos  = DIRECTORY.lastIndexOf("\\");
			DIRECTORY = DIRECTORY.substring(0,Math.max(fPos, bPos)+1);//Ensure we didn't bring the file name with us
		}
		else
			DIRECTORY = null;
	}
	
	/**
	 * The Main should be instantiated at the game start.
	 * Certain static fields may be set before this to indicate various startup parameters, like a loader image file name.
	 * It will initialize OpenGL and the sound system, and create the Scene.
	 * 
	 * @param container A Frame, Applet, or some other container for the game.
	 */
	public Main(GameEngineTopContainer container){
		topContainer = container;
		container.setContent(getLoaderPanel());
		//let's make some lists
		if (!isApplet()){
			writeDirectoryListing("res/sounds/");
			writeDirectoryListing("res/sprites/");
		}
		
		System.out.println("Loading images");
	    Sprite.loadBufferedImages();
		Main.setLoadingText("Initializing OpenGL");
		Main.setLoadingProgress(.3);
	    System.out.println("Initializing OpenGL");
	    
	    device = GLProfile.getDefaultDevice();
	    profile = GLProfile.get(device,GLProfile.GL2);	
	    caps = new GLCapabilities(profile);
	    caps.setStencilBits(8);
	    caps.setDepthBits(24); 
	    
	    Main.setLoadingText("Initializing Graphics");
	    Main.setLoadingProgress(.4);
	    scene = new Scene(preferredWidth,preferredHeight,preferredProjectionWidth,preferredProjectionHeight);//Note: scene instantiates the GFX

	    canvas = new GLCanvas(caps);
	    canvas.addGLEventListener(scene);
	    canvas.setPreferredSize(new Dimension(preferredWidth,preferredHeight));
	    
	    Main.setLoadingText("Initializing Audio");
	    Main.setLoadingProgress(.5);
	    audio = new Audio();

	    input = new Input();
	    canvas.addKeyListener(input);
	    canvas.addMouseListener(input);
	    canvas.addMouseMotionListener(input);
	    
	    topContainer.addComponentListener(new ComponentAdapter(){
	    	@Override
	    	public void componentResized(ComponentEvent e){
	    		if (!ignoreResize){
	    			windowedBounds = e.getComponent().getBounds();}
		    		ignoreResize=false;
	    		}
			});
	    
	    animator = new AwesomeAnimator(canvas, FPS);
	    
	    topContainer.setContent(canvas);
	    windowedBounds = topContainer.getBounds();
	    canvas.requestFocusInWindow();
	    setFullScreen(false);
	    
	    Main.setLoadingText("Processing Sprites");
	    Main.setLoadingProgress(.95);
	    
	    System.out.println("Displaying canvas");
	    canvas.display();
	}
	
	/**Set the application to fullscreen or windowed mode.
	 * Will fail if run as an applet.*/
	public static void setFullScreen(boolean full)
		{
		isFullscreen = full;
		if(isFullscreen)
	    	{
	    	ignoreResize=true;
	    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		    topContainer.setBounds(new Rectangle(0,0,screenSize.width, screenSize.height + 1));
	    	}
	    else
	    	topContainer.setBounds(windowedBounds);
		}
	
	/**Return a JPanel with the loader image defined with loaderImageFileName.*/
	private static JPanel getLoaderPanel()
	{	
	if(loaderImageFileName==null)
		return null;
	try
		{
		InputStream img = getInputStream(loaderImageFileName);
		loadingProgress = 0;

	    loadImage = ImageIO.read(img);
	    loadingPanel = new JPanel(){
	    	@Override
	    	public void paintComponent(Graphics g){
		    		atProgress = Calc.approach(atProgress, loadingProgress, 4.0);
		    		g.drawImage(loadImage,0,0,getWidth(),getHeight(),null);
		    		g.setColor(loadBack);
		    		g.fillRect(0, getHeight()-16, getWidth(), 16);
		    		g.setColor(loadFront);
		    		g.fillRect(0, getHeight()-16, (int)(getWidth()*atProgress), 16);
		    		g.setColor(loadBack);
		    		g.setFont(loadFont);
		    		g.drawString(loadingText, 4, getHeight()-4);
    		}
    	};
    	
	    loadingPanel.setOpaque(true);
	    loadingPanel.setPreferredSize(new Dimension(loadImage.getWidth(),loadImage.getHeight()));
	    loadingPanel.setDoubleBuffered(true);
	    return loadingPanel;
		}
	catch (IOException e)
		{
    	error("The loader image \""+loaderImageFileName+"\" was missing.\n" +
    			"Please make sure you have extracted the\n" +
    			"archive to your disk before running the game.\n" +
    			"\n" +
    			"Exiting game.", "Loader image missing");
		
		throw new RuntimeException("Loader Image Missing");}
	}
	public static void setLoadingText(String text){
		loadingText = text;
		loadingPanel.repaint(0,  loadingPanel.getHeight()-16,  loadingPanel.getWidth(),  loadingPanel.getHeight());
	}
	public static void setLoadingProgress(double progress){
		loadingProgress = progress;
	}
	
	
	/**This will show a JOptionPane error message.*/
	public static void error(String message, String caption){
		JOptionPane.showMessageDialog(null,message,caption,JOptionPane.ERROR_MESSAGE);
	}
	
	/**This method will return an InputStream for the relative path.
	 * Depending on whether this is run as an applet or application, the returned InputStream might
	 * be a FileInputStream or URLInputStream.*/
	public static InputStream getInputStream(String fileName) throws IOException{
		InputStream inp = topContainer.getInputStream(fileName);
		InputStream dong = new BufferedInputStream(inp);
		return dong;
	}
	
	/**This method creates a list of files in the indicated relative directory and saves the list in that directory as list.txt.
	 * The purpose is to automatically generate the list when the program is run as an application, and to use the directory
	 * list when running as an applet. The reason for using such a list is that it is non-trivial to retrieve a list of files
	 * on a web server.*/	
	public static void writeDirectoryListing(String dir){
		
		File a = new File(DIRECTORY, dir);
		File[] files = a.listFiles();
		
		File b = new File(DIRECTORY, dir+"list.txt");
		FileWriter write;
		try {
			write = new FileWriter(b);
			PrintWriter print;
			print=new PrintWriter(write);
			
			for(int i=0; i<files.length; i++){
				if (files[i].compareTo(b)!=0){
					print.println(files[i].getName());
				}
			}
			
			print.close();
			
		} catch (IOException e) {
			System.err.println(dir+"list.txt cannot be created!!!!!!!!");
			e.printStackTrace();
		}
		
	}
	
	/**This will look for list.txt in the directory, and return an array of the file names in the list. 
	 * Calling writeDirectoryListing() with the directory will automatically generate such a file,
	 * if the program has the appropriate permissions.*/
	public static String[] readList(String dir){
		InputStream list;
		
		try {
			list = getInputStream(dir+"list.txt");
			
			InputStreamReader w;
			
			w = new InputStreamReader(list);
			BufferedReader p = new BufferedReader(w);
			
			String s = p.readLine();
			
			ArrayList<String> a=new ArrayList<String>();
			
			do{
				a.add(s);
				s=p.readLine();
			}
			while (s!= null);
			
			String[] array=new String[a.size()];
			a.toArray(array);
			
			return array;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("the list was not found or something");
		}
	}
	
	/**@return whether the top level container is an applet.*/
	public static boolean isApplet(){
		return (topContainer instanceof GameEngineApplet);
	}
	
	public static GameEngineTopContainer getContainer(){
		return topContainer;
	}
}