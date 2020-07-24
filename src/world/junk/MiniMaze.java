package world.junk;

public class MiniMaze{
	public int[][] map = new int[20][20];
	public int x, y;
	public boolean win = false;
	
	//2 - > block, 3 - > win, 0 - > nothingness, 1 - > player
	
	public MiniMaze(int x, int y, int[][] map){
		this.map = map;
		this.x = x;
		this.y = y;
	}

	public boolean move(int h, int v){
		if (!win){
			int x = this.x, y = this.y, iX = this.x, iY = this.y;
			
			int oldX = x, oldY = y;
			
			x = this.x + h;
			y = this.y + v;
			while(map[x][y] != 2 && map[x][y] != 3 && x >= 0 && x < 20){
				oldX = x;
				oldY = y;
				
				x += h;
				y += v;
			}
			
			if (map[x][y] < 3){
				x = oldX;
				y = oldY;
			}
			
			this.x = (int)Math.max(0, Math.min(19, x));
			this.y = (int)Math.max(0, Math.min(19, y));
			
			win = (map[x][y] == 3);
			
			return (this.x != iX || this.y != iY);
		}
		
		return false;
	}
	
	public boolean left(){
		return move(-1, 0);
	}
	
	public boolean right(){
		return move(1, 0);
	}
	
	public boolean up(){
		return move(0, -1);
	}
	
	public boolean down(){
		return move(0, 1);
	}
	
	public int[][] data(){
		int[][] out = new int[20][20];
		for (int i=0; i<20; i++){
			for (int j=0; j<20; j++)
				out[i][j] = map[i][j];
		}
		out[x][y] = 1;
		return out;
	}

}
