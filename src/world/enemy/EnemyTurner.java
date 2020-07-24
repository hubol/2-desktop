package world.enemy;

import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class EnemyTurner extends Entity {

	public EnemyTurner(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setCollisionGroup(Global.TURNER);
		visible=false;
		sprite=Global.sBLOCK;
		mask=sprite.mask;
	}

}
