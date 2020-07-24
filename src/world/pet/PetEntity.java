package world.pet;

public class PetEntity implements Comparable<PetEntity>{
	public double x, y;
	public PetSprite sprite;
	public PetShell shell;
	
	//alarma shit
	public boolean alarmActive;
	public int[] alarm;
	
	private int depth = 0;
	
	public PetEntity(double x, double y, PetShell p){
		this.x = x;
		this.y = y;
		
		//aha
		alarmActive = false;
		
		shell = p;
		shell.entities.add(this);
		
		sprite = null;
	}
	
	/**the select button is pressed*/
	public void select(){
	}
	
	/**the confirm button is pressed*/
	public void confirm(){
	}
	
	/**the cancel button is pressed*/
	public void cancel(){
	}
	
	/**update*/
	public void step(){
		if (alarmActive)
			alarmStep();
	}
	
	/**remove this entity from its shell*/
	public void destroy(){
		shell.entities.remove(this);
	}
	
	public void render(){
		if (!sprite.equals(null)){
			
		}
	}
	
	/**this enables alarms!!!!!! do not call this more than once!!!!*/
	public void alarmInitialize(int i){
		alarmActive = true;
		alarm = new int[i];
		for (int j=0; j<i; j++)
			alarm[j] = -1;
	}
	
	/**this advances the entity's alarm and calls events if necessary!!! it is not necessary to call this if you are already calling the step()*/
	public void alarmStep(){
		for (int i=0; i<alarm.length; i++){
			if (alarm[i] > -1)
				alarm[i] -= 1;
			if (alarm[i] == 0)
				alarmEvent(i);
		}
	}
	
	/**set the depth of the entity to an integer*/
	public void setDepth(int newDepth){
		depth=newDepth;
	}
	
	/**get the depth of the entity*/
	public int getDepth(){
		return depth;
	}

	@Override
	public int compareTo(PetEntity other) {
		if (other.getDepth() > depth)
			return 1;
		if (other.getDepth() < depth)
			return -1;
		return 0;
	}
	
	/**this is designed to be overridden and adjusted per your needs!!!*/
	public void alarmEvent(int i){
	}

}
