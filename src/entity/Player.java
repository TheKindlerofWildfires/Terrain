package entity;

import graphics.Camera;
import maths.Vector3f;
import object.GameObject;
import world.Chunk;
import world.World;

public class Player extends GameObject {
	private static final float CLIMABLE = 1.5f;
	private Vector3f target;
	private float speed;
	private Vector3f upward;
	Camera camera;
	Vector3f displacement = new Vector3f(0, 0, 0);
	Vector3f destination;
	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed();
		upward = new Vector3f(0, 0, speed);
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1,1,1);
		//this.position = camera.getPos();
		
		
	}

	public void update() {
		camera.pos=position;
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
		int chunkX = Math.round(position.x / 2 / Chunk.SIZE);
		int chunkY = Math.round(position.y / 2 / Chunk.SIZE);
		
		//To avoid phasing through walls we need to check a multiple point 2d shape
		//and have height equal to the highest of its points
		this.destination = position.add(displacement.scale(10));
		
		Chunk location = new Chunk(World.noise, chunkX, chunkY);
		destination.z =location.getHeight(destination.x, destination.y)+1f;
		
		float rise = destination.z-position.z;
		
		if(rise>CLIMABLE){
			canMove = false;
		}else if(rise< -CLIMABLE){
			displacement = displacement.add(upward.negate());
		}else{
			float diff = position.z - destination.z;
			diff = 0.1f*diff;
			displacement = displacement.add(new Vector3f(0,0,-diff));
		}
		if(canMove){
			this.translate(displacement);
			camera.move(displacement);
		}
	}
}
