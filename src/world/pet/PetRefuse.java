package world.pet;

public class PetRefuse extends PetEntity{
	public int cXsc, times;

	public PetRefuse(double x, double y, PetShell p) {
		super(x, y, p);
		cXsc = 1;
		alarmInitialize(1);
		times = 1;
		alarm[0] = 15;
	}
	
	public void alarmEvent(int i){
		cXsc *= -1;
		times += 1;
		if (times == 5){
			new PetMain(0, 0, shell);
			destroy();
		}
		else
			alarm[0] = 15;
	}
	
	public void render(){
		shell.character.render(5, 8, 14, cXsc, 1, shell);
	}

}
