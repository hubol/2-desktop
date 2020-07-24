package world.control;

import main.Calc;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;

public class Map {
	public static final double w=15, h=15, hspace=1, vspace=1, r=.505;
	public final static Sprite ICONS = Sprite.get("sMapIcons");
	
	public static void drawMap(double x, double y){
		drawMap(x, y, 1, Global.gotMapDoor, Global.gotMapIcons);
	}
	
	public static void drawMap(double x, double y, double a, boolean doors, boolean icons){
		//block
		Graphics.setAlpha(a);
		for (int i=0; i<16; i++){
			for (int j=0; j<8; j++){
				String c = Global.getHexMap(i, j);
				boolean notBlack = (!c.equals("000000"));
				
				if (notBlack){
					Graphics.setAlpha(a);
				}
				else{
					c = "0095EF";
					Graphics.setAlpha(a * .4);
				}
				Graphics.setColor(c);
				Shape.drawRectangle(x + (i * w) +hspace + Calc.rangedRandom(r), y + (j * h) +vspace + Calc.rangedRandom(r), x + ((i + 1) * w) + Calc.rangedRandom(r), y + ((j + 1) * h) + Calc.rangedRandom(r));
			}
		}
		
		//draw door connections if you have the ability to do so
		if (doors && !Global.dream){
			Graphics.setAlpha(a);
			for (int i=0; i<Global.mapDoorAmount; i++){
				if (Global.mapDoorX[i]!= -1){
					double xx, yy, aa, bb;
					xx = Calc.rangedRandom(r);
					yy = Calc.rangedRandom(r);
					aa = Calc.rangedRandom(r);
					bb = Calc.rangedRandom(r);
					
					double jesus, dick, dir;
					dir = Calc.pointDirection(x + ((Global.mapDoorX[i] + .5) * w) + hspace + xx, y + ((Global.mapDoorY[i] + .5) * h) + vspace + yy, x + ((Global.mapDoorToX[i] + .5) * w) + hspace + aa, y + ((Global.mapDoorToY[i] + .5) * h) + vspace + bb);
					jesus = Calc.dirX(1, dir + 180);
					dick = Calc.dirY(1, dir + 180);
					
					Graphics.setColor("FFFFFF");
					Shape.drawLine(x + ((Global.mapDoorX[i] + .5) * w) + hspace + xx + jesus, y + ((Global.mapDoorY[i] + .5) * h) + vspace + yy + dick, x + ((Global.mapDoorToX[i] + .5) * w) + hspace + aa - jesus, y + ((Global.mapDoorToY[i] + .5) * h) + vspace + bb - dick, 3);
					Graphics.setColor("20A1FF");
					Shape.drawLine(x + ((Global.mapDoorX[i] + .5) * w) + hspace + xx, y + ((Global.mapDoorY[i] + .5) * h) + vspace + yy, x + ((Global.mapDoorToX[i] + .5) * w) + hspace + aa, y + ((Global.mapDoorToY[i] + .5) * h) + vspace + bb, 1);
				}
			}
		}
		
		//line
		Graphics.setAlpha(a);
		for (int i=0; i<16; i++){
			for (int j=0; j<8; j++){
				String c = Global.getHexMap(i, j);
				boolean notBlack = (!c.equals("000000"));

				if (notBlack){
					//draw icons if you have the ability to do so
					int p = 0;
					if (Global.roomX == i && Global.roomY == j && !Global.dream)
						p = 1;
					else if (icons)
						p = Global.getIconMap(i, j);
					if (p != 0)
						ICONS.render(p, Sprite.CENTERED, x + ((i + .5) * w) + hspace + Calc.rangedRandom(r), y + ((j + .5) * h) + vspace + Calc.rangedRandom(r), 1, 1, 0, a, 1, 1, 1);
				}
			}
		}
		
		Graphics.setAlpha(1);
	}

}
