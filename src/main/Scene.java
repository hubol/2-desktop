package main;

import graphics.Sprite;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


import audio.Audio;

import java.awt.event.KeyEvent;


public class Scene implements GLEventListener {

	public static int width;
	public static int height;
	public static GL2 gl;
	private static ArrayList<Entity> instances;
	private static Queue<Entity> stepQueue;
	
	public static int screenTexture;
	public static double screenTextureDX;
	public static double screenTextureDY;
	
	public static double viewX;
	public static double viewY;
	
	public static boolean keepAspectRatio;
	public static boolean keepViewport;
	
	private static int canvasX, canvasY;
	public static int canvasWidth;
	public static int canvasHeight;
	
	public static int projectionWidth, projectionHeight;
	
	public static int viewportX, viewportY;
	public static int viewportWidth, viewportHeight;
	
	public static boolean resized = false;
	public static boolean killOnEscape = true;
	
	public static boolean console = true;
	
	private static GameStartListener gameStarter;
	
	//this is for checking to see how many times you loop through the god damn entity list
	public static boolean printEntityChecks = false;
	public static int entityChecks = 0;
	
	//checking triangles lol
	public static int trianglesDrawn = 0;
	
	public Scene(int windowedWidth, int windowedHeight) {
		width=windowedWidth;
		height = windowedHeight;
		
		canvasWidth = width;
		canvasHeight = height;
		viewportWidth = width;
		viewportHeight = height;
		
		projectionWidth = width;
		projectionHeight = height;
	}
	
	public Scene(int windowedWidth, int windowedHeight, int projectionW, int projectionH) {
		width=windowedWidth;
		height = windowedHeight;
		
		canvasWidth = width;
		canvasHeight = height;
		viewportWidth = width;
		viewportHeight = height;
		
		projectionWidth = projectionW;
		projectionHeight = projectionH;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// When the program starts
		gl = (GL2) drawable.getGL();
		
		gl.glAlphaFunc(GL.GL_GREATER, 0);
		
		if (console){
			System.out.println("scene.init just got called");
			int[] sbits = new int[1];
			  gl.glGetIntegerv(GL2.GL_STENCIL_BITS, sbits, 0);
			  System.out.println("stencil bits: "+sbits[0]);
		}
		
		viewX=0;
		viewY=0;
		
		if (console)
			System.out.println("GL NON POWER OF TWO TEXTURES AVAILABLE: "+gl.isExtensionAvailable("GL_ARB_texture_non_power_of_two"));
		
		Main.setLoadingText("Initialize Sprites");
		Sprite.loadTextures(); //load up them sprites i think
		
		instances = new ArrayList<Entity>();
		stepQueue = new LinkedList<Entity>();
		
		screenTexture = -1;
		regenScreenTexture(width,height);

		Main.started=true;
		if(gameStarter==null)
			throw new RuntimeException("No game starter. " +
					"Attach a GameStartListener implementation with the setGameStarter method first.");
		else
			gameStarter.gameInitialized();
		Main.animator.start();
	}

