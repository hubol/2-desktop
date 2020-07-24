package world.event;

import graphics.Font;
import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.player.Player;

import main.Entity;
import main.Input;
import main.Scene;

public class Feelings extends Entity{
	public int entries;
	public String[] statement, name;
	public boolean active;
	public int phase;
	
	public String inputStatement, inputName;
	public Font font;
	
	public boolean show;
	
	public Shake s;

	public Feelings(double x, double y) {
		super(x, y);
		
		Player.control = false;
		
		setDepth(Integer.MIN_VALUE + 6);
		
		new SoundLoader(false, "sIntroKey", "sFeeling");
		new SpriteLoader("sFeelingBox", "sFeelingName", "sFeelingStatement","Isuck_48");
		
		font = new Font("Isuck",true,'|');
		
		active = true;
		phase = 0;
		
		inputStatement = "";
		inputName = "";
		
		show = true;
		
		s = new Shake(.369);
		
		visible = false;
		
		alarmInitialize(2);
		alarm[0] = 15;
		alarm[1] = 1;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			show = !show;
			alarm[0] = 15;
		}
		else if (i == 1){
			Sound.playPitched("sFeeling", .1);
			visible = true;
		}
	}
	
	public void step(){
		if (active){
			String input = "";
			for (int i=0; i<Input.maxKey; i++){
				int keyInt = i;
				if (Input.checkFrameKey(i) && ((keyInt>=65 && keyInt<=90)||(keyInt>=97 && keyInt<=122)))
					input += ""+(char)keyInt;
			}
			
			if (Input.checkFrameKey(KeyEvent.VK_SPACE))
				input += " ";
			
			boolean a = (input.equals("") && Input.checkFrameKey(KeyEvent.VK_BACK_SPACE));
			
			String s;
			
			if (phase == 0){
				inputStatement += input;
				if (a && inputStatement.length() > 0)
					inputStatement = inputStatement.substring(0, inputStatement.length()-1);
				s = inputStatement;
			}
			else{
				inputName += input;
				if (a && inputName.length() > 0)
					inputName = inputName.substring(0, inputName.length()-1);
				s = inputName;
			}
			
			if (inputStatement.length() > 123)
				inputStatement = inputStatement.substring(0, 123);
			
			if (inputName.length() > 15)
				inputName = inputName.substring(0, 15);
			
			if ((a || !input.equals("")) && !s.equals(""))
				Sound.playPitched("sIntroKey", .03);
			
			if (Input.checkFrameKey(KeyEvent.VK_ENTER) && s.length() > 0){
				Sound.playPitched("sFeeling", .1);
				if (phase != 0){
					try {
						submit(inputStatement, inputName);
						refresh();
						//TODO do something with this data u just loaded
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					show = true;
					phase += 1;
				}
			}
		}
		
		alarmStep();
	}
	
	public void render(){
		//final Sprite diag = Sprite.get("sFeelingBox");
		String title = "HOW DO YOU FEEL ABOUT THIS GAME?", input = inputStatement, pre;
		Sprite boxs = Sprite.get("sFeelingStatement");
		
		if (phase == 1){
			title = "WHAT IS YOUR NAME?";
			input = inputName;
			boxs = Sprite.get("sFeelingName");
		}
		input = Text.widthLimit(input, 26);
		pre = input;
		if (show)
			input += "|";
		
		final String text = "FF002E", box = "66C9FF";//, dialog = "A796FF";
		
		//diag.render(0, Sprite.CENTERED, 320 + s.x, 240 + s.y, 1, 1, 0, 1, dialog);
		boxs.render(0, Sprite.CENTERED, 320 - s.y, 252 + s.x, 1, 1, 0, 1, box);
		
		Text.randomize(.2);
		Text.setFont(font);
		Graphics.setColor(text);
		Graphics.setAlpha(1);
		
		//Text.drawTextExt(332 - ((diag.imageWidth) / 2.0) - s.x, 260 - ((diag.imageHeight) / 2.0) - s.y, title, .5625, .5625, 0);
		Text.orientation = Text.SOUTH;
		Text.idiot = false;
		Text.drawTextExt(320 - s.x, 234 - ((boxs.imageHeight) / 2.0) - s.y, title, .5625, .5625, 0);
		Text.orientation = Text.NORTH;
		if (!pre.equals(""))
			Text.drawTextExt(320 + s.x, 276 + ((boxs.imageHeight) / 2.0) + s.y, "PRESS ENTER TO CONFIRM", .5625, .5625, 0);
		Text.idiot = true;
		Text.orientation = Text.NORTHWEST;
		Text.drawTextExt(324 - ((boxs.imageWidth - 20) / 2.0) + s.y, 260 - ((boxs.imageHeight - 20) / 2.0) - s.x, input, .5625, .5625, 0);
		
		Text.randomize(0);
	}
	
	/**write a feeling "statement" - "name"*/
	public void submit(String statement, String name) throws IOException{
		statement = statement.toUpperCase();
		name = name.toUpperCase();
		
		if (statement.length() > 123)
			statement = statement.substring(0, 123);
		
		if (name.length() > 15)
			name = name.substring(0, 15);
		
		// Construct data
	    String data = encode("statement", statement);
	    data += "&" + encode("name",name);
	    
	    // Send data
	    URL url = new URL("http://www.hubolhubolhubol.com/FILES/CrazdTwo/addFeeling.php");
	    
	    URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.write(data);
	    wr.flush();
	    
	    // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
	    	if (Scene.console)
	    		System.out.println(line);
	    }
	    wr.close();
	    rd.close();
	}
	
	/**obtain the statments and respective names*/
	public void refresh() throws IOException{
		URL url = new URL("http://www.hubolhubolhubol.com/FILES/CrazdTwo/getFeeling.php");

	    URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.flush();
	
	    // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) { //now you have the line, split it up into stuff
	        String[] splitLine=line.split("%");
	        entries = (int)(splitLine.length/2);
	        name =  new String[entries+1];
	        statement = new String[entries+1];
	        
	        boolean fck=true;
	        for (int i=0; i<splitLine.length; i++){
	        	if (fck)
	        		statement[(int)(i/2)]=splitLine[i];
	        	else
	        		name[(int)(i/2)]=splitLine[i];
	        	fck=!fck;
	        }
	    }
	    wr.close();
	    rd.close();
	}
	
	/**this does something important, what, i cant remember for i wrote it more than a year ago. it was
	 * probably oddwarg who wrote it anyway
	 */
	public String encode(String s, String sB) throws UnsupportedEncodingException{
		return URLEncoder.encode(s, "UTF-8") + "=" + URLEncoder.encode(sB, "UTF-8");
	}

}
