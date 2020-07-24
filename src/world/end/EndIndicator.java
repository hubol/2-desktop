package world.end;

import java.text.DecimalFormat;

import javax.media.opengl.GL2;

import audio.Audio;

import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.player.Player;
import graphics.Font;
import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Scene;

public class EndIndicator extends Entity{
	private String totalTime, playTime, pauseTime;
	
	private final double max = Global.getMaxProgress();
	private int display = 0;
	
	private int hearts = 0, chests = 0, bosses = 0;
	
	private final int SHAKE = 5;
	private EndShake[] shakes = new EndShake[SHAKE];
	private Shake[] s = new Shake[SHAKE];
	
	private double[] progScale = new double[3];
	
	private int show = 0;
	private final Font font = new Font("menufont", true, '-', '<');
	private final String[] items = new String[]{"HEARTS", "CHESTS", "BOSSES"};

	public EndIndicator(long frames, long played, long paused) {
		super(0, 0);
		totalTime = time(frames);
		playTime = time(played);
		pauseTime = time(paused);
		
		for (int i=0; i<SHAKE; i++){
			shakes[i] = new EndShake();
			s[i] = new Shake(.4);
		}
		
		display = 0;
		
		for (int i=0; i<3; i++)
			progScale[i] = 1;
		
		alarmInitialize(8);
		alarm[0] = 60;
	}
	
	public String time(long t){
		DecimalFormat f = new DecimalFormat();
		f.setMaximumIntegerDigits(0);
		f.setMinimumFractionDigits(2);
		f.setMaximumFractionDigits(2);
		
		return Calc.formatTime((int)(t / 30)) + "" + f.format((double)((t % 30.0) / 30.0));
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			progress(1);
			show = 1;
			shakes[0].shake = 4;
			alarm[1] = 10;
			alarm[2] = 15;
		}
		else if (i == 1)
			show++;
		else if (i == 2){
			if (hearts < Global.heartsGot()){
				hearts++;
				progress(1 + (display / max));
				shakes[0].shake = 4;
				shakes[1].shake = 4;
				alarm[2] = 3;
			}
			
			if (hearts >= Global.heartsGot()){
				alarm[2] = -1;
				alarm[1] = 10;
				alarm[3] = 15;
			}
		}
		else if (i == 3){
			if (chests < Global.chestsOpened()){
				chests++;
				progress(1 + (display / max));
				shakes[0].shake = 4;
				shakes[2].shake = 4;
				alarm[3] = 3;
			}
			
			if (chests >= Global.chestsOpened()){
				alarm[1] = 10;
				alarm[3] = -1;
				alarm[4] = 15;
			}
		}
		else if (i == 4){
			if (bosses < Global.bossesKilled()){
				bosses++;
				progress(1 + (display / max));
				display = hearts + chests + bosses;
				if (display == max){
					final double vol = .5;
					Audio.fade("sEndHorn", vol, vol);
					Audio.get("sEndHorn").setGain(vol);
					Sound.play("sEndHorn");
				}
				shakes[0].shake = 4;
				shakes[3].shake = 4;
				alarm[4] = 3;
			}
			
			if (bosses >= Global.bossesKilled()){
				alarm[4] = -1;
				alarm[1] = 10;
				alarm[5] = 60;
			}
		}
		else if (i == 5){
			alarmEvent(1);
			if (show <= 8){
				shakes[show - 5].shake = 4;
				Sound.playPitched("sEndAdvance", .05);
			}
			if (show < 9)
				alarm[5] = 10;
			else
				alarm[6] = 120;
		}
		else if (i == 6){
			visible = false;
			Sound.playPitched("sEndAdvance", .05);
			alarm[7] = 30;
		}
		else if (i == 7){
			destroy();
			new Credits();
		}
	}
	
	public void progress(double pitch){
		Audio.get("sEndProgress").setPitch(pitch);
		Sound.play("sEndProgress");
	}
	
	public void step(){
		if (Player.me.endAlpha > 0)
			Player.me.endAlpha -= .02;
		
		double aprch = 1.25;
		if (show >= 5)
			aprch = .75;
		for (int i=0; i<3; i++){
			if (show == i + 2 || show == i + 6)
				progScale[i] = Calc.approach(progScale[i], aprch, 2);
			else
				progScale[i] = Calc.approach(progScale[i], .7, 2);
		}
		
		alarmStep();
		display = hearts + chests + bosses;
	}
	
	public void render(){
		String text = ""+(int)(((double)display / max) * 100)+"<";
		final double txtScale = 2;
		
		final double addY = -80;
		
		Text.idiot = false;
		Text.setFont(font);
		if (show > 0){
			Graphics.setColor("FFFFFF");
			drawPieChart(320 + shakes[0].x + s[0].x, 240 + shakes[0].y + s[0].y + addY, 128, display, 0);
			
			Text.drawTextExt(320 + shakes[0].x + s[1].x, 240 + shakes[0].y + s[1].y + addY, text, txtScale * .8, txtScale, 0);
			
			Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
			
			Scene.gl.glClearStencil(0);
			Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
			Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
			
			Graphics.setAlpha(1.0 / 256.0);
			drawPieChart(320 + shakes[0].x + s[0].x, 240 + shakes[0].y + s[0].y + addY, 128, display, 0);
			Graphics.setAlpha(1);
			
			Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
			 
			Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
			
			Graphics.setColor("0081CF");
			Text.drawTextExt(320 + shakes[0].x + s[1].x, 240 + shakes[0].y + s[1].y + addY, text, txtScale * .8, txtScale, 0);
			
			Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		}
		
		Graphics.setColor("FFFFFF");
		for (int i=0; i<3; i++){
			int at = hearts, max = Global.heartAmount;
			if (i == 1){
				at = chests;
				max = Global.totalChests;
			}
			if (i == 2){
				at = bosses;
				max = Global.totalBosses;
			}
			
			String txt = items[i]+": "+at+" OF "+max;
			if (show > i + 5){
				if (i == 0)
					txt = "TIME PLAYED: "+playTime;
				else if (i == 1)
					txt = "TIME PAUSED: "+pauseTime;
				else
					txt = "TOTAL TIME: "+totalTime;
			}
			
			if (show > i + 1)
				Text.drawTextExt(320 + s[2 + i].x + shakes[i + 1].x, 345 + s[2 + i].y + shakes[i + 1].y + (i * 45), txt, .9 * progScale[i], .9 * progScale[i], 0);
		}
		Text.idiot = true;
	}
	
	public void drawPieChart(double x, double y, double radius, double displayed, double angle){
		final int steps = 80;
		final double percent = displayed / max;
		
		double[] stored = new double[3];
		stored = Graphics.color;
		
		Graphics.setColor(1, 1, 1);
		Scene.gl.glColor4d(Graphics.color[0], Graphics.color[1], Graphics.color[2], Graphics.alpha);
		Scene.gl.glBegin(GL2.GL_TRIANGLE_FAN);
		Scene.gl.glVertex2d(x,y);
		int tot = (int)Math.max(6, Math.ceil(steps * percent));
		for(int a = 0; a<=tot; a++){
			Scene.gl.glVertex2d(x+radius*Math.cos(Math.PI * angle),y+radius*Math.sin(Math.PI * angle));
			if (a < tot)
				angle += ((1.0 / (double)tot) * percent) * 2.0;
			Scene.trianglesDrawn += 1;
		}
		Scene.gl.glEnd();
		
		Graphics.setColor(stored);
	}

}
