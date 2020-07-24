package world.boss.slick;

import java.util.ArrayList;

import main.Entity;

public class SlickAnimation extends Entity{
	public ArrayList<Integer[]> data;
	public int size;

	public SlickAnimation(Integer[]... data) {
		super(0, 0);
		
		size = data[0].length;
		
		this.data = new ArrayList<Integer[]>();
		for (int i=0; i<data.length; i++)
			this.data.add(data[i]);
	}
	
	/**returns the specified animation array*/
	public Integer[] get(int i){
		return data.get(i);
	}
	
	/**returns a single entry of the specified (i) animation array*/
	public double get(int i, int a){
		return data.get(i)[a];
	}

}
