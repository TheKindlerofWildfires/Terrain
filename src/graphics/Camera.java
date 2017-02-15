package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class Camera {

	public Vector3f pos;
	public Vector3f target;

	private Vector3f up;
	private boolean flipped = false;

	private double degX, degZ;

	public Matrix4f projection;
	public Matrix4f view;
	public Matrix4f pv;

	private float lx, ly;

	private Vector3f upward;

	Vector3f cameraUp = new Vector3f(0, 0, 1);

	private float speed = .4f;
	private float sense = 1f;

	public float near;
	public float far;

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
	public Camera(Vector3f cameraPos, Vector3f cameraTarget, Vector3f cameraUp, float angle, float aspect, float near,
			float far, float speed, float sensitivity) {
		pos = cameraPos;
		target = cameraTarget;
		up = cameraUp;
		projection = Matrix4f.perspective(angle, aspect, near, far);
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
		pv = projection.multiply(view);
		upward = new Vector3f(0, 0, speed);
		this.speed = speed;
		this.sense = sensitivity;
		this.near = near;
		this.far = far;
	}

	/**
	 * Moves the camera and target Deprecated
	 * 
	 * @param displacement
	 *            displacement vector
	 */
	public void moveCamera(Vector3f displacement) {
		pos = pos.add(displacement);
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
		pv = projection.multiply(view);
	}

	public void move(Vector3f displacement) {
		pos = pos.add(displacement);
		target = target.add(displacement);
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
		pv = projection.multiply(view);
		Window.water.translate(displacement.x, displacement.y, 0);
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
		displacement = displacement.normalize().scale(speed);
		// displacement.scale(speed*0.1f);
		move(displacement);
	}

	/**
	 * Rotates field of view, only works on xy plane
	 * 
	 * @param mousePos
	 *            location of mouse on screen, given by mouseInput
	 */
	public void rotateCamera(double[] mousePos) {
		double dx = Window.deltaX;
		double dy = Window.deltaY;
		float mouseX = (float) (dx) / -Window.WINDOW_WIDTH * 2;
		float mouseY = (float) (dy) / -Window.WINDOW_HEIGHT * 2;
		if (!(mouseX == lx)) {
			degX += mouseX;
			lx = mouseX;
		}
		if (!(mouseY == ly)) {
			degZ += mouseY;
			ly = mouseY;
		}
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
		/*
		 * target.x = x * h + pos.x; target.y = y * h + pos.y; target.z = z +
		 * pos.z;
		 */
		target.x = x * h;
		target.y = y * h;
		target.z = z;
		target.normalize();
		target.x += pos.x;
		target.y += pos.y;
		target.z += pos.z;
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
		pv = projection.multiply(view);
	}

	public void moveTarget(Vector3f displacement) {
		target = target.add(displacement);
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
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

	public void flipCamera() {
		up = up.negate();
		flipped = !flipped;
		view = Matrix4f.gluLookAt(pos, target, up, flipped);
	}

	public float getSpeed() {
		return speed;
	}
	public void setTarget(Vector3f target){
		this.target = target;
	}
}
