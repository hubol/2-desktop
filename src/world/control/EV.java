package world.control;

public class EV{
	public final static int
	JELLYBOSS = 0,
	ADVENTURE_GAME_COMPLETE = 1,
	MUSIC_DREAM = 2,
	SKULL_DEFEAT = 3,
	TREE_PROG = 4,
	HEART_DREAM = 5,
	SUNLIGHT_DREAM = 6,
	PENIS_DREAM = 7,
	BANAAN_DEFEAT = 8,
	WEED_JOKE = 9,
	
	DISH_PUZZLE = 10,
	TRUCK_UNLOCKED = 11,
	MONSTER_WINGED = 12,
	JAR_DREAM = 13,
	MAP_VIEW = 14,
	PET_DREAM = 15,
	GOT_SNORKEL = 16,
	GOT_LUNGS = 17, //TODO (maybe)
	FIELD_KILL = 18,
	BATH_BREAK = 19,
	
	BATH_ARROWS = 20,
	SLICK_DEFEAT = 21,
	FUNWITHBALLOONS = 22,
	RAINWEIRD = 23,
	RAINBLOCK = 24,
	BABEPROG = 25,
	MOREFUNWITHBALLOONS = 26,
	RAINKILL = 27,
	ROOMOFDOORS = 28,
	PLANTDOOR = 29,
	
	STRAWBERRY = 30,
	LEMON = 31,
	LOVERCRACK = 32,
	PUKE_DEFEAT = 33,
	PLANTMAZE = 34,
	DINGUS = 35,
	KISSADD = 36,
	ABOVEADD = 37,
	DUKE_DEFEAT = 38,
	KISSKEYKRAK = 39,
	
	BOMBUPGRADE = 40,
	PAYCLOUD1 = 41,
	PAYCLOUD2 = 42,
	ONOFF = 43,
	PAPA_DEFEAT = 44,
	BULB = 45,
	STUPIDHEART = 46,
	KISSFED = 47,
	BULBPAY = 48,
	FINALENTER = 49,
	
	FINALSTRAW = 50,
	FINALLEMON = 51,
	FINALBALLOON = 52,
	FINALCRACK = 53,
	FINALBLOODBALLOON = 54,
	FINALURCHIN = 55,
	
	PDRIFTER = 56,
	PJELLY = 57,
	PSLICK = 58,
	PBANAAN = 59,
	PPUKE = 60,
	PDUKE = 61,
	PPAPA = 62;

	/**if youre too lazy and youre just checking if a Global.event[integer i] is equal to integer check. wow what a stupid function i wonder if ive actually even used it. jesus christ*/
	public static boolean checkEvent(int i, int check){
		return Global.event[i] == check;
	}

}
