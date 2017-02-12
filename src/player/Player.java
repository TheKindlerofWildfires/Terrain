package player;

import java.util.ArrayList;

import entity.Life;
import graphics.Camera;
import maths.Vector3f;
import object.GameObject;
import physics.Time;
import world.Biome;
import world.World;

/**
 * @author TheKingInYellow
 */
public class Player extends GameObject {
	private static final float CLIMABLE = 1.7f;
	private static final float SPEEDSCALER = 20;
	private Vector3f target;
	private float speed;
	private Vector3f upward;
	Camera camera;
	Vector3f displacement = new Vector3f(0, 0, 0);
	Vector3f[] destination = new Vector3f[4];
	public Inventory inventory;
	public Life self;
	public float suitEnergy;
	public float energyLoss = 0.0f;//0.1
	boolean noClip = false;
	ArrayList<String> lastKey = new ArrayList<String>();
	int keyTime;
	boolean onGround;

	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed() * SPEEDSCALER;
		upward = new Vector3f(0, 0, speed);
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 30);
		inventory = new Inventory(10);
		//inventory.add(new Item("item", 100, "I1"));
		self = new Life(1000);
		suitEnergy = 100;
		keyTime = Time.getSecTick();
		
	}

	public void update() {
		camera.pos = position;
		suitEnergy-=energyLoss*(.8+World.difficulty);
		if(Time.getSecTick() -keyTime>= 1){
			lastKey.clear();
		}
		if(suitEnergy<0){
			self.kill(false);
		}
		move();
	}

	public void movePlayer(String dir) {
		if(self.isLiving){
		this.target = camera.target;

		float vx = position.x - target.x;
		float vy = position.y - target.y;
		vx *= speed;
		vy *= speed;
		lastKey.add(dir);
		//For the love of god don't try and fix the normalizing here, i made it like this very intentionally 
		switch (dir) {
		case "UP":
			if(noClip){
				displacement = upward;
			}else{
				jump();
			}
			break;
		case "DOWN":
			if(noClip){
				displacement = upward.negate();
			}else{
				shift();
			}
			break;
		case "FORWARD":
			displacement = new Vector3f(-vx, -vy, 0);
			displacement = displacement.normalize().scale(speed);
			break;
		case "BACK":
			displacement = new Vector3f(vx, vy, 0);
			displacement = displacement.normalize().scale(speed);
			break;
		case "LEFT":
			displacement = new Vector3f(vy, -vx, 0);
			displacement = displacement.normalize().scale(speed);
			break;
		case "RIGHT":
			displacement = new Vector3f(-vy, vx, 0);
			displacement = displacement.normalize().scale(speed);
			break;
		default:
			System.err.println("wtf");
		}
		
		}
	}

	private void shift() {
		//Doesn't do anything yet, but should make player prone basically but that requires both hit boxes and and an understanding of cameras 
		
	}

	private void jump() {
	//This code is being bad!
		if(!lastKey.contains("UP")){
			System.out.println("h");
			displacement = displacement.add(upward.scale(9));
		}
		displacement = displacement.add(upward.scale(9));
		
	}

	private void move() {
		boolean canMove = true;
		onGround = true;
		if (!noClip) {
			//All this code should be redone with hitboxes prolly
			this.destination[0] = position.add(displacement.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(displacement.scale(5 / SPEEDSCALER).negate());
			this.destination[2] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			destination[0].z = Biome.getPlanet(destination[0], destination[0])[3] + 1.5f;
			destination[1].z = Biome.getPlanet(destination[1], destination[1])[3] + 1.5f;
			destination[2].z = Biome.getPlanet(destination[2], destination[2])[3] + 1.5f;
			destination[3].z = Biome.getPlanet(destination[3], destination[3])[3] + 1.5f;
			float rise = Math.max(Math.max(destination[0].z, destination[1].z),
					Math.max(destination[2].z, destination[3].z));
			rise = rise - position.z;

			if (rise > CLIMABLE) {
				canMove = false;// this part works on at least one side
			} else if (rise < -CLIMABLE) {
				displacement = displacement.add(upward.negate());
				onGround = false;
			} else {
				float[] diff = { position.z - destination[0].z, position.z - destination[1].z };
				float difference = Math.min(diff[0], diff[1]) * 0.1f;
				displacement = displacement.add(new Vector3f(0, 0, -difference));
			}
		}
		if (canMove) {
			this.translate(displacement);
			camera.move(displacement);
		}
		displacement = displacement.scale(.1f);
	}

	public void activeItem() {
		inventory.activeItem();
		
	}

	public void scroll(double s) {
		if(s==1 || s==-1){
			inventory.scroll(0);
		}else if(s>0){
			inventory.scroll(s-1);
		}else if(s<0){
			inventory.scroll(s+1);
		}
		//System.out.println(s);
		
	}

}
