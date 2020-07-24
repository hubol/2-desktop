package graphics;

import main.Calc;

public class Graphics {
	public static double[] color=new double[3];
	public static double alpha;
	public static String myColor;
	
	public Graphics(){
		color[0]=0;
		color[1]=0;
		color[2]=0;
		alpha=1;
		
		myColor = calcColor();
	}
	
	public static String calcColor(){
		return Calc.makeHexColor(color);
	}

	public static void setColor(String hex){
		int[] input;
		input=Calc.getColorHex(hex);
		
		for (int i=0; i<3; i++){
			color[i]=input[i]/255.0;
		}
		
		myColor = hex;
	}
	
	public static void setColor(double[] input){
		for (int i=0; i<3; i++){
			color[i]=input[i];
		}
		
		myColor = calcColor();
	}
	
	public static void setColor(int[] input){
		for (int i=0; i<3; i++){
			color[i]=input[i]/255.0;
		}
		
		myColor = calcColor();
	}
	
	public static void setColor(int[] input, boolean a){
		for (int i=0; i<3; i++){
			color[i]=input[i]/255.0;
		}
		
		myColor = calcColor();
	}
	
	public static void setColor(double r, double g, double b){
		color[0]=r;
		color[1]=g;
		color[2]=b;
		
		myColor = calcColor();
	}
	public static void setAlpha(double a){
		alpha=a;
	}
	
	public static String getColor(){
		return myColor;
	}
	
	public static double getAlpha(){
		return alpha;
	}
}
