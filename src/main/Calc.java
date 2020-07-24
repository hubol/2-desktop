package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import java.awt.event.KeyEvent;

public class Calc {
	public final static double toRad = Math.PI/180;
	public final static double toDeg = 180/Math.PI;
	
	public static double[] transformPointByAngle(double x, double y, double angle){
		/*double h = Math.cos(angle);
		double v = Math.sin(angle);
		
		double[] output = new double[2];
		output[0] = x*h - y*v;
		output[1] = y*h + x*v;
		return output;*/
		//return new double[]{x*Math.cos(angle) + y*(-Math.sin(angle)), x*Math.sin(angle) + y*Math.cos(angle))};
		//return new double[] {x*Math.cos(angle) - y*Math.sin(angle),  x*Math.sin(angle) + y*Math.cos(angle)};
		return new double[]{Math.cos(angle * toRad) * (x) + Math.sin(angle * toRad) * (y), Math.sin(angle * toRad) * (x) + Math.cos(angle * toRad) * (y)};
	}
	
	public static int boolToInt(boolean a){
		if (a)
			return 1;
		return 0;
	}
	
	/**do not talk to me about this method*/
	public static String stupidStringToHex(String s){
		final double div = 96;
		int length = (int)(s.length() / 3);
		return makeHexColor((addString(s.substring(0, length - 1)) % div) / div, (addString(s.substring(length, (2 * length) - 1)) % div) / div, (addString(s.substring((2 * length) - 1, s.length() - 1)) % div) / div);
	}
	
	/**add up the characters of a string for some reason*/
	public static int addString(String s){
		int a = 0;
		for (int i=0; i<s.length(); i++)
			a += (int)s.charAt(i);
		return a;
	}
	
	public static double pointDirection(double x1,double y1,double x2,double y2){
		return toDeg*Math.atan2(y1-y2, x2-x1);
	}
	
