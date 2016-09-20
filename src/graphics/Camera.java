package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class Camera {

	private Vector3f pos, target, up;
	private double pitch, yaw, roll;
	private Matrix4f projection;
	private Matrix4f view;
	public Matrix4f pv;

	private Vector3f upward;
	private Vector3f backward;
	private Vector3f left;
	Vector3f cameraFront = new Vector3f(0,0,-1);
	Vector3f cameraUp = new Vector3f(0,0,1);
	private float speed = .2f;
	private float sense = .05f;
	private double[] lastMouse  = new double[2];
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
	public Camera(Vector3f cameraPos, Vector3f cameraTarget, Vector3f cameraUp, float angle, float aspect, float near,
			float far) {
		pos = cameraPos;
		target = cameraTarget;
		up = cameraUp;
		projection = Matrix4f.perspective(angle, aspect, near, far);
		view = Matrix4f.gluLookAt(pos, target, up);
		pv = projection.multiply(view);
		upward = new Vector3f(0, 0, speed);
		backward = new Vector3f(0, speed, 0);
		left = new Vector3f(-speed, 0, 0);
	}

	/**
	 * Moves the camera and target
	 * Deprecated
	 * 
	 * @param displacement
	 *            displacement vector
	 */
	private void move(Vector3f displacement) {
		pos = pos.add(displacement);
		target = target.add(cameraFront);
		view = Matrix4f.gluLookAt(pos, cameraFront.add(pos), up);
		pv = projection.multiply(view);
		//pv = pv.multiply(Matrix4f.translate(displacement.x, displacement.y, displacement.z));
	}

	/**
	 * Moves the camera and target
	 * @param dir
	 * 			which way the player has told to move
	 */
	public void moveCamera(String dir) {
		Vector3f displacement = new Vector3f(0, 0, 0);
		float vx = pos.x- target.x;
		float vy = pos.y - target.y;
		vx*=speed;
		vy*=speed;
		switch (dir) {
		case "UP":
			displacement = upward;
			break;
		case "DOWN":
			displacement = upward.negate();
			break;
		case "FORWARD":
			//displacement = new Vector3f(-vx,-vy,0);//backward.negate();
			displacement = cameraFront.normalize().scale(speed);
			break;
		case "BACK":
			//displacement = new Vector3f(vx,vy,0);//backward;
			displacement = cameraFront.normalize().scale(-speed);
			break;
		case "LEFT":
			//displacement = new Vector3f(-vy,vx,0);//left;
			displacement = cameraFront.cross(cameraUp).normalize().scale(speed);
			break;
		case "RIGHT":
			//displacement = new Vector3f(vy,-vx,0);//left.negate();
			displacement = cameraFront.cross(cameraUp).normalize().scale(-speed);
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
	 * 				location of mouse on screen, given by mouseInput
	 */
	public void rotateCamera(double[] mousePos) {
		//float mouseX = (float) ((1920 / 2 - mousePos[0]) / 1920 * 2);
		//float mouseY= (float) ((1080 / 2 - mousePos[1]) / 1080 * 2);
		 if(firstMouse)
		    {
			 lastMouse[0] = mousePos[0];
			 lastMouse[1] = mousePos[1];
		        firstMouse = false;
		    }
		
		double xOff= mousePos[0]-lastMouse[0];
		double yOff= lastMouse[1]-mousePos[1];
		lastMouse[0]= mousePos[0];
		lastMouse[1] = mousePos[1];
		xOff *= sense;
		yOff *= sense;
		yaw+= xOff;
		pitch+= yOff;
		if (pitch > 89) {
			pitch = 89;
		}
		if (pitch < -89) {
			pitch = -89;
		}

		//gets stuck at target.z = 2, 4
		//target.x = (float) (Math.cos(pitch)*Math.cos(yaw))+ pos.x; 
		//target.y = (float) (Math.sin(pitch))+ pos.y;
		//target.z = (float) (Math.cos(pitch)*Math.cos(yaw)) + pos.z;
		Vector3f front = new Vector3f((float) (Math.cos(pitch)*Math.cos(yaw)),(float) (Math.sin(pitch)),(float) (Math.cos(pitch)*Math.cos(yaw)));
		front.normalize();
		cameraFront = front;
		view = Matrix4f.gluLookAt(pos, cameraFront.add(pos), up);
		pv = projection.multiply(view);
		//ShaderManager.setCamera(view, pos);
		//frust.updateMatrix(projection.multiply(view));
		
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

	//public Frustum getFrustum() {
	//return frust;
	//}
}
