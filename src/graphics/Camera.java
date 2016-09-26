package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class Camera {

	private Vector3f pos, target, up;
	private double degX, degZ;
	private Matrix4f projection;
	private Matrix4f view;
	public Matrix4f pv;
	float lx, ly;
	private Vector3f upward;
	Vector3f cameraUp = new Vector3f(0, 0, 1);
	private float speed = .2f;
	private float sense = 1f;
	boolean firstMouse = true;

	/**
	 * Initializes camera
	 * 
	 * @param cameraPos
	 *            position of camera
	 * @param cameraTarget
	 *            location camera is looking at
	 * @param cameraUp
	 *            up vector
	 */
	public Camera(Vector3f cameraPos, Vector3f cameraTarget, Vector3f cameraUp,
			float angle, float aspect, float near, float far) {
		pos = cameraPos;
		target = cameraTarget;
		up = cameraUp;
		projection = Matrix4f.perspective(angle, aspect, near, far);
		view = Matrix4f.gluLookAt(pos, target, up);
		pv = projection.multiply(view);
		upward = new Vector3f(0, 0, speed);
		// backward = new Vector3f(0, speed, 0);
		// left = new Vector3f(-speed, 0, 0);
	}

	/**
	 * Moves the camera and target Deprecated
	 * 
	 * @param displacement
	 *            displacement vector
	 */
	private void move(Vector3f displacement) {
		pos = pos.add(displacement);
		target = target.add(displacement);
		view = Matrix4f.gluLookAt(pos, target, up);
		pv = projection.multiply(view);
		// pv = pv.multiply(Matrix4f.translate(displacement.x, displacement.y,
		// displacement.z));
	}

	/**
	 * Moves the camera and target
	 * 
	 * @param dir
	 *            which way the player has told to move
	 */
	public void moveCamera(String dir) {
		Vector3f displacement = new Vector3f(0, 0, 0);
		float vx = pos.x - target.x;
		float vy = pos.y - target.y;
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
		move(displacement);
	}

	/**
	 * Rotates field of view, only works on xy plane
	 * 
	 * @param mousePos
	 *            location of mouse on screen, given by mouseInput
	 */
	public void rotateCamera(double[] mousePos) {
		// float mouseX = (float) ((1920 / 2 - mousePos[0]) / 1920 * 2);
		// float mouseY = (float) ((1080 / 2 - mousePos[1]) / 1080 * 2);
		double dx = Window.deltaX;
		double dy = Window.deltaY;
		float mouseX = (float) (dx) / -1920f * 2;
		float mouseY = (float) (dy) / -1080f * 2;
		// System.out.println(mouseX + "" + lx);
		if (!(mouseX == lx)) {
			degX += mouseX;
			lx = mouseX;
		}
		if (!(mouseY == ly)) {
			degZ += mouseY;
			ly = mouseY;
		}

		// degZ += mouseY;
		degX = degX % (2 * Math.PI);
		if (degZ > 1.57 * sense) {
			degZ = 1.57 * sense;
		} else if (degZ < -1.57 * sense) {
			degZ = -1.57 * sense;
		}

		float x = (float) Math.cos(degX * sense);
		float y = (float) Math.sin(degX * sense);
		float z = (float) Math.sin(degZ * sense);
		float h = (float) Math.cos(degZ * sense);
		// gets stuck at target.z = 2, 4
		target.x = x * h + pos.x; // should be in ratio
		target.y = y * h + pos.y;
		target.z = z + pos.z;

		view = Matrix4f.gluLookAt(pos, target, up);
		pv = projection.multiply(view);

	}

	public Vector3f getPos() {
		return pos;
	}

	public Vector3f getTarget() {
		return target;
	}

	public Vector3f getUp() {
		return up;
	}

	// public Frustum getFrustum() {
	// return frust;
	// }
}
