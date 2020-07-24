package world.turkey;

import world.control.Global;
import world.interact.Message;
import main.Calc;
import main.Entity;

public class TownSpawn extends Entity{
	public final int NEUTRAL = 0, HAPPY = 1, SAD = 2;

	public TownSpawn(double x, double y, int i) {
		super(x, y);
		
		Turkey j;
		if (i == 0){
			j = new Turkey(x, y, "sBlindFace", "sBlindFeather", "EEFF28", "sBlindSpeak", 1.0);
			if (Global.hexMap[Calc.getGridPosition(7, 3, 16)].equals("000000")){
				j.setConversation(new Message("the [blind] can speak!! yes!! this is not a fiction!!"), new Message("these are the things life is made of!! savor them!!"), new Message("heed my words!! you must!! don't you know how to behave yourself in the presence of [the blind prophet]!!"), new Message("remember me and i shall forget you and your unsavory scent!!"), new Message("now, begone!!"));
				j.setEmotions(HAPPY, NEUTRAL, HAPPY, NEUTRAL, HAPPY);
				j.setYelps("!!");
			}
			else{
				j.setConversation(new Message("return from vacation!! relaxation!! it soothes!!"));
				j.setEmotions(HAPPY);
				j.setYelps("STRESS!!","PAIN!!","DEPRESSION!!","HEARTBREAK!!");
			}
		}
		else if (i == 1){
			j = new Turkey(x, y, "sWeirdFace", "sWeirdFeather", "B5F700", "sWeirdSpeak", 1.0);
			j.setConversation(new Message("what are you here for? [who] summoned you?"), new Message("it was a trap! there is no [summoner]!"), new Message("you are the [summoner]! i am the [summoner]! follow your heart!"), new Message("heck yeah!"));
			j.setEmotions(NEUTRAL, HAPPY, HAPPY, HAPPY);
			j.setYelps("TROLL!","EPIC NPC HEADSHOT!","NOOB!","SCRUB!");
		}
		else if (i == 2){
			j = new Turkey(x, y, "sMouthFace", "sMouthFeather", "EEFF28", "sMouthSpeak", 1.0);
			j.setConversation(new Message("an [intruder]! an [intruder]!! an [intruder]!!!"), new Message("what is an [intruder] doing in this place of our coolness! an [intruder] is unwelcome!!"), new Message("an [intruder] is to be escorted! expulsion is the punishment!! such is the way of things!!!"), new Message("a [gas leak]! it does a thing on our brains!!"));
			j.setEmotions(HAPPY, NEUTRAL, HAPPY, SAD);
			j.setYelps("HITTEN!","CURSES!","MULTIKILL!","EPIC HEADSHOT!");
		}
		else if (i == 3){
			j = new Turkey(x, y, "sCuteBoyFace", "sCuteBoyFeather", "B5F700", "sCuteBoySpeak", 1.0);
			if (!Global.heroMode){
				j.setConversation(new Message("a [sparkle]! a time to shine! this one is yours!"), new Message("what? did you not come prepared?"), new Message("show them all that you've got! that is an order, mister!"), new Message("a gender is not settled on you, hm? i respect you!"));
				j.setEmotions(HAPPY, NEUTRAL, HAPPY, NEUTRAL);
			}
			else{
				j.setConversation(new Message("a [sparkle]! a time to shine! this one is yours!"), new Message("what? did you not come prepared?"), new Message("show them all that you've got! that is an order, missy!"));
				j.setEmotions(HAPPY, NEUTRAL, HAPPY, NEUTRAL);
			}
			j.setYelps("FREN!","ULUM!");
		}
		else if (i == 4){
			j = new Turkey(x, y, "sGalFace", "sGalFeather", "EEFF28", "sGalSpeak", 1.0);
			j.setConversation(new Message("a waste?! a waste?! you think this is a waste?!"), new Message("i can show you a waste! it's what scares you and me!"), new Message("you and i we are not too unsimilar! you are merely less evolved than i!"), new Message("a change is in order, no? you must do what is correct!"), new Message("their task is to [kill all men]! and you both are well aligned if you ask me!"));
			j.setEmotions(HAPPY, HAPPY, HAPPY, NEUTRAL, HAPPY);
			j.setYelps("1!","22!","333!","4444!");
		}
		else if (i == 5){
			j = new Turkey(x, y, "sBlindFace", "sBlindFeather", "EEFF28", "sBlindSpeak", 1.0);
			if (!Global.heartGot[33]){
				j.setConversation(new Message("a vacation home!! sure!! you got me!!"), new Message("[the blind abode]!! ablind!! i'm working on it!!"), new Message("good sales!! a profit!! this home!! so what!!"), new Message("leave me alone!!"));
				j.setEmotions(HAPPY, NEUTRAL, HAPPY, SAD);
			}
			else{
				if (Calc.random(1) < .5){
					j.setConversation(new Message("a heart breaker!! but amiss!!"));
					j.setEmotions(HAPPY);
				}
				else{
					j.setConversation(new Message("i miss!! it is him!!"));
					j.setEmotions(SAD);
				}
			}
			j.setYelps("SHABIP!!","SHABITTY!!","BOP!!","SHABOP!!","SHABIPPITY BOP!!");
		}
	}

}
