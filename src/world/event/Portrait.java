package world.event;

import javax.media.opengl.GL2;

import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import world.control.EV;
import world.control.Global;
import world.control.Hud;
import world.control.Shake;
import world.control.SpriteLoader;
import world.player.Player;
import main.Entity;
import main.Scene;

public class Portrait extends Entity{
	private int price;
	private int event;
	private int id;
	private double display;
	
	private Shake[] s = new Shake[4];
	private final String[] names = new String[]{"DRIFTER", "J-BOY", "SLICK", "BANAAN", "PUKE", "DUKE", "PAPA"};
	private final String[] color = new String[]{"E2FF18", "FF8B10", "F8FF20", "FF7787", "9FFFCE", "FAFF58", "00C77D"};
	
	private Font font;

	public Portrait(double x, double y, int id) {
		super(x, y);
		
		setDepth(20);
		setCollisionGroup(Global.DEACTIVATEME);
		
		if (Global.bossesKilled() < Global.totalBosses)
			destroy();
		else{
			new SpriteLoader("menufont_49", "sPortrait_7", "sPortraitHeader", "sPortraitOutline");
			font = new Font("menufont", true, '-', '<');
			
			this.id = id;
			event = id + EV.PDRIFTER;
			price = 100 + (50 * id);
			
			for (int i=0; i<4; i++)
				s[i] = new Shake(.2);
			calcDisplay();
		}
	}
	
	public void step(){
		if (inBounds(Player.me.x - 15, Player.me.y - 16) || inBounds(Player.me.x - 15, Player.me.y + 16) || inBounds(Player.me.x + 15, Player.me.y + 16) || inBounds(Player.me.x + 15, Player.me.y - 16)){
			int repeat = 1 + (int)((price - 100) / 100);
			int done = 0;
			for (int i=0; i<repeat; i++){
				if (Global.money > 0 && Global.event[event] < price){
					Global.money--;
					Global.event[event]++;
					done++;
					calcDisplay();
				}
			}
			if (done > 0)
				Hud.me.currentMoney -= done - 1;
		}
	}
	
	public boolean inBounds(double x, double y){
		return (x >= this.x && y >= this.y && x <= this.x + 64 && y <= this.y + 96);
	}
	
	public void calcDisplay(){
		display = (double)Global.event[event] / (double)price;
	}
	
	public void render(){
		String text = names[id];
		if (Global.event[event] < price)
			text = ""+(price - Global.event[event]);
		
		Graphics.setColor("FFFFFF");
		
		Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
		
		Scene.gl.glClearStencil(0);
		Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
		Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
		
		Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
		
		Graphics.setAlpha(1);
		Shape.drawRectangle(x + s[1].x, y + 96 + s[1].y - (display * 96), x + s[1].x + 64, y + 96 + s[1].y);
		
		Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
		 
		Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
		
		Sprite.get("sPortrait").render(id, Sprite.NORTHWEST, x + s[1].x, y + s[1].y, .5, .5, 0, 1, 1, 1, 1);
		
		Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		
		double xx = 0, yy = 0;
		if (id == 3){
			xx = 96;
			yy = 32;
		}
		
		Sprite.get("sPortraitHeader").render(0, Sprite.CENTERED, x + 29 + s[2].x + xx, y - 24 + s[2].y + yy, 1, 1, 0, 1, 1, 1, 1);
		Text.idiot = false;
		Text.setFont(font);
		Text.orientation = Text.CENTERED;
		Graphics.setColor(color[id]);
		final double ss = 0.26785714285714285714285714285714;
		Text.drawTextExt(x + 30 + s[3].x + xx, y - 26 + s[3].y + yy, text, ss, ss * 1.8, 0);
		Text.idiot = true;
		
		Sprite.get("sPortraitOutline").render(0, Sprite.NORTHWEST, x + s[0].x - 4, y + s[0].y - 4, .5, .5, 0, 1, 1, 1, 1);
	}

}
