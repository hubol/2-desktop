package graphics;

import java.awt.image.BufferedImage;
/**Temporary storage for BufferedImages before they are uploaded to OpenGL.
 * This is used as a workaround for the fact that Swing components(the loading screen)
 * do not repaint while OpenGL is busy.
 * 
 * Images are downloaded into memory from the server or hard drive before being uploaded into OpenGL.*/
public class BufferedImageContainer {
	public String name;
	public BufferedImage[] images;
	public int frameNumber;
	
	public Sprite generateSprite(){
		return new Sprite(name, frameNumber, images);
	}
	
}
