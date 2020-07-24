package main;

import java.util.ArrayList;

import graphics.Sprite;


public abstract class Entity implements Comparable<Entity> {
	public double x,y,xstart,ystart,xprevious,yprevious,hspeed,vspeed,xscale,yscale,angle,imageSpeed,imageSingle,alpha,colR,colG,colB;
	public Sprite sprite;
	public Mask mask;
	private int depth;
	public int orientation;
	private boolean destroyed;
	public boolean visible, persistent, stepActive;
	public ArrayList<Integer> collisionGroups;
	
	//alarma shit
	public boolean alarmActive;
	public int[] alarm;
	
	public Entity(double x, double y){
		Scene.addInstance(this);
		collisionGroups = new ArrayList<Integer>();
		
		//aha
		alarmActive = false;
		
		//x and y coordinates
		this.x = x;
		this.y = y;
		xstart = x;
		ystart = y;
		xprevious = x;
		yprevious = y;
		//horizontal and vertical speeds
		hspeed = 0;
		vspeed = 0;
		//collision group id
		setCollisionGroup(0);
		//shit
		destroyed = false;
		persistent = false;
		stepActive = true;
		//stuff
		visible = true;
		orientation = Sprite.CENTERED;
		//set the sprite and mask to sprites
		sprite = Sprite.get("test");
		mask = sprite.mask;
		//image index control
		imageSingle = 0;
		imageSpeed = 1;
		//transformations
		xscale = 1;
		yscale = 1;
		angle = 0;
		//alpha/color blending
		alpha = 1;
		colR = 1;
		colG = 1;
		colB = 1;
	}
	
	
	/**This sets the r,g,b color components to the values in the specified DOUBLE array (0-1)*/
	public void setColor(double[] col){
		colR = col[0];
		colG = col[1];
		colB = col[2];
	}
	
	/**This sets the r,g,b color components to the values in the specified INTEGER array (0-255)*/
	public void setColor(int[] col){
		colR = col[0]/255.0;
		colG = col[1]/255.0;
		colB = col[2]/255.0;
	}
	
	/**This sets the r,g,b color components to three r,g,b values (0.0 - 1.0)*/
	public void setColor(double r, double g, double b){
		colR = r;
		colG = g;
		colB = b;
	}
	
	/**lets add (a) collisionGroup(s)!!!*/
	public void addCollisionGroup(int... insert){
		for (int i=0; i<insert.length; i++){
			if (!collisionGroups.contains(insert[i]))
				collisionGroups.add(insert[i]);
		}
	}
	
	/**lets set the collisionGroup!!! (wipes all collision groups currently stored!)*/
	public void setCollisionGroup(int... insert){
		collisionGroups.clear();
		for (int i=0; i<insert.length; i++)
			collisionGroups.add(insert[i]);
	}
	
	/**lets remove (a) collisionGroup(s)!!!*/
	public void dropCollisionGroup(int... drop){
		for (int i=0; i<drop.length; i++){
			if (collisionGroups.contains(drop[i]))
				collisionGroups.remove((Integer)drop[i]);
		}
	}
	
	/**very useful function: set x and y coordinates to the supplied values*/
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**set rgb color components with a hex string*/
	public void setColor(String hex){
		int[] input;
		double[] output;
		
		input=Calc.getColorHex(hex);
		output=new double[3];
		
		for (int i=0; i<3; i++){
			output[i]=input[i]/255.0;
		}
		setColor(output);
	}
	
	/**this advances imageSingle by imageSpeed, sets x and yprevious, and adjusts coordinates based on horizontal and vertical speeds. it also calls alarmStep()*/
	public void step(){
		imageSingle+=imageSpeed;
		while(imageSingle>=sprite.textures.length)
			imageSingle-=sprite.textures.length;
		
		xprevious = x;
		yprevious = y;
		
		x += hspeed;
		y += vspeed;
		
		if (alarmActive)
			alarmStep();
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
	
	/**this is designed to be overridden and adjusted per your needs!!!*/
	public void alarmEvent(int i){
		
	}
	
	public void render(){
		sprite.render((int)imageSingle, orientation, x, y, xscale,yscale,angle,alpha,colR,colG,colB);
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
	public int compareTo(Entity other) {
		if(((Entity)other).getDepth()>depth)
			return 1;
		if(((Entity)other).getDepth()<depth)
			return -1;
		return 0;
	}
	
	/**destroy the entity*/
	public void destroy(){
		destroyed = true;
		Scene.removeInstance(this);
	}
	
	/**destroy the entity (this function should only be called by a room-changing system) [will be overridden if the entity is persistent!]*/
	public void roomDestroy(){
		if (!persistent){
			destroyed = true;
			Scene.removeInstance(this);
		}
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	public double getSpeed(){
		return Calc.pointDistance(0, 0, hspeed, vspeed);
	}
	public double getDirection(){
		return Calc.pointDirection(0, 0, hspeed, vspeed);
	}
	public void setSpeed(double newSpeed){
		double s = getSpeed();
		if (s!=0){
			hspeed*=newSpeed/s;
			vspeed*=newSpeed/s;
		}
		else{
			hspeed=newSpeed;
			vspeed=0;
		}
	}
	
	public void setDirection(double dir){
		double s = getSpeed();
		hspeed=Calc.dirX(s, dir);
		vspeed=Calc.dirY(s, dir);
	}
	
	public void setDirSpeed(double dir, double newSpeed){
		setSpeed(newSpeed);
		setDirection(dir);
	}

	/**are you a member of a list of collision integers???*/
	public boolean isOfCollisionGroup(int[] groups) {
		for(int i=0; i<groups.length; i++){
			if (collisionGroups.contains(groups[i]))
				return true;
		}
		return false;
	}
	
	/**are you a member of the following collision integer*/
	public boolean isOfCollisionGroup(int group) {
		return collisionGroups.contains(group);
	}


	public boolean collidesWith(Entity other, double x, double y) {
		return mask.collidesWith(this,x,y,other);
	}
		
	
}