	public void regenScreenTexture(int ww, int hh) {
		if(screenTexture!=-1){
			gl.glDeleteTextures(1, new int[]{screenTexture}, 0);
		}
		int[] gen = new int[1];
		gl.glGenTextures(1, gen, 0);
		screenTexture = gen[0];
		int w = (int)Math.pow(2,Math.ceil(Calc.log2(ww)));
		int h = (int)Math.pow(2,Math.ceil(Calc.log2(hh)));
		ByteBuffer buf = ByteBuffer.allocate(w*h*3);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D,Scene.screenTexture);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, w, h, 0, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, buf);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		screenTextureDX = (double)ww/w;
		screenTextureDY = (double)hh/h;
	}
	
	public static void screenSave(String filename){
		int w = Main.topContainer.getBounds().width, h = Main.topContainer.getBounds().height;
		//int w = width, h = height;
		//int w = viewportWidth, h = viewportHeight;
		
		//differentiate format from file name
		String format;
		String[] parts;
		parts = filename.split("\\.");
		format=parts[1];
		//capture data from screen
		ByteBuffer pixels = ByteBuffer.allocateDirect(w*h*3);
		gl.glReadPixels(0, 0, w, h, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, pixels);
		BufferedImage screen = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		//flip the data
		pixels.rewind();
		byte[] data = new byte[w*h*3];
		for(int j = h-1; j>=0; j--){
			for(int i = 0; i<w; i++){
				data[(j*w+i)*3  ] = pixels.get();
				data[(j*w+i)*3+1] = pixels.get();
				data[(j*w+i)*3+2] = pixels.get();
			}
		}
		//save the data
		screen.getRaster().setDataElements(0, 0, w, h, data);
		try {
			ImageIO.write(screen, format.toUpperCase(), new File(Main.DIRECTORY, filename));
		} catch (IOException e) {
			if (console)
				System.out.println("saving screenshot "+filename+" failed for whatever reason");
			e.printStackTrace();
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		      if (!drawable.getContext().isCurrent())
		        drawable.getContext().makeCurrent(); 
		
			//boolean enter = Input.checkKey(KeyEvent.VK_ENTER);
			/*if(enter && !lastEnter && Input.checkKey(KeyEvent.VK_ALT)){
				Main.setFullScreen(!Main.isFullscreen);
			}*/
			//lastEnter = enter;
			
			if (!keepViewport){
				if(keepAspectRatio){
					if(canvasWidth==0 || canvasHeight==0)
					    return;
						
						viewportWidth = canvasWidth;
						viewportHeight = canvasHeight;
						
						double ratio = (double)canvasWidth/canvasHeight;
								   if(ratio>Main.aspect)
								    viewportWidth= (int)(canvasHeight*Main.aspect);
								   else
								    viewportHeight= (int)(canvasWidth*(1/Main.aspect));
								   
								   viewportX = (canvasWidth-viewportWidth)/2;
								   viewportY = (canvasHeight-viewportHeight)/2;
				}
				else{
					viewportX = canvasX;
					viewportY = canvasY;
					viewportWidth = canvasWidth;
					viewportHeight = canvasHeight;
				}
				   
				drawable.getGL().glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		
				keepViewport = true;
			}
			
			int error = gl.glGetError();
			while(error!=0){
				System.err.println("OpenGL error: "+new GLU().gluErrorString(error));
				error = gl.glGetError();
			}
			
			// loop
			if(Input.checkKey(KeyEvent.VK_ESCAPE) && !Main.isApplet() && killOnEscape){
				System.exit(0);
			}
			gl = (GL2) drawable.getGL();
			
			//Step
			entityChecks = 0;
			Input.framePressRefresh(); //okay lets refresh the input stuff (for clicking and pressing on a single frame!!!)
			
			stepQueue = new LinkedList<Entity>();
			for(int i=0; i<instances.size(); i++){
				stepQueue.offer(instances.get(i));
			}
			while(!stepQueue.isEmpty()){
				Entity e = stepQueue.poll();
				if(!e.isDestroyed() && e.stepActive)
					e.step();
			}
			
			if (printEntityChecks && console)
				System.out.println(entityChecks + " entity list checks in this step");
			
			Audio.step();
			
			trianglesDrawn = 0;
			//Render
			Collections.sort(instances);
			for(int i=0; i<instances.size(); i++){
				Entity e = instances.get(i);
				if(e.visible){
					e.render();
				}
			}
	}
	/**Register a GameStartListener implementation. Should be done as early as possible.
	 * The gameInitialized method will be fired when the game is loaded.
	 * Only one GameStartListener can be registered*/
	public static void setGameStarter(GameStartListener gameStarter){
		Scene.gameStarter = gameStarter;
	}
	
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// When the program closes
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		  Scene.canvasX = x;
		  Scene.canvasY = y;
		  Scene.canvasWidth=w;
		  Scene.canvasHeight=h;
		  Scene.resized = true;
		  
		  Scene.keepViewport = false;
		  
		regenScreenTexture(w,h);
	}

	public static void addInstance(Entity entity) {
		instances.add(entity);
		stepQueue.add(entity);
	}
	public static void removeInstance(Entity entity) {
		instances.remove(entity);
	}

	public static boolean collision(Entity subject,double x, double y, int... groups) {
		entityChecks += 1;
		for(int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			if(e.isOfCollisionGroup(groups) && e != subject){
				if (subject.collidesWith(e,x,y)) //optimization?
					return true;
			}
		}
		return false;
	}
	
	public static Entity[] getInstances(){
		Entity[] r = new Entity[instances.size()];
		Object[] s = instances.toArray();
		for(int i=0; i<r.length; i++){
			r[i] = (Entity)s[i];
		}
		return r;
	}
	
	public static String[] readEntities(File f){
		ArrayList<String> list = new ArrayList<String>();
		try {
			FileReader w = new FileReader(f);
			BufferedReader p = new BufferedReader(w);
			
			String className = null;
			int numParameters = 0;
			Queue<Object> parameterValues = new LinkedList<Object>();
			Queue<Class<?>> parameterTypes = new LinkedList<Class<?>>();
			
			boolean nextLineIsClassName = true;
			//skip begin metadata line
			String s = p.readLine();
			
			//read the metadata
			while (!s.equals("END OF METADATA")){
				s = p.readLine();
				list.add(s);
			}
			//skip end line
			s = p.readLine();
			
			while (s!=null){
				
				if (s.equals("end"))
				{
					//Convert input parameters to arrays
					Object[] values = new Object[numParameters];
					Class<?>[] types = new Class<?>[numParameters];
					for (int k=0; k<values.length; k++){
						values[k] = parameterValues.poll();
						types[k] = parameterTypes.poll();
					}
					
					//Find Class with the name from the file
					Class<?> currentClass = Class.forName(className);
					//Find the correct Constructor for this class
					Constructor<?>[] constructors = currentClass.getConstructors();
					for(int j=0; j<constructors.length; j++)
					{
						Constructor<?> c = constructors[j];
						
						Class<?>[] constructorParamTypes = c.getParameterTypes();
						
						if (constructorParamTypes.length==numParameters){
							boolean mismatch = false;
							for (int k=0; k<numParameters; k++){
								if (types[k]!=constructorParamTypes[k]){
									mismatch = true;
									break;
								}
							}
							if(!mismatch){
								//Correct constructor detected
								c.newInstance(values);
							}
						}
						
						
					}
					
					//Empty parameter lists
					parameterValues = new LinkedList<Object>();
					parameterTypes = new LinkedList<Class<?>>();
					numParameters=0;
					nextLineIsClassName = true;
				}
				else if(nextLineIsClassName){
					className = s;
					nextLineIsClassName = false;
				}
				else if(s.startsWith("i:")){//s is an int
					parameterTypes.add(int.class);
					parameterValues.add(Calc.parseInt(s.substring(2)));
					numParameters++;
				}
				else if(s.startsWith("s:")){//s is a string
					parameterTypes.add(String.class);
					parameterValues.add(s.substring(2));
					numParameters++;
				}
				else if(s.startsWith("ia:")){//s is an int array (ia:6,9,420)
					parameterTypes.add(int[].class);
					String z = s.substring(3);
					String[] lizt = z.split(",");
					int[] add = new int[lizt.length];
					for (int i=0; i<lizt.length; i++)
						add[i] = Calc.parseInt(lizt[i]);
					parameterValues.add(add);
					numParameters++;
				}
				else{//s can't be anything but a double
					parameterTypes.add(double.class);
					parameterValues.add(Calc.parseDouble(s));
					numParameters++;
				}
				
				s = p.readLine();
				
			}
			
			p.close();
			w.close();
			
			//spit out metadata
			String[] array = new String[list.size()];
			list.toArray(array);
			
			return array;
			
		} 
		catch (Exception e) {
			System.err.println("Level parsing fail: "+f.getName());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[] readEntities(String file){
		return readEntities(new File(Main.DIRECTORY, file));
	}
	
	/*public static Entity[] getCollisionGroup(int... input) {
		int numberOf=0;
		
		for (int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			if (e.isOfCollisionGroup(input))
				numberOf+=1;
		}
		
		Entity[] r = new Entity[numberOf];
		
		int at=0;
		for (int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			
			if (e.isOfCollisionGroup(input)){
				r[at]=e;
				at+=1;
			}
		}
		
		return r;
	}
	
	@SuppressWarnings("rawtypes")
	public static Entity[] getClassInstances(Class input) {
		int numberOf=0;
		
		for (int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			if (e.getClass() == input)
				numberOf+=1;
		}
		
		Entity[] r = new Entity[numberOf];
		
		int at=0;
		for (int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			
			if (e.getClass() == input){
				r[at]=e;
				at+=1;
			}
		}
		
		return r;
	}*/
	
	public static <T> int instanceNumber(Class<T> c){
		entityChecks += 1;
		
		int output = 0;
		for (Entity e: instances){
			if (c.isInstance(e) && !e.isDestroyed()){
				output += 1;
			}
		}
		return output;
	}
	
	public static <T> boolean instanceExists(Class<T> c){
		if (instanceNumber(c) > 0)
			return true;
		return false;
	}
	
	public static ArrayList<Entity> getCollisionGroupList(int... c){
		entityChecks += 1;
		
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e: instances){
			if (e.isOfCollisionGroup(c))
				list.add(e);
		}
		return list;
	}
	
	public static <T> ArrayList<T> getEntityList(Class<T> c){
		entityChecks += 1;
		
		ArrayList<T> list = new ArrayList<T>();
		for (Entity e: instances){
			if (c.isInstance(e))
				list.add(c.cast(e));
		}
		return list;
	}
	
	public static <T extends Entity> void destroy(Class<T> c){
		ArrayList<T> list = new ArrayList<T>();
		for (Entity e: instances){
			if (c.isInstance(e))
				list.add(c.cast(e));
		}
		
		for (int i=0; i<list.size(); i++)
			list.get(i).destroy();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getCollidedEntities(Entity subject,double x, double y, int... groups) {
		entityChecks += 1;
		
		ArrayList<T> list = new ArrayList<T>();
		for(int i=0; i<instances.size(); i++){
			Entity e = instances.get(i);
			if(e.isOfCollisionGroup(groups) && subject.collidesWith(e,x,y) && e != subject)
				list.add((T)e);
		}
		return list;
	}

}
