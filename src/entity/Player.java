package entity;

import maths.Vector3f;
import object.GameObject;
import world.Chunk;
import world.World;

public class Player extends GameObject {
	private Vector3f target;
	private float speed = .2f;
	private Vector3f upward;

	public Player() {
		super("resources/models/box.obj", "none", true);
		this.scale(1, 1, 1);
		upward = new Vector3f(0, 0, speed);
	}

	public void update() {
		float cZ = (float) Math.abs(World.noise.getValue(position.x, position.y, 0.1)) * Chunk.SIZE / 2 + .5f;
		//System.out.println(position);
		//System.out.println(cZ);
		float diff = position.z - cZ;
		//System.out.println(diff);
		this.translate(new Vector3f(0, 0, -diff));
	}

	public void movePlayer(String dir) {
		target = position.add(new Vector3f(0, 1, 0));
		Vector3f displacement = new Vector3f(0, 0, 0);
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
			displacement = new Vector3f(-vx, -vy, 0);// backward.negate();
			break;
		case "BACK":
			displacement = new Vector3f(vx, vy, 0);// backward;
			break;
		case "LEFT":
			displacement = new Vector3f(-vy, vx, 0);// left;
			break;
		case "RIGHT":
			displacement = new Vector3f(vy, -vx, 0);// left.negate();
			break;
		default:
			System.err.println("wtf");
		}
		displacement = displacement.normalize().scale(speed);
		this.translate(displacement);

	}
}
