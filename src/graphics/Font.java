package graphics;

import java.util.HashMap;

public class Font {
	public HashMap<Character, Integer> symbols;
	public Sprite sprite;
	
	/**initialize font with ordered characters (note that you cannot use % as a character as this is used for new lines)*/
	public Font(String s, char... sym){
		sprite = Sprite.get(s);
		
		symbols = new HashMap<Character, Integer>();
		for (int i=0; i<sym.length; i++)
			symbols.put(sym[i], i);
	}
	
	/**initialze font with standard characters*/
	public Font(String s){
		sprite = Sprite.get(s);
		char[] sym = standard();
		
		symbols = new HashMap<Character, Integer>();
		for (int i=0; i<sym.length; i++)
			symbols.put(sym[i], i);
	}
	
	/**initialze font with standard characters + adtnl chars*/
	public Font(String s, boolean a, char... bym){
		sprite = Sprite.get(s);
		char[] sym = standard();
		
		symbols = new HashMap<Character, Integer>();
		int j = 0;
		for (int i=0; i<sym.length; i++){
			symbols.put(sym[i], i);
			j += 1;
		}
		for (int i=0; i<bym.length; i++){
			symbols.put(bym[i], j);
			j += 1;
		}
	}
	
	/**this is NOT stupid*/
	public char[] standard(){
		char[] sym = new char[47];
		sym[0] = '0'; sym[1] = '1'; sym[2] = '2'; sym[3] = '3'; sym[4] = '4'; sym[5] = '5'; sym[6] = '6'; sym[7] = '7';
		sym[8] = '8'; sym[9] = '9'; sym[10] = 'A'; sym[11] = 'B'; sym[12] = 'C'; sym[13] = 'D'; sym[14] = 'E'; sym[15] = 'F';
		sym[16] = 'G'; sym[17] = 'H'; sym[18] = 'I'; sym[19] = 'J'; sym[20] = 'K'; sym[21] = 'L'; sym[22] = 'M'; sym[23] = 'N';
		sym[24] = 'O'; sym[25] = 'P'; sym[26] = 'Q'; sym[27] = 'R'; sym[28] = 'S'; sym[29] = 'T'; sym[30] = 'U';sym[31] = 'V';
		sym[32] = 'W'; sym[33] = 'X'; sym[34] = 'Y'; sym[35] = 'Z'; sym[36] = '.'; sym[37] = '!'; sym[38] = '?'; sym[39] = ',';
		sym[40] = '#'; sym[41] = '\''; sym[42] = '"'; sym[43] = '$'; sym[44] = ':'; sym[45] = ';'; sym[46] = ' ';
		
		return sym;
	}
	
	public int fetchSymbolId(char i){
		if (symbols.containsKey(i))
			return symbols.get(i);
		else{
			return symbols.get(' ');
		}
	}

}