	public static double dirX(double len, double dir){
		return len*Math.cos(toRad*dir);
	}
	public static double dirY(double len, double dir){
		return -len*Math.sin(toRad*dir);
	}
	public static double pointDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(y1-y2, 2)+Math.pow(x1-x2, 2));
	}
	public static double pointDistance(double x1, double y1, double z1, double x2, double y2, double z2){
		return Math.sqrt(Math.pow(z1-z2, 2)+Math.pow(y1-y2, 2)+Math.pow(x1-x2, 2));
	}
	
	public static double approach(double current, double wanted, double divide){
		return current+((wanted-current)/divide);
	}
	
	/**this function is stupid. if x == 0, 1 is returned. else, 0 is returned.*/
	public static double invert(double x){
		if (x == 0)
			return 1;
		return 0;
	}
	
	/**this function is also stupid. if x == 0, 1 is returned. else, 0 is returned.*/
	public static int invert(int x){
		if (x == 0)
			return 1;
		return 0;
	}
	
	/**approach an angle properly!!!*/
	public static double angleApproach(double current, double wanted, double divide){
		//if (dirY(1, current - wanted) > 0)
			return approach(current, wanted, divide);
		/*else
			return approach(current, wanted - 360, divide);*/
	}
	/**returns true if at least one entry in a boolean array is true*/
	public static boolean oneIsTrue(boolean... a){
		for (int i=0; i<a.length; i++){
			if (a[i])
				return true;
		}
		return false;
	}
	
	/**return a random number from 0 - input*/
	public static double random(double input){
		return Math.random()*input;
	}
	/**return a random number in between a - b (note: this function is practically useless)*/
	public static double random(double a, double b){
		double min = b, max = a;
		if (a < b){
			min = a;
			max = b;
		}
		return min + random(max - min);
	}
	public static int fRandom(double input){
		return (int)Math.floor(Math.random()*input);
	}
	public static Object choose(Object... entries){
		int i = (int)(Math.random()*entries.length);
		return entries[i];
	}
	public static double keepInBounds(double i, double min, double max){
		return (Math.min(max, Math.max(min, i)));
	}
	public static double loopBounds(double i, double min, double max){
		double o = i;
		while (o < min)
			o += max - min;
		while (o >= max)
			o += min - max;
		return o;
	}
	/**convert a double array to a single string with a string "div" dividing each entry*/
	public static String arrayToString(double[] a, String div){
		String output = "";
		for(int i=0; i<a.length; i++){
			output=output+""+a[i];
			if (i<a.length-1)
				output=output+div;
		}
		return output;
	}
	/**convert an integer array to a single string with a string "div" dividing each entry*/
	public static String arrayToString(int[] a, String div){
		String output = "";
		for(int i=0; i<a.length; i++){
			output=output+""+a[i];
			if (i<a.length-1)
				output=output+div;
		}
		return output;
	}
	/**convert a string array to a single string with a string "div" dividing each entry*/
	public static String arrayToString(String[] a, String div){
		String output = "";
		for(int i=0; i<a.length; i++){
			if (a[i].equals(""))
				a[i] = "<-_->";
			output=output+a[i];
			if (i<a.length-1)
				output=output+div;
		}
		//what the fuck is this a java bug????????
		for(int i=0; i<a.length; i++){
			if (a[i].equals("<-_->"))
				a[i] = "";
		}
		return output;
	}
	/**convert a boolean array to a single string (looks like binary!)*/
	public static String arrayToString(boolean[] a){
		String output = "";
		for(int i=0; i<a.length; i++){
			if (a[i])
				output=output+"1";
			else
				output=output+"0";
		}
		return output;
	}
	/**convert a string to a double array with a string "div" dividing each entry*/
	public static double[] stringToDoubleArray(String s, String div){
		String[] ugh = s.split(div);
		double[] output = new double[ugh.length];
		for (int i=0; i<ugh.length; i++){
			output[i]=parseDouble(ugh[i]);
		}
		return output;
	}
	/**convert a string to an integer array with a string "div" dividing each entry*/
	public static int[] stringToIntArray(String s, String div){
		String[] ugh = s.split(div);
		int[] output = new int[ugh.length];
		for (int i=0; i<ugh.length; i++){
			output[i]=parseInt(ugh[i]);
		}
		return output;
	}
	/**convert a string comprised of 0s and 1s (binary!) to a boolean array!*/
	public static boolean[] stringToBooleanArray(String s){
		boolean[] output = new boolean[s.length()];
		for (int i=0; i<s.length(); i++){
			if (s.charAt(i) == '0')
				output[i] = false;
			else
				output[i] = true;
		}
		return output;
	}
	/**convert a string to a string array with a string "div" dividing each entry*/
	public static String[] stringToStringArray(String s, String div){
		String[] ugh = s.split(div);
		for (int i=0; i<ugh.length; i++){
			//System.out.println(ugh[i]);
			if (ugh[i].equals("<-_->")){
				ugh[i] = "";
				//System.out.println("fix! "+ugh[i]);
			}
		}
		return ugh;
	}
	//THIS WILL BE HELPFUL FOR LOADING FILES YOU FUCK: (CONV STRING TO INT)
	public static Integer parseInt(String s){
		try{
			return Integer.parseInt(s);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public static Double parseDouble(String s){
		try{
			return Double.parseDouble(s);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public static Long parseLong(String s){
		try{
			return Long.parseLong(s);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public static Boolean parseBoolean(String s){
		try{
			return Boolean.parseBoolean(s);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	/**returns double ranging from -a to a*/
	public static double rangedRandom(double a){
		return -a + Calc.random(a * 2);
	}
	
	/**get position of a grid cell in a 2d array (because im lazy denna mohfockuh)*/
	public static int getGridPosition(int x, int y, int width){
		return (y*width)+x;
	}

	/**generate a 4byte ARGB BufferedImage*/
	public static BufferedImage createByteABGRCopy(Image img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = bi.createGraphics();
		g.drawImage(img,0,0,null);
		g.dispose();
		return bi;
	}
	
	/**generate a 4byte ARGB BufferedImage, splitting the strip into num frames*/
	public static BufferedImage[] createByteABGRFrames(Image img,int num) {
		BufferedImage[] bi = new BufferedImage[num];
		for(int i=0; i<num; i++){
			bi[i] = new BufferedImage(img.getWidth(null)/num,img.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = bi[i].createGraphics();
			g.drawImage(img,-i*img.getWidth(null)/num,0,null);
			g.dispose();
			}
		return bi;
	}
	
	/**make a color from hue saturation and the v thing*/
	public static double[] getColorHSV(int h, int s, int v){
		double[] output=new double[3];
		Color c=new Color(Color.HSBtoRGB((float)(h/255.0), (float)(s/255.0), (float)(v/255.0)));
		output[0]=Math.min(1.0, (double)c.getRed()/255.0);
		output[1]=Math.min(1.0, (double)c.getGreen()/255.0);
		output[2]=Math.min(1.0, (double)c.getBlue()/255.0);
		return output;
	}
	
	/**key (integer) to string*/
	public static String keyToString(int k){
		String output="Unknown Key";
		
		//if the key is a letter
		if (k>=(int)'a' && k<=(int)'z'){
			output=""+(char)k;
			output=output.toUpperCase();
		}
		else if (k>=(int)'0' && k<=(int)'9') //if the key is a number
			output=""+(char)k;
		else if (k==KeyEvent.VK_SPACE)
			output="Space";
		else if (k==KeyEvent.VK_CONTROL)
			output="Control";
		else if (k==KeyEvent.VK_SHIFT)
			output="Shift";
		else if (k==KeyEvent.VK_ENTER)
			output="Enter";
		else if (k==KeyEvent.VK_ALT)
			output="Alt";
		
		return output;
	}
	/**Failsafe version of arccos*/
	public static double acos(double d){
		if (d <= -1)
			return  Math.PI;
		else if (d >= 1)
			return  0;
		return Math.acos(d);
	}
	
	///**make a color from hue saturation and the v thing*/
	/*public static int[] getColorHSV(int h, int s, int v){
		int[] output=new int[3];
		Color c=new Color(Color.HSBtoRGB((float)h/255, (float)s/255, (float)v/255));
		output[0]=c.getRed();
		output[1]=c.getRed();
		output[2]=c.getRed();
		return output;
	}*/
	
	/**format String color*/
	public static String formatColor(String hex){
		hex=hex.toLowerCase();
		
		if (hex.charAt(0)=='#')
			hex=hex.substring(1,7);
		return hex;
	}
	
	/**compare colors*/
	public static boolean compareColor(String a, String b){
		if (a!=null && b!=null)
			return (formatColor(a).equals(formatColor(b)));
		else
			return false;
	}
	
	/**input: "20", 4; output: "0020"*/
	public static String formatNumberString(String s, int n){
		String output = s;
		while (output.length() < n)
			output = "0"+output;
		return output;
	}
	
	/**input: 20, 4; output: "0020"*/
	public static String formatNumberString(int s, int n){
		return formatNumberString(""+s, n);
	}
	
	/**input an int of seconds!!! return a string in hh:mm:ss*/
	public static String formatTime(int seconds){
		TimeZone tz = TimeZone.getTimeZone("UTC");
	    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	    df.setTimeZone(tz);
	    return df.format(new Date((long)(seconds * 1000)));
	}
	
	/**return an array of rgb components from hex string, welcome to Oddwarg's Efficiency Hell!*/
	public static int[] getColorHex(String hex){
		char[] chars = hex.toCharArray();
		  int index = chars[0]=='#'?1:0;
		  return new int[]{
		    (chars[index+0]<65?chars[index+0]&(0xF):9+chars[index+0]&(0xF))<<4 | (chars[index+1]<65?chars[index+1]&(0xF):9+chars[index+1]&(0xF)),
		    (chars[index+2]<65?chars[index+2]&(0xF):9+chars[index+2]&(0xF))<<4 | (chars[index+3]<65?chars[index+3]&(0xF):9+chars[index+3]&(0xF)),
		    (chars[index+4]<65?chars[index+4]&(0xF):9+chars[index+4]&(0xF))<<4 | (chars[index+5]<65?chars[index+5]&(0xF):9+chars[index+5]&(0xF))
		    };
	}
	
	/**make a hex color from three doubles dude*/
	public static String makeHexColor(double... vals){
		return ""+formatNumberString(Integer.toHexString((int)((vals[0] * 256) - 1)),2)+""+formatNumberString(Integer.toHexString((int)((vals[1] * 256) - 1)),2)+""+formatNumberString(Integer.toHexString((int)((vals[2] * 256) - 1)),2);
	}
	
	/**make a hex color from three ints (0 - 255) dude*/
	public static String makeHexColorInt(int... vals){
		return ""+formatNumberString(Integer.toHexString((int)vals[0]),2)+""+formatNumberString(Integer.toHexString((int)vals[1]),2)+""+formatNumberString(Integer.toHexString((int)vals[2]),2);
	}
	
	public static void println(double... vals){
		for(double val: vals)
			System.out.print(val+", ");
		System.out.println();
	}
	
	public static double log2(double d){
		return Math.log(d)/Math.log(2);
	}
	
	/**is a within the range dictated by b and c*/
	public static boolean isContained(double a, double b, double c){
		return ((a >= b && a <= c) || (a >= c && a <= b));
	}
	
	/**authored by Gabriel Ivăncescu*/
	public static boolean triangleCircleCollision(double centrex, double centrey, double radius, double v1x, double v1y, double v2x, double v2y, double v3x, double v3y){
		//TEST 1: Vertex within circle
		double c1x = centrex - v1x;
		double c1y = centrey - v1y;
		
		double radiusSqr = radius*radius;
		double c1sqr = c1x*c1x + c1y*c1y - radiusSqr;
		
		if (c1sqr <= 0)
		  return true;
		
		double c2x = centrex - v2x;
		double c2y = centrey - v2y;
		double c2sqr = c2x*c2x + c2y*c2y - radiusSqr;
		
		if (c2sqr <= 0)
		  return true;
		
		double c3x = centrex - v3x;
		double c3y = centrey - v3y;
		
		double c3sqr = c3x*c3x + c3y*c3y - radiusSqr;
		radiusSqr = c3sqr;
		
		if (c3sqr <= 0)
		  return true;
		
		
		//TEST 2: Circle centre within triangle
		
		//Calculate edges
		double e1x = v2x - v1x;
		double e1y = v2y - v1y;
		
		double e2x = v3x - v2x;
		double e2y = v3y - v2y;
		
		double e3x = v1x - v3x;
		double e3y = v1y - v3y;
		
		if (e1y*c1x >= e1x*c1y && e2y*c2x >= e2x*c2y && e3y*c3x >= e3x*c3y)
			return true;
		
		
		//TEST 3: Circle intersects edge
		double k = c1x*e1x + c1y*e1y;
		
		if (k > 0)
		{
		  double len = e1x*e1x + e1y*e1y;
		
		  if (k < len)
		  {
		    if (c1sqr * len <= k*k)
		      return true;
		  }
		}
		
		//Second edge
		k = c2x*e2x + c2y*e2y;
		
		if (k > 0)
		{
		  double len = e2x*e2x + e2y*e2y;
		
		  if (k < len)
		  {
		    if (c2sqr * len <= k*k)
		      return true;
		  }
		}
		
		//Third edge
		k = c3x*e3x + c3y*e3y;
		
		if (k > 0)
		{
		  double len = e3x*e3x + e3y*e3y;
		
		  if (k < len)
		  {
		    if (c3sqr * len <= k*k)
		      return true;
		  }
		}
		
		//We're done, no intersection
		return false;
	}
}
