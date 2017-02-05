package player;

import entity.Life;
import graphics.Camera;
import maths.Vector3f;
import object.GameObject;
import world.Biome;

/**
 * @author TheKingInYellow
 */
public class Player extends GameObject {
	private static final float CLIMABLE = 1.5f;
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
	boolean noClip = true;

	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed() * SPEEDSCALER;
		upward = new Vector3f(0, 0, speed);
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 10);
		inventory = new Inventory(10);
		//inventory.add(new Item("item", 100, "I1"));
		self = new Life(1000);
		suitEnergy = 100;
		
	}

	public void update() {
		camera.pos = position;
		suitEnergy-=energyLoss ;
		if(suitEnergy<0){
			self.kill(false);
		}
	}

	public void movePlayer(String dir) {
		if(self.isLiving){
		this.target = camera.target;

		float vx = position.x - target.x;
		float vy = position.y - target.y;
		vx *= speed;
		vy *= speed;
		switch (dir) {
		case "UP":
			displacement = upward;
			break;
		case "DOWN":
			displacement = upward.negate();
			break;
		case "FORWARD":
			displacement = new Vector3f(-vx, -vy, 0);
			break;
		case "BACK":
			displacement = new Vector3f(vx, vy, 0);
			break;
		case "LEFT":
			displacement = new Vector3f(vy, -vx, 0);
			break;
		case "RIGHT":
			displacement = new Vector3f(-vy, vx, 0);
			break;
		default:
			System.err.println("wtf");
		}
		displacement = displacement.normalize().scale(speed);
		move();
		//Biome.effect();
		}
	}

	private void move() {
		boolean canMove = true;
		
		if (!noClip) {

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
