package world.boss.skull;

import java.util.ArrayList;

import world.control.Shake;

import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;

public class SkullShadowRender extends Entity{
	public Skull mom;
	private ArrayList<Shake> s = new ArrayList<Shake>();

	public SkullShadowRender(Skull mom) {
		super(0, 0);
		this.mom = mom;
		for (int i=0; i<8; i++)
			s.add(new Shake(.3));
		
		setDepth(50);
	}
	
	public void step(){
		
	}
	
	public void render(){
		Graphics.setColor("E2FF18");
		Graphics.setAlpha(1);
		Text.idiot = false;
		Text.setFont(mom.font);
		Text.orientation = Text.CENTERED;
		
		double yy = Calc.dirY(mom.phsADist, mom.phsADir);
		double scale = mom.smult * .65;
		for (int i=0; i<mom.shadows.size(); i++){
			SkullShadow s = mom.shadows.get(i);
			mom.shadow(s.x + this.s.get(i).x, s.y + this.s.get(i).y);
			Text.drawTextExt(s.x + this.s.get(i).x, s.y + this.s.get(i).y + yy, ""+(s.id + 1), scale, scale, 0);
		}
		
		Text.idiot = true;
	}

}
