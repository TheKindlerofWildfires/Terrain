package entity;

import graphics.Window;
import maths.Vector3f;
import object.GameObject;
import world.Biome;

public class Slasher extends Monster{
	Vector3f direction = new Vector3f(0,0,0);
	int slashing;
	private Vector3f upward = new Vector3f(0,0,1);
	private static final float CLIMABLE = 2f;
	Vector3f[] destination = new Vector3f[4];
	public Slasher(String body, Vector3f position) {
		super(body, position);
		
	}
	public void construct(){
		GameObject blade1 = new GameObject("resources/models/cyl.obj", "none", true);
		GameObject blade2 = new GameObject("resources/models/cyl.obj", "none", true);
		blade1.placeAt(0f, -1.5f, 0);
		blade2.placeAt(-0f, 1.5f, 0);
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
		satellites.stream().forEach(o -> o.translate(new Vector3f((time-30)*.004f,0,0)));
	}
	
	public void update(){
		move();
		physic(); 
		satellites.stream().forEach(o -> o.physic());
		satellites.stream().forEach(o -> o.velocity = velocity);
		satellites.stream().forEach(o -> o.resting = resting);
		if(slashing>=0){
			slash(slashing);
			slashing--;
		}
	}
	private void move() { 
		int dir = Window.entityRandom.nextInt(20);
		if( dir == 1){
			direction = new Vector3f(0,.008f,0);
		}else if(dir == 2){
			direction = new Vector3f(0,-.008f,0);
		}else if (dir ==3){
			direction = new Vector3f(.008f,0,0);
		}else if(dir == 4){
			direction = new Vector3f(-.008f,0,0);
		}else if(dir == 5){
			if(slashing<0){
				slashing = 60;}
		}else if (dir == 6){
			setAcceleration(acceleration.normalize().scale(0.008f));
		}else{
			direction = new Vector3f(0,0,0);
		}
		addAcceleration(direction);
		boolean canMove = true;
		resting = true;
			// All this code should be redone with hitboxes prolly
			this.destination[0] = position.add(velocity.scale(25));
			this.destination[1] = position.add(velocity.scale(5).negate());
			this.destination[2] = position.add(velocity.scale(5).cross(upward.normalize()));
			this.destination[3] = position.add(velocity.scale(5).cross(upward.negate().normalize()));
			destination[0].z = Biome.getPlanet(destination[0], destination[0])[3] + 1f;
			destination[1].z = Biome.getPlanet(destination[1], destination[1])[3] + 1f;
			destination[2].z = Biome.getPlanet(destination[2], destination[2])[3] + 1f;
			destination[3].z = Biome.getPlanet(destination[3], destination[3])[3] + 1f;
			float rise = Math.max(Math.max(destination[0].z, destination[1].z),
					Math.max(destination[2].z, destination[3].z));
			rise = rise - position.z;
			if (rise > CLIMABLE) {
				canMove = false;
			} else if (rise < -CLIMABLE) {
				resting = false;
			} else if(resting){
				float[] diff = { position.z - destination[0].z, position.z - destination[1].z };
				float difference = Math.min(diff[0], diff[1]) * 0.1f;
				setVelocity(new Vector3f(velocity.x,velocity.y,velocity.z-difference));
			}
		if (!canMove) {
			setVelocity(new Vector3f(0,0,0));
		}
	}
	

}
