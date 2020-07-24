package world.dream;

import audio.Audio;
import graphics.Graphics;
import graphics.Shape;
import graphics.Text;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.particles.JumpDreamPart;
import world.player.Player;
import main.Calc;
import main.Entity;

public class JumpControl extends Entity{
	public int count;
	public double jumpSpeed;
	public static JumpControl me;
	
	public boolean increase, fail;
	public String colorA, colorB;
	
	public double vol;
	public double thing;
	public int chimeAt, chimeTime, chimeSet;
	
	public boolean aha;

	public JumpControl(double x, double y) {
		super(0, 0);
		setDepth(Integer.MAX_VALUE-19);
		setCollisionGroup(Global.DEACTIVATEME);
		
		aha = true;
		
		count = 0;
		jumpSpeed = 0;
		
		increase = false;
		setColors();
		
		me = this;
		
		fail = false;
		
		thing = 0;
		
		alarmInitialize(3);
		alarm[0] = 30;
		
		vol = 1;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			increase = !increase;
			alarm[0] = Math.max(30 - (count / 2), 23);
			setColors();
			Sound.playPitched("sFjaak"+Calc.boolToInt(increase), .04);
			fail = false;
		}
	}
	
	public void step(){
		thing += .05;
		thing *= 1.1;
		if (thing >= 1)
			thing -= 1;
		
		chimeTime -= 1;
		if (chimeTime == 0 && chimeAt < count / 2){
			chimeAt += 1;
			chimePlay();
		}
		
		if (Player.me.vspeed < 0 && jumpSpeed != 0){
			String col = colorA;
			if (aha)
				col = colorB;
			
			new JumpDreamPart(Player.me.x + 8, Player.me.y + 8, col);
			new JumpDreamPart(Player.me.x + 8, Player.me.y - 8, col);
			new JumpDreamPart(Player.me.x - 8, Player.me.y + 8, col);
			new JumpDreamPart(Player.me.x - 8, Player.me.y - 8, col);
			aha = !aha;
		}
		
		alarmStep();
	}
	
	public void setColors(){
		final String colpreA = colorA, colpreB = colorB;
		final String[] choices = new String[9];
		final int s = choices.length;
		choices[0] = "A277FF";
		choices[1] = "54FF9E";
		choices[2] = "FF3911";
		choices[3] = "FFFB23";
		choices[4] = "82FF3A";
		choices[5] = "FF5174";
		choices[6] = "FF2B47";
		choices[7] = "BD5BFF";
		choices[8] = "23FF53";
		
		colorA = choices[(int)Calc.random(s)];
		while (colorA.equals(colpreA)||colorA.equals(colpreB))
			colorA = choices[(int)Calc.random(s)];
		
		colorB = choices[(int)Calc.random(s)];
		while (colorB.equals(colorA) || colorB.equals(colpreB) || colorB.equals(colpreA))
			colorB = choices[(int)Calc.random(s)];
	}
	
	public void chimePlay(){
		Audio.get("sDreamJumpChime").setGain(vol);
		Audio.fade("sDreamJumpChime", vol, 1.0);
		Sound.playPitched("sDreamJumpChime", (.4) * (Math.min(1, (double)chimeAt / (double)10)));
		chimeSet = Math.max(4, chimeSet - 1);
		chimeTime = chimeSet;
		vol = Calc.approach(vol, 0, 8);
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.roomDestroy();
	}
	
	/**this is what happens when the player jumps!!!! wowsers!!!*/
	public void playerJump(){
		if (increase){ //the player has jumped at the correct time
			if (jumpSpeed < 20)
			 jumpSpeed += .85;
			count += 1;
			
			chimeSet = 10;
			chimeAt = 0;
			vol = 1;
			chimePlay();
		}
		else{ //the player is a moron
			fail = true;
			jumpSpeed = 0;
			Fg.me.shakeTimer = 10;
			count = 0;
			setColors();
			Sound.play("sDishError");
		}
	}
	
	public void render(){
		Graphics.setColor(colorA);
		Shape.drawRectangle(0, 0, 640, 480);
		
		Text.setFont(Global.FONT);
		Text.orientation = Text.NORTHWEST;
		Text.randomize(.01 + (thing / 50.0));
		
		final boolean n = false;
		
		String txt = "STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP%STOPSTOPSTOPSTOPSTOP";
		if (/*count < 5 && */increase)
			txt = "JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP%JUMPJUMPJUMPJUMPJUMP";
		if (fail)
			txt = "BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA%BAKABAKABAKABAKABAKA";
		
		if (n){
			txt = "STOP";
			if (increase)
				txt = "JUMP";
			if (fail)
				txt = "BAKA";
		}
		
		Graphics.setColor("0095CF");
		Graphics.setAlpha(.75);
		if (!n)
			Text.drawText(3, 3, txt);
		else
			Text.drawTextExt(8, 8, txt, 6, 16, 0);
		Graphics.setAlpha(1);
		Graphics.setColor(colorB);
		if (!n)
			Text.drawText(0, 0, txt);
		else
			Text.drawTextExt(0, 0, txt, 6, 16, 0);
		Text.randomize(0);
	}

}
