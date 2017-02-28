package entity;

import graphics.Window;
import maths.Vector3f;
import object.GameObject;
import physics.Time;

public class Slasher extends Monster{
	Vector3f direction = new Vector3f(0,0,0);
	int slashing;
	public Slasher(String body, Vector3f position) {
		super(body, position);
		
	}
	public void construct(){
		GameObject blade1 = new GameObject("resources/models/cyl.obj", "none", true);
		GameObject blade2 = new GameObject("resources/models/cyl.obj", "none", true);
		blade1.placeAt(-.75f, -1.5f, 0);
		blade2.placeAt(-.75f, 1.5f, 0);
		blade1.scale(1f, .04f, 1f);
		blade2.scale(1f, .04f, 1f);
		blade1.rotate(90,1,0,0);
		blade2.rotate(90,1,0,0);
		blade1.resting = true;
		blade2.resting = true;
		satellites.add(blade1);
		satellites.add(blade2);
		satellites.stream().forEach(o -> o.material.colour = new Vector3f(0,1,1));
	}
	public void slash(int time){
		double t = time*Math.PI/10;
		satellites.stream().forEach(o -> o.velocity = (new Vector3f((float)Math.cos(t + Math.PI)*.2f, 0,0)));
	}
	
	public void update(){
		move();
		satellites.stream().forEach(o -> o.physic());
		physic(); 
		if(slashing>0){
			slashing--;
			slash(slashing);
		}
	}
	private void move() { 
		int dir = Window.entityRandom.nextInt(10);
		if( dir == 1){
			direction = new Vector3f(0,1,0);
		}else if(dir == 2){
			//direction = new Vector3f(0,-1,0);
		}else if (dir ==3){
			if(slashing<=0){
			slashing = 10;}
		}else{
			direction = new Vector3f(0,0,0);
		}
		acceleration.add(direction);
		
	}

}
