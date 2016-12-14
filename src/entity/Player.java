package entity;

import graphics.Camera;
import maths.Vector3f;
import object.GameObject;
import world.Chunk;
import world.World;

public class Player extends GameObject {
	private static final float CLIMABLE = 1.5f;
	private static final float SPEEDSCALER = 10;
	private Vector3f target;
	private float speed;
	private Vector3f upward;
	Camera camera;
	Vector3f displacement = new Vector3f(0, 0, 0);
	Vector3f[] destination = new Vector3f[4];

	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed() * SPEEDSCALER;
		upward = new Vector3f(0, 0, speed);
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 10);
		this.camera = camera;
	}

	public void slaveCamera(Camera camera) {
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 10);
	}

	public void update() {
<<<<<<< HEAD
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
=======
		camera.pos = position;
>>>>>>> master
	}

	public void movePlayer(String dir) {
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
		move();

	}

	private void move() {
		boolean canMove = true;
		boolean noClip = true;
		int chunkX = Math.round(position.x / 2 / Chunk.SIZE);
		int chunkY = Math.round(position.y / 2 / Chunk.SIZE);
		Chunk location = new Chunk(World.noise, chunkX, chunkY);
		//location.getBiome(position);
		if (!noClip) {

			this.destination[0] = position.add(displacement.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(displacement.scale(5 / SPEEDSCALER).negate());
			this.destination[2] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			destination[0].z = location.getHeight(destination[0].x, destination[0].y) + 1f;
			destination[1].z = location.getHeight(destination[1].x, destination[1].y) + 1f;
			destination[2].z = location.getHeight(destination[2].x, destination[2].y) + 1f;
			destination[3].z = location.getHeight(destination[3].x, destination[3].y) + 1f;
			float rise = Math.max(Math.max(destination[0].z, destination[1].z),
					Math.max(destination[2].z, destination[3].z));
			rise = rise - position.z;

			if (rise > CLIMABLE) {
				canMove = false;// this part works on at least one side
			} else if (rise < -CLIMABLE) {
				displacement = displacement.add(upward.negate());
			} else {
				float[] diff = { position.z - destination[0].z,position.z - destination[1].z };
				float difference = Math.min(diff[0], diff[1]) * 0.1f;
				displacement = displacement.add(new Vector3f(0, 0, -difference));
			}
		}
		if (canMove) {
			this.translate(displacement);
			camera.move(displacement);
		}
	}

}
