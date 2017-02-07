package tunnel;

import java.util.ArrayList;
import java.util.Random;

import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Walk {
	public ArrayList<GameObject> solids = new ArrayList<GameObject>();

	// The order is left, right, forward, backs,up, down, exists
	// 1 is open
	int xSize = 30;
	int ySize = 30;
	int zSize = 30;

	Random rng = new Random();
	public ArrayList<Vector3f> blocks = new ArrayList<Vector3f>();
	Vector3f head = new Vector3f(0, 0, 0);
	Vector3f vel = new Vector3f(0, 0, 0);
	// public byte[][][] tunnelArray = new byte[xSize][ySize][zSize];

	public static byte EXISTS = 0b00000001;

	public static byte DOWN = 0b00000010;
	public static byte UP = 0b00000100;
	public static byte BACK = 0b00001000;
	public static byte FRONT = 0b00010000;
	public static byte RIGHT = 0b00100000;
	public static byte LEFT = 0b01000000;
	static Vector3f down = new Vector3f(0, 0, -1);
	static Vector3f up = new Vector3f(0, 0, 1);
	static Vector3f back = new Vector3f(0, -1, 0);
	static Vector3f front = new Vector3f(0, 1, 0);
	static Vector3f right = new Vector3f(1, 0, 0);
	static Vector3f left = new Vector3f(-1, 0, 0);
	int stepCount = 2500;
	int maxDist = 250;

	public Walk() {
		blocks.add(head);
		simple();
		for (int i = 0; i < blocks.size(); i++) {
			Vector3f e = blocks.get(i);
			System.out.println(e);
			GameObject object = new GameObject("resources/models/box.obj", "none", true);
			object.placeAt(e.x * 2, e.y * 2, e.z * 2); // too ez for rtz
			solids.add(object);
		}
	}

	public void update() {

	}

	public void render(Vector4f clipPlane) {
		solids.stream().forEach(c -> c.render(clipPlane));
	}

	/**
	 * roughly a cube, perfect boxing
	 */
	public void step() {
		for (int i = 0; i < stepCount; i++) {
			int dir = rng.nextInt(6);
			switch (dir) {
			case 0:
				head = head.add(down);
				break;
			case 1:
				head = head.add(up);
				break;
			case 2:
				head = head.add(left);
				break;
			case 3:
				head = head.add(right);
				break;
			case 4:
				head = head.add(front);
				break;
			case 5:
				head = head.add(back);
				break;
			}
			if (!blocks.contains(head)) {
				blocks.add(head);
			} else {
				i--;
			}
		}
	}

	/**
	 * roughly a dense network, not boxing
	 */
	public void lattice() {
		for (int i = 0; i < stepCount; i++) {
			int dir = rng.nextInt(7);
			switch (dir) {
			case 0:
				vel = vel.add(down);
				break;
			case 1:
				vel = vel.add(up);
				break;
			case 2:
				vel = vel.add(left);
				break;
			case 3:
				vel = vel.add(right);
				break;
			case 4:
				vel = vel.add(front);
				break;
			case 5:
				vel = vel.add(back);
				break;
			case 6:
				vel = vel.normalize();
				break;
			}
			if (head.lengthSquared() > maxDist) {
				vel = vel.negate();
			}
			head = head.add(vel.normalize());
			blocks.add(head);

		}
	}

	/**
	 * Fucks off, but in a good way! non boxing
	 */
	public void snake() {
		for (int i = 0; i < stepCount; i++) {
			int dir = rng.nextInt(7);
			switch (dir) {
			case 0:
				vel = vel.add(down);
				break;
			case 1:
				vel = vel.add(up);
				break;
			case 2:
				vel = vel.add(left);
				break;
			case 3:
				vel = vel.add(right);
				break;
			case 4:
				vel = vel.add(front);
				break;
			case 5:
				vel = vel.add(back);
				break;
			case 6:
				vel = vel.normalize();
				break;
			}
			head = head.add(vel.normalize());
			blocks.add(head);
		}
	}

	/**
	 * Death star meets octopus, non boxing
	 */
	public void octopus() {
		for (int i = 0; i < stepCount; i++) {
			int dir = rng.nextInt(7);
			switch (dir) {
			case 0:
				vel = vel.add(down);
				break;
			case 1:
				vel = vel.add(up);
				break;
			case 2:
				vel = vel.add(left);
				break;
			case 3:
				vel = vel.add(right);
				break;
			case 4:
				vel = vel.add(front);
				break;
			case 5:
				vel = vel.add(back);
				break;
			case 6:
				vel = vel.normalize();
				break;
			}
			if (head.lengthSquared() > maxDist) {
				head = head.normalize();
			}
			head = head.add(vel.normalize());
			blocks.add(head);

		}
	}

	/**
	 * A shaped charge, boxing
	 */
	public void antHill() {
		for (int i = 0; i < stepCount; i++) {
			float dir = rng.nextFloat();
			if (dir < 0.19) {
				head = head.add(down);
			} else if (dir < 0.36) {
				head = head.add(up);
			} else if (dir < 0.52) {
				head = head.add(back);
			} else if (dir < 0.68) {
				head = head.add(front);
			} else if (dir < 0.84) {
				head = head.add(right);
			} else {
				head = head.add(left);
			}
			if (!blocks.contains(head)) {
				blocks.add(head);
			} else {
				i--;
			}
		}
	}

	/**
	 * A cave system designed to be easy to travel, boxing
	 */
	public void simple() {
		for (int i = 0; i < stepCount; i++) {
			boolean shift = false;
			float dir = rng.nextFloat();
			if (dir < 0.1) {
				head = head.add(down);
				shift = true;
			} else if (dir < 0.2) {
				head = head.add(up);
				shift = true;
			} else if (dir < 0.4) {
				head = head.add(back);
			} else if (dir < 0.6) {
				head = head.add(front);
			} else if (dir < 0.8) {
				head = head.add(right);
			} else {
				head = head.add(left);
			}
			if (!blocks.contains(head)) {
				blocks.add(head);
			} else {
				i--;
			}
			if (shift) {
				i++;
				dir = rng.nextFloat();
				if (dir < 0.25) {
					head = head.add(back);

				} else if (dir < 0.5) {
					head = head.add(front);

				} else if (dir < 0.75) {
					head = head.add(right);

				} else {
					head = head.add(left);

				}
				if (!blocks.contains(head)) {
					blocks.add(head);
				}
			}
		}
	}
}
