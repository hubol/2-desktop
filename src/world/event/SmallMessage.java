package world.event;

import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import world.control.Global;
import main.Calc;
import main.Entity;

public class SmallMessage extends Entity{
	public double xOffset, yOffset, length;
	public Entity mother;
	public String text;
	
	public int phase;

	public SmallMessage(Entity mom, double xOffset, double yOffset, String text, double displayTime) {
		super(mom.x + xOffset + 4, mom.y + yOffset);
		mother = mom;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.text = Text.widthLimit(text, 12).toUpperCase();
		this.length = displayTime;
		
		phase = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE+10);
		
		sprite = Sprite.get("sSmallBubble");
		imageSingle = 0;
		imageSpeed = 1.0/6.0;
		
		xscale = 0;
		yscale = 0;
		
		orientation = Sprite.SOUTH;
		//TODO in sound
	}
	
	public void step(){
		x = mother.x + xOffset + Calc.rangedRandom(.2) + 4;
		y = mother.y + yOffset + Calc.rangedRandom(.2);
		
		if (phase == 0){
			xscale += .175;
			xscale *= 1.1;
			if (xscale >= 1){
				xscale = 1.0;
				phase = 1;
			}
			yscale = xscale;
		}
		else if (phase == 1){
			length -= 1;
			if (length <= 0){
				phase = 2;
				//TODO out sound
			}
		}
		else if (phase == 2){
			xscale -= .175;
			xscale *= .9;
			yscale = xscale;
			if (xscale <= 0)
				destroy();
		}
		
		super.step();
	}
	
	public void render(){
		x = mother.x + xOffset + Calc.rangedRandom(.2) + 4;
		y = mother.y + yOffset + Calc.rangedRandom(.2);
		
		super.render();
		Text.setFont(Global.FONT);
		Text.orientation = Text.CENTERED;
		Graphics.setColor("189EFF");
		Graphics.setAlpha(1.0);
		Text.randomize(.02);
		Text.drawTextExt(x + 3, y - (45 * yscale), text, .3 * xscale, .3 * yscale, 0);
		Text.randomize(0);
	}

}
