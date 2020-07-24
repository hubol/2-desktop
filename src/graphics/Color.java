package graphics;

import main.Calc;

public class Color{
	public int[] elements;
	
	/**create a new color from a hex string!! color elements are 0 - 255 ints*/
	public Color(String s){
		elements = Calc.getColorHex(s);
	}
	
	/**create a new color from 3 ints (0 - 255)!! color elements are 0 - 255 ints*/
	public Color(int... s){
		elements = new int[3];
		for (int i=0; i<3; i++)
			elements[i] = s[i];
	}
	
	/**return hex string of this color's elements*/
	public String getHex(){
		return Calc.makeHexColorInt(elements);
	}
	
	/**return double array (0 - 1)*/
	public double[] getDouble(){
		return new double[]{(elements[0]/255.0),(elements[1]/255.0),(elements[2]/255.0)};
	}
	
	/**return int array (0 - 255)*/
	public int[] getInt(){
		return elements;
	}
}
