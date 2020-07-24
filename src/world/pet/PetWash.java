package world.pet;

public class PetWash extends PetEntity{
	public double sub;
	public PetMain mommy;

	public PetWash(double x, double y, PetShell p, PetMain m) {
		super(x, y, p);
		sub = 0;
		mommy = m;
	}
	
	public void step(){
		sub -= .5;
		mommy.washOff = (int)Math.ceil(sub);
		
		if (sub <= -47){
			mommy.active = true;
			mommy.washOff = 0;
			if (shell.poops != 0){
				mommy.destroy();
				new PetHappy(0,0,shell);
			}
			shell.poops = 0;
			destroy();
		}
	}
	
	public void render(){
		shell.wash.render(0, 34 + Math.ceil(sub), 0, shell);
	}

}
