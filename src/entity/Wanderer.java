package entity;

import java.util.Random;

import graphics.Window;
import maths.Vector3f;
import object.GameObject;
import physics.Time;
import world.Biome;

/**
 * @author TheKingInYellow
 */
public class Wanderer extends GameObject {
	private static final float CLIMABLE = 10f;
	private static final float speed = .2f;
	private static final int SPEEDSCALER = 100;
	Vector3f upward = new Vector3f(0, 0, speed);
	Random random = Window.entityRandom;
	private Vector3f target;
	private Vector3f displacement;
	private Vector3f[] destination = new Vector3f[5];
	int lastMove;
	int lastChoice;
	int dy, dx = 0;
	String direction = "FORWARD";

	public Wanderer(String model) {
		super(model, "none", true);
		lastMove = Time.getSecTick();
		target = this.position.add(new Vector3f(1, 0, 0));
	}

	public void update() {
		if (lastChoice - Time.getUpdateTick() < -random.nextInt(120)) {
			direct(-1);
			lastChoice = Time.getUpdateTick();
		}
		if (lastMove - Time.getUpdateTick() < -0) {

			displacement = new Vector3f(dx, dy, 0).normalize().scale(speed);
			move();
			lastMove = Time.getUpdateTick();
		}
	}

	public void direct(int dir) {
		direction = "FORWARD";
		int r;
		if (dir == -1) {
			r = random.nextInt(4);
		} else {
			r = dir;
		}
		if (r == 0) {
			direction = "LEFT";
			dy--;
		} else if (r == 1) {
			direction = "BACK";
			dx--;
		} else if (r == 2) {
			direction = "RIGHT";
			dy++;
		} else if (r == 3) {
			direction = "FORWARD";
			dx++;
		} else {
			dy = 0;
			dx = 0;
			direction = "STOP";
		}

	}

	private void move() {
		boolean canMove = true;
		boolean noClip = false;
		if (!noClip) {
			this.destination[0] = position.add(displacement.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(displacement.scale(5 / SPEEDSCALER).negate());

			this.destination[2] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			destination[0].z = Biome.getPlanet(destination[0], destination[0])[3] + 1f;
			destination[1].z = Biome.getPlanet(destination[1], destination[1])[3] + 1f;
			destination[2].z = Biome.getPlanet(destination[2], destination[2])[3] + 1f;
			destination[3].z = Biome.getPlanet(destination[3], destination[3])[3] + 1f;
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
			this.target.add(displacement);
		} else {
			dx = dy = 0;
		}
	}
}
