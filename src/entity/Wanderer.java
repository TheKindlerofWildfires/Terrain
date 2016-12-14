package entity;

import java.util.Random;

import graphics.Window;
import maths.Vector3f;
import object.GameObject;
import world.Biome;

/**
 * @author TheKingInYellow
 */
public class Wanderer extends GameObject {
	private static final float CLIMABLE = 10f;
	private static final float speed = 0.4f;
	private static final int SPEEDSCALER = 10;
	Vector3f upward = new Vector3f(0, 0, speed);
	Random random = Window.entityRandom;
	private Vector3f target;
	private Vector3f displacement;
	private Vector3f[] destination;

	public Wanderer(String model) {
		super(model, "none", true);
		target = this.position.add(new Vector3f(1,0,0));
	}

	public void update() {
		direct(-1);
		System.out.println(position.x);
	}

	public void direct(int dir) {
		String direction = "STOP"; 
		int r;
		if (dir == -1) {
			 r = random.nextInt(20);
		}else{
			r = dir;
		}
		if (r == 0) {
			direction = "STOP";
		} else if (r == 1) {
			direction = "FORWARD";
		} else if (r == 2) {
			direction = "BACK";
		} else if (r == 3) {
			direction = "RIGHT";
		} else if (r == 4) {
			direction = "LEFT";
		}
		moveObject(direction);
	}
	public void moveObject(String dir) {
		displacement = new Vector3f(0,0,0);
		float vx = position.x - target.x;
		float vy = position.y - target.y;
		vx *= speed;
		vy *= speed;
		switch (dir) {
		case "FORWARD":
			displacement = new Vector3f(-vx, -vy, 0);// backward.negate();
			break;
		case "BACK":
			displacement = new Vector3f(vx, vy, 0);// backward;
			break;
		case "LEFT":
			displacement = new Vector3f(vy, -vx, 0);// left;
			break;
		case "RIGHT":
			displacement = new Vector3f(-vy, vx, 0);// left.negate();
			break;
		case "STOP":
			displacement = new Vector3f(0,0,0);
		default:
			System.out.println(dir=="STOP");
			System.err.println("wtf");
		}
		displacement = displacement.normalize().scale(speed);
		move();

	}

	private void move() {
		boolean canMove = true;
		boolean noClip = true;
		if (!noClip) {
			
			this.destination[0] = position.add(displacement.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(displacement.scale(5 / SPEEDSCALER).negate());
			
			this.destination[2] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			
			destination[0].z = Biome.getValue(destination[0], destination[0],false)[3] + 1.5f;
			destination[1].z = Biome.getValue(destination[1], destination[1],false)[3] + 1.5f;
			destination[2].z = Biome.getValue(destination[2], destination[2],false)[3] + 1.5f;
			destination[3].z = Biome.getValue(destination[3], destination[3],false)[3] + 1.5f;
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
		}
	}
}
