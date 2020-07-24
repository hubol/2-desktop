package graphics;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import main.Calc;
import main.Main;
import main.Mask;
import main.Scene;
/**This class contains a library of Sprites loaded from the sprite directory.
 * The sprites are awesome and in RGBA format.*/
public class Sprite {
	//Begin static part.
	public final static int CENTERED = -1;
	public final static int EAST= 0;
	public final static int NORTHEAST = 1;
	public final static int NORTH = 2;
	public final static int NORTHWEST = 3;
	public final static int WEST = 4;
	public final static int SOUTHWEST = 5;
	public final static int SOUTH = 6;
	public final static int SOUTHEAST = 7;
	
	//TODO remove duplicate rendering code!!!!!!
	
	/**Temporary storage*/
	private static ArrayList<BufferedImageContainer> images;
	/**Permanent storage*/
	private static HashMap<String,Sprite> sprites;
	
	/**Download images, but do not upload to OpenGL.
	 * Will cause the loading screen to update.*/
	public static void loadBufferedImages(){
		images = new ArrayList<BufferedImageContainer>();
		
		String[] files = Main.readList("res/sprites/");
		
		for(int i=0; i<files.length; i++){
			Main.setLoadingProgress(i*.3/files.length);
			Main.setLoadingText("Loading Sprite "+(i+1)+"/"+(files.length));
			//Main.loadingPanel.revalidate();
			preLoadFile(files[i],"res/sprites/");
		}
	}
	/**Upload previously downloaded BufferedImages into OpenGL.
	 * Must be called when a GL Context is current on the thread.*/
	public static void loadTextures(){
		sprites = new HashMap<String,Sprite>();
		for(BufferedImageContainer c: images){
			c.generateSprite();
		}
		images = null;
	}
	/**Load a file into a BufferedImage array.
	 * Will put the result in the images list.
	 * 
	 * If the file name ends with an underscore followed by a number, followed by the file extension,
	 * The image will be assumed to be an animation strip with that number of subimages.
	 * It will then be split into that number of Textures.
	 * Otherwise it is assumed to have a single subimage.*/
	private static void preLoadFile(String fname, String dir){
		try {
			BufferedImageContainer imgs = loadBufferedImageContainer(fname, dir, 0);
			images.add(imgs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**Load a file into a texture.
	 * Will put the result in the sprites list.
	 * 
	 * If the file name ends with an underscore followed by a number, followed by the file extension,
	 * The image will be assumed to be an animation strip with that number of subimages.
	 * It will then be split into that number of Textures.
	 * Otherwise it is assumed to have a single subimage.*/
	public static void loadFile(String fname, String dir, int num){
		try {
			loadBufferedImageContainer(fname, dir, num).generateSprite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static BufferedImageContainer loadBufferedImageContainer(String fname, String dir, int num) {
		String f = dir+fname;
		String name;
		
		int lastUnderscorePosition = fname.lastIndexOf('_');
		int frameNumber;
		if(lastUnderscorePosition==-1){
			frameNumber=1;
			name = fname.substring(0,fname.lastIndexOf('.'));
		}
		else{
			String numStr = fname.substring(lastUnderscorePosition+1, fname.lastIndexOf('.'));
			Integer temp = Calc.parseInt(numStr);
			if(temp==null){
				name = fname.substring(0,fname.lastIndexOf('.'));
				frameNumber=1;
			}
			else{
				name = fname.substring(0,lastUnderscorePosition);
				frameNumber=temp;
			}
			
		}
		
		if (num!=0)
			frameNumber=num;
		
		BufferedImage[] img = null;
		
		try {
			img = Calc.createByteABGRFrames(ImageIO.read(Main.getInputStream(f)),frameNumber);
		} catch (IOException e) {
			System.err.println("Missing/Corrupt image: "+fname);
			e.printStackTrace();
			System.exit(1);
		}
		BufferedImageContainer imgs = new BufferedImageContainer();
		imgs.images = img;
		imgs.frameNumber=frameNumber;
		imgs.name = name;
		
		return imgs;
	}
	/**Gets the sprite object with the given name.
	 * The name is derived from the file name, 
	 * with any file extension and frame number part stripped away.
	 * The frame number part is identified by an underscore followed by a number before the file extension.*/
	public static Sprite get(String name){
		if (exists(name))
			return sprites.get(name);
		else
			return null;
	}
	
	public static boolean exists(String name){
		return sprites.containsKey(name);
	}
	
	
	//Begin non-static part
	
	/**The texture array for this sprite. The frame count is the length of this array.*/
	public int[] textures;
	/**The origin offset for this sprite. 0,0 by default. This is added to the orientation given to the render method.*/
	public Point originOffset;
	/**The name, as used in the sprite library.*/
	String name;
	/**A mask associated with this Sprite*/
	public Mask mask;
	/**Width and height of the loaded image. May or may not match texture size.*/
	public int imageWidth,imageHeight;
	
	/**Create a new Sprite object from the given BufferedImage array. */
	Sprite(String name,int frameNumber, BufferedImage[] img){
		this.name=name;
		
		textures = new int[frameNumber];
		GL2 gl = Scene.gl;
		imageWidth = img[0].getWidth();
		imageHeight = img[0].getHeight();
		
		byte[] data = new byte[imageWidth*imageHeight*4];
		ByteBuffer buf = ByteBuffer.wrap(data);
		
		for(int i=0; i<frameNumber;i++){
			img[i].getRaster().getDataElements(0, 0, imageWidth,imageHeight,data);
			buf.rewind();
			
			int[] intBuffer = new int[1];
			gl.glGenTextures(1, intBuffer, 0);
			textures[i] = intBuffer[0];
			
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[i]);
			
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, imageWidth, imageHeight, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buf);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);//texcoord x clamp
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);//texcoord y clamp
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);//Shrinking
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);//Magnifying
			
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
			
		
		originOffset = new Point(0,0);
		mask = new Mask(this);
		sprites.put(name,this);
	}
	
