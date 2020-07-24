package world.end;

import graphics.Sprite;
import main.Entity;

public class CreditsPattern extends Entity{
	private double spd;
	
	private int isDot = 0;
	private int img = 0;
	
	private boolean left = true;
	private double addX = 0;
	private final double ADD = 80;

	public CreditsPattern(double spd) {
		super(0, 0);
		this.spd = spd;
		alarmInitialize(1);
		alarmEvent(0);
	}
	
	public void alarmEvent(int i){
		int j = img % 3;
		boolean fuck = false;
		Sprite s = Sprite.get("sCreditsFlower");
		if (isDot % 5 < 4){
			j = 0;
			s = Sprite.get("sCreditsDot");
			fuck = true;
		}
		
		new CreditsFlower(480 + addX, 512, s, j, -spd);
		
		if (!fuck)
			img++;
		isDot++;
		
		if (left)
			addX -= ADD;
		if (addX < -ADD){
			left = false;
			addX = -ADD;
		}
		if (!left)
			addX += ADD;
		if (addX > ADD){
			left = true;
			addX = 0;
		}
		alarm[0] = 60;
	}

}
