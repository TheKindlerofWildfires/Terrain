package player;

import entity.Life;
import graphics.Camera;
import graphics.Window;
import input.KeyboardInput;
import input.MouseButtonCallback;
import input.ScrollCallback;
import maths.Vector3f;
import object.GameObject;
import physics.Time;
import world.Biome;
import world.World;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author TheKingInYellow
 */
public class Player extends GameObject {
	private static final float CLIMABLE = 1.7f;
	private static final float SPEEDSCALER = 2;
	private static final int ALLOWEDJUMPS = 1;
	Vector3f target;
	private float speed;
	private Vector3f upward;
	Camera camera;
	Vector3f[] destination = new Vector3f[4];
	public Inventory inventory= new Inventory(10);;
	public Life self;
	public float suitEnergy;
	public float energyLoss = 0.0f;// 0.1
	boolean noClip = false;
	int jumpCount = 0;
	int[] last = new int[6];
	int jumping = 0;
	boolean canJump = true;
	public Item i = new Shooter("item", 100, 0, "B1");
	int c = 0;

	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed() * SPEEDSCALER;
		upward = new Vector3f(0, 0, speed);
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 30);
		inventory.add(new Shooter("item", 40, 0, "B1"));
		self = new Life(1000);
		suitEnergy = 100;
		last[3] = last[0] = Time.getUpdateTick();
	}
	public void update() {
		keycall();
		camera.pos = position;
		target = camera.target;
		suitEnergy -= energyLoss * (.8 + World.difficulty);
		if (suitEnergy < 0) {
			self.kill(false);
		}
		if (resting) {
			jumpCount = ALLOWEDJUMPS;
		}
		if(jumping>0){
			velocity = velocity.add(upward.scale(jumping));
			jumping--;
			}
		if(KeyboardInput.isKeyPressed(GLFW_KEY_SPACE)){
			canJump = false;
		}else{
			canJump = true;
		}
		move();
		physic();
		camera.move(velocity);
	}
	public void movePlayer(String dir) {
		if (self.isLiving) {
			this.target = camera.target;

			float vx = position.x - target.x;
			float vy = position.y - target.y;
			vx *= speed;
			vy *= speed;
			// For the love of god don't try and fix the normalizing here
			switch (dir) {
			case "UP":
				if (noClip) {
					velocity = upward;
				} else if(canJump){
					jump();
				}
				break;
			case "DOWN":
				if (noClip) {
					velocity = upward.negate();
				} else {
					shift();
				}
				break;
			case "FORWARD":
				velocity = velocity.add(new Vector3f(-vx, -vy, 0).normalize().scale(speed));
				if(KeyboardInput.isKeyPressed(GLFW_KEY_LEFT_SHIFT)||KeyboardInput.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
					velocity = velocity.add(new Vector3f(-vx, -vy, 0).normalize().scale(speed));
				}
				break;
			case "BACK":
				velocity = velocity.add(new Vector3f(vx, vy, 0).normalize().scale(speed));
				break;
			case "LEFT":
				velocity = velocity.add(new Vector3f(vy, -vx, 0).normalize().scale(speed));
				break;
			case "RIGHT":
				velocity = velocity.add(new Vector3f(-vy, vx, 0).normalize().scale(speed));
				break;
			default:
				System.err.println("wtf");
			}

		}
	}
	private void shift() {
		// Doesn't do anything yet, but should make player prone basically but
		// that requires both hit boxes and and an understanding of cameras

	}
	private void jump() {
		if (jumpCount > 0) {
			jumping = 7;
			jumpCount--;
		}
	}
	private void move() {
		boolean canMove = true;
		resting = true;
		if (!noClip) {
			// All this code should be redone with hitboxes prolly
			this.destination[0] = position.add(velocity.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(velocity.scale(5 / SPEEDSCALER).negate());
			this.destination[2] = position.add(velocity.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(velocity.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			destination[0].z = Biome.getPlanet(destination[0], destination[0])[3] + 1.5f;
			destination[1].z = Biome.getPlanet(destination[1], destination[1])[3] + 1.5f;
			destination[2].z = Biome.getPlanet(destination[2], destination[2])[3] + 1.5f;
			destination[3].z = Biome.getPlanet(destination[3], destination[3])[3] + 1.5f;
			float rise = Math.max(Math.max(destination[0].z, destination[1].z),
					Math.max(destination[2].z, destination[3].z));
			rise = rise - position.z;

			if (rise > CLIMABLE) {
				canMove = false;
			} else if (rise < -CLIMABLE) {
				resting = false;
			} else if(resting){//this statement needs help ie if !falling
				float[] diff = { position.z - destination[0].z, position.z - destination[1].z };
				float difference = Math.min(diff[0], diff[1]) * 0.1f;
				velocity.z = velocity.z -difference;// = velocity.add(new Vector3f(0, 0, -difference));//this is the smooth walk that is messing me up
			}
		}
		if (!canMove) {
			velocity = new Vector3f(0,0,0);
		}
	}
	public void activeItem() {
		inventory.activeItem();
	}
	public void scroll(double s) {
		if (s == 1 || s == -1) {
			inventory.scroll(0);
		} else if (s > 0) {
			inventory.scroll(s - 1);
		} else if (s < 0) {
			inventory.scroll(s + 1);
		}
	}
	private void keycall() {
		if (KeyboardInput.isKeyPressed(GLFW_KEY_A)||KeyboardInput.isKeyDown(GLFW_KEY_A)) {
			movePlayer("LEFT");
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_D)||KeyboardInput.isKeyDown(GLFW_KEY_D)) {
			movePlayer("RIGHT");
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_W)||KeyboardInput.isKeyDown(GLFW_KEY_W)) {
			movePlayer("FORWARD");
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_S)||KeyboardInput.isKeyDown(GLFW_KEY_S)) {
			movePlayer("BACK");
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_SPACE)&& !KeyboardInput.isKeyDown(GLFW_KEY_SPACE))	 {
			movePlayer("UP");
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_C)||KeyboardInput.isKeyDown(GLFW_KEY_C))	 {
			movePlayer("DOWN");
		}
		int[] m = MouseButtonCallback.getMouseButton();
		if (m[1] == GLFW_PRESS) {
			if (m[0] == GLFW_MOUSE_BUTTON_LEFT) {
				activeItem();
			}
		}
		double s = ScrollCallback.getyoffset();
		scroll(s);
		if (KeyboardInput.isKeyPressed(GLFW_KEY_1)) {
			inventory.setActive(0);
			//player.i.activate();
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_2)) {
			inventory.setActive(1);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_3)) {
			inventory.setActive(2);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_4)) {
			inventory.setActive(3);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_5)) {
			inventory.setActive(4);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_6)) {
			inventory.setActive(5);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_7)) {
			inventory.setActive(6);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_8)) {
			inventory.setActive(7);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_9)) {
			inventory.setActive(8);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_0)) {
			inventory.setActive(0);
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_P)) {
			Window.reload(c++ % 3);
		}
		
	}

}