	public void remove(){
		sprites.remove(name);
		Scene.gl.glDeleteTextures(textures.length, textures, 0);
		if (Scene.console)
			System.out.println("sprite " + name + " deallocated");
	}
	
	/**return horizontal and vertical scale based on given orientation*/
	public double[] getOrientationScale(int orientation){
		double ox,oy;
		if(orientation==CENTERED){
			ox=0.5;
			oy=0.5;
		}
		else{
			if (orientation<=1 || orientation==7)
				ox=1;
			else if (orientation>=3 && orientation<=5)
				ox=0;
			else
				ox=0.5;
			if (orientation>=1 && orientation<=3)
				oy=0;
			else if (orientation>=5)
				oy=1;
			else
				oy=0.5;
		}
		
		double[] out = new double[2];
		out[0] = ox; out[1] = oy;
		return out;
	}
	
	/**
	 * draw a more complex sprite
	 * @param subImage
	 * @param orientation
	 * @param x x-position of sprite
	 * @param y y-position of sprite
	 * @param xscale
	 * @param yscale
	 * @param angle
	 * @param alpha
	 * @param colR
	 * @param colG
	 * @param colB
	 */
	public void render(double subImage,int orientation,double x, double y, double xscale, double yscale, double angle, double alpha, double colR, double colG, double colB){
		GL2 gl = Scene.gl;
		gl.glEnable(GL2.GL_TEXTURE_2D);
		//textures[subImage].enable(gl);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[(int)subImage]);
		//textures[subImage].bind(gl);
		gl.glColor4d(colR,colG,colB,alpha);
		
		double a[] = getOrientationScale(orientation);
		double ox = a[0], oy = a[1];
		ox*=-imageWidth;
		oy*=-imageHeight;
		ox-=originOffset.x;
		oy-=originOffset.y;
		
		glRender(gl, x + Scene.viewX, y + Scene.viewY, ox, oy, xscale, yscale, angle, (int)subImage, colR, colG, colB, alpha);
	}
	/**
	 * draw a more complex sprite
	 * @param subImage
	 * @param orientation
	 * @param x x-position of sprite
	 * @param y y-position of sprite
	 * @param xscale
	 * @param yscale
	 * @param angle
	 * @param alpha
	 * @param color hex string
	 */
	public void render(double subImage,int orientation,double x, double y, double xscale, double yscale, double angle, double alpha, String hex){
		//convert the string to the color shit!
		int[] input=Calc.getColorHex(hex);
		render((int)subImage, orientation, x, y, xscale, yscale, angle, alpha, input[0]/255.0, input[1]/255.0, input[2]/255.0);
	}
	
	/**render a sprite without being affected by the view???? i dont know why this is a function lol*/
	public void renderWithoutView(double subImage,int orientation,double x, double y, double xscale, double yscale, double angle, double alpha, double colR, double colG, double colB){
		render((int)subImage, orientation, x - Scene.viewX, y - Scene.viewY, xscale, yscale, angle, alpha, colR, colG, colB);
	}
	
