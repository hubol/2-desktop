package world.pet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.Calc;
import main.Main;

public class PetSprite {
	public int[][][] data;
	public int subimages, width, height;
	
	public PetSprite(String file){
		
		try {
			File f = new File(Main.DIRECTORY, "res/pet/"+file+".flx");
			FileReader r;
			r = new FileReader(f);
			BufferedReader p = new BufferedReader(r);
			
			subimages = Calc.parseInt(p.readLine());
			width = Calc.parseInt(p.readLine());
			height = Calc.parseInt(p.readLine());
			
			data = new int[subimages][width][height];
			
			for (int h=0; h<subimages; h++){
				for (int i=0; i<width; i++){
					for (int j=0; j<height; j++)
						data[h][i][j] = Calc.parseInt(p.readLine());
				}
			}
			
			p.close();
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(int image, double xx, double yy, PetShell shell){
		render(image, xx, yy, 1, 1, shell);
	}
	
	public void render(int image, double xx, double yy, int xsc, int ysc, PetShell shell){
		int x = (int)Math.round(xx), y = (int)Math.round(yy);
		
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				if (x + i < shell.width && y + j < shell.height && x + i >= 0 && y + j >= 0){
					int ii = i, jj = j, at;
					if (xsc == -1)
						ii = (width - 1) - i;
					if (ysc == -1)
						jj = (height - 1) - i;
					at = data[image][ii][jj];
					
					if (at == 1)
						shell.display[x + i][y + j] = false;
					else if (at == 2)
						shell.display[x + i][y + j] = true;
				}
			}
		}
	}

}
