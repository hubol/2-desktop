package world.particles;

import world.control.Global;
import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;

public class FloatText extends Entity{
	public String text;
	public double spd;

	public FloatText(double x, double y) {
		super(x, y);
		setColor(Calc.getColorHSV((int)Calc.random(256), 255, 255));
		
		setDepth(40);
		setCollisionGroup(Global.DEACTIVATEME);
		
		int a= (int)Calc.random(10);
		if (a==0)
			text=(String)Calc.choose("burger","worry","worries","nope","stop","give","up","too","late","already","dead","death","one","fifteen");
		else if (a==1)
			text=(String)Calc.choose("hemma","mogen","wowsers","hello","goodbye","help","delicious","monkey","cheese","shiba","piss","dong");
		else if (a==2)
			text=(String)Calc.choose("cat","planet","lots","do","on","cupcakes","dog","cat","dodge","laxative","coffee","careful","wisen");
		else if (a==3)
			text=(String)Calc.choose("fluttershy","sha","oddwarg","hubol","shake","shiver","helix","hi","my","name","is","breathe","fire");
		else if (a==4)
			text=(String)Calc.choose("furry","feminism","rocks","destroy","food","company","where","the","hell","ice","moose","gay","deer");
		else if (a==5)
			text=(String)Calc.choose("blender","in","a","sunshine","bag","face","keep","quiet","look","eye","dance","get","up","and","smile");
		else if (a==6)
			text=(String)Calc.choose("furries","rawr","roar","see","you","again","sit","still","i","really","like","daughter","son","mother");
		else if (a==7)
			text=(String)Calc.choose("father","uncle","aunt","cousin","nephew","niece","peace","piece","life","not","only","someday","soon");
		else if (a==8)
			text=(String)Calc.choose("chocolate","cream","whipped","ouch","whoops","oops","iceberg","lettuce","belong","ownership","war","crime");
		else if (a==9)
			text=(String)Calc.choose("insanity","sanity","crazed","lunatic","escapee","canonical","comical","collaborate","fart","belch","grumble");
		
		text = text.toUpperCase();
		angle=Calc.random(360);
		spd=-5+Calc.random(10);
	}
	
	public void step(){
		angle+=spd;
	}
	
	public void render(){
		Text.orientation=Text.CENTERED;
		Graphics.setColor(colR,colG,colB);
		Graphics.setAlpha(.6+Calc.random(.4));
		Text.randomize(Calc.random(2));
		Text.setFont("systemFont");
		Text.drawTextExt(x, y, text, .98+Calc.random(.03), .98+Calc.random(.03), angle);
		Text.randomize(0);
		Graphics.setAlpha(1);
	}

}