	/**render a sprite without being affected by the view???? i dont know why this is a function lol*/
	public void renderWithoutView(double subImage,int orientation,double x, double y, double xscale, double yscale, double angle, double alpha, String hex){
		int[] input=Calc.getColorHex(hex);
		render((int)subImage, orientation, x - Scene.viewX, y - Scene.viewY, xscale, yscale, angle, alpha, input[0]/255.0, input[1]/255.0, input[2]/255.0);
	}
	
	
	public void renderPart(double subImage,int orientation,double x, double y, double offsetX, double offsetY, double width, double height, double xscale, double yscale, double angle, double alpha, String hex){
		//convert the string to the color shit!
		int[] input=Calc.getColorHex(hex);
		renderPart(subImage, orientation, x, y, offsetX, offsetY, width, height, xscale, yscale, angle, alpha, input[0]/255.0, input[1]/255.0, input[2]/255.0);
	}
	
	/**
	 * draw a part of a sprite
	 * @param subImage
	 * @param orientation
	 * @param x x-position of sprite
	 * @param y y-position of sprite
	 * @param offsetX (for determining what part to draw)
	 * @param offsetY (for determining what part to draw)
	 * @param width (for determining what part to draw)
	 * @param height (for determining what part to draw)
	 * @param xscale
	 * @param yscale
	 * @param angle
	 * @param alpha
	 * @param colR 0-1
	 * @param colG 0-1
	 * @param colB 0-1
	 */
	public void renderPart(double subImage,int orientation,double x, double y, double offsetX, double offsetY, double width, double height, double xscale, double yscale, double angle, double alpha, double colR, double colG, double colB){
		double a[] = getOrientationScale(orientation);
		double ox = a[0], oy = a[1];
		ox*=-imageWidth;
		oy*=-imageHeight;
		ox-=originOffset.x;
		oy-=originOffset.y;
		
		GL2 gl = Scene.gl;
		initGlRender(gl, x + Scene.viewX, y + Scene.viewY, xscale, yscale, angle, (int)subImage, colR, colG, colB, alpha);
		
		gl.glTexCoord2d(offsetX/imageWidth,offsetY/imageHeight); //0,0
		gl.glVertex2d(ox+offsetX, oy+offsetY);
		gl.glTexCoord2d((offsetX+width)/imageWidth, offsetY/imageHeight); //1,0
		gl.glVertex2d(ox+offsetX+width, oy+offsetY);
		gl.glTexCoord2d(offsetX/imageWidth, (offsetY+height)/imageHeight);  //0,1
		gl.glVertex2d(ox+offsetX, oy+offsetY+height);
		gl.glTexCoord2d((width+offsetX)/imageWidth, (height+offsetY)/imageHeight); //1,1
		gl.glVertex2d(ox+offsetX+width, oy+offsetY+height);
		
		endGlRender(gl);
		
		Scene.trianglesDrawn += 2;
	}
	
	/**
	 * draw a simple sprite using only subimage and x and y coordinates
	 * @param subImage
	 * @param x
	 * @param y
	 */
	public void render(double subImage,double x, double y){
		render((int)subImage, NORTHWEST, x, y, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void initGlRender(GL2 gl, double x, double y, double xscale, double yscale, double angle, int subImage, double colR, double colG, double colB, double alpha){
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[subImage]);
		gl.glColor4d(colR,colG,colB,alpha);
		
		gl.glPushMatrix();
		gl.glTranslated(x, y, 0);
		gl.glRotated(-angle,0,0,1);
		gl.glScaled(xscale, yscale, 0);
		
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
	}
	
	public void glRender(GL2 gl, double x, double y, double ox, double oy, double xscale, double yscale, double angle, int subImage, double colR, double colG, double colB, double alpha){
		initGlRender(gl, x, y, xscale, yscale, angle, subImage, colR, colG, colB, alpha);

		gl.glTexCoord2d(0,0);
		gl.glVertex2d(ox, oy);
		gl.glTexCoord2d(1, 0);
		gl.glVertex2d(ox+imageWidth, oy);
		gl.glTexCoord2d(0, 1); 
		gl.glVertex2d(ox, oy+imageHeight);
		gl.glTexCoord2d(1, 1);
		gl.glVertex2d(ox+imageWidth, oy+imageHeight);
		
		endGlRender(gl);
		
		Scene.trianglesDrawn += 2;
	}
	
	public void endGlRender(GL2 gl){
		gl.glEnd();
		
		gl.glPopMatrix();
		
		gl.glColor4d(1,1,1,1);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}
}
