package entity;

import graphics.Camera;
import maths.Vector3f;
import object.GameObject;
import world.Chunk;
import world.World;

public class Player extends GameObject {
	private Vector3f target;
	private float speed;
	private Vector3f upward;

	Camera camera;

	public Player() {
		super("resources/models/box.obj", "none", true);
		upward = new Vector3f(0, 0, speed);
	}

	public void slaveCamera(Camera camera) {
		this.camera = camera;
		this.target = camera.getTarget();
		this.speed = camera.getSpeed();
	}

	public void update() {
		int chunkX = Math.round(camera.pos.x / 2 / Chunk.SIZE);
		int chunkY = Math.round(camera.pos.y / 2 / Chunk.SIZE);
		//This line exists because I can't get the chunk from the chunklist in world
		Chunk myChunk = new Chunk(World.noise, chunkX, chunkY);
		float cZ = myChunk.getHeight(camera.pos.x, camera.pos.y) + 1f;
		//float cZ = (float) Math.abs(World.noise.getValue(position.x, position.y, 0.1)) * Chunk.SIZE / 2 + .5f;
		/*
		 * Lets replace that takes the x, y points, 
		 * finds the triangle, 
		 * and takes the calculated value of the location at the point based on the points h
		 * this could be some fun math! 
		 */
		float diff = position.z - cZ;
		diff = (float) (Math.pow(diff, 3) * 0.03f);
		camera.moveCamera(new Vector3f(0, 0, -diff));
		this.placeAt(camera.pos.x, camera.pos.y, camera.pos.z);
		//this.target = camera.getTarget();
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
