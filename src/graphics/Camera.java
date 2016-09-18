package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class Camera {

	private Vector3f pos, target, up;
	private double degX, degZ;
	private double sense = 0.1;
	private Matrix4f projection;
	private Matrix4f view;
	public Matrix4f pv;

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
	}

	/**
	 * Moves the camera and target
	 * Depricated
	 * 
	 * @param displacement
	 *            displacement vector
	 */
	@Deprecated
	public void move(Vector3f displacement) {
		pos = pos.add(displacement);
		target = target.add(displacement);
		pv = pv.multiply(Matrix4f.translate(displacement.x, displacement.y, displacement.z));
	}
	/**
	 * Moves the camera and target
	 * @param dir
	 * 			which way the player has told to move
	 */
	public void moveCamera(String dir) {
		float vx = pos.x- target.x;
		float vy = pos.y - target.y;
		//float vz = pos.z - target.z;
		Vector3f displacement = new Vector3f(0,0,0);
		if(dir=="UP"){
			displacement = new Vector3f(0,0,1);
		}else if(dir=="DOWN"){
			displacement = new Vector3f(0,0,-1);
		}else if(dir == "FORWARD"){
			displacement = new Vector3f(-vx,-vy,0);
		}else if(dir == "BACK"){
			displacement = new Vector3f(vx,vy,0);
		}else if(dir == "LEFT"){
			displacement = new Vector3f(-vy,vx,0);
		}else if(dir == "RIGHT"){
			displacement = new Vector3f(vy,-vx,0);
		}
		//System.out.println(displacement);
		pos = pos.add(displacement);
		target = target.add(displacement);
		//view = Matrix4f.gluLookAt(pos, target, up);
		pv = pv.multiply(Matrix4f.translate(displacement.x, displacement.y, displacement.z));
		//ShaderManager.setCamera(view, pos);
		//frust.updateMatrix(projection.multiply(view));
	}
	/**
	 * Rotates field of view, only works on xy plane
	 * 
	 * @param mousePos
	 * 				location of mouse on screen, given by mouseInput
	 */
	public void rotateCamera(double[] mousePos) {
		if (degX>360){
			degX -=360;
		}
		if (degZ>360){
			degZ -=360;
		}
		float mouseX = (float) ((1920/2-mousePos[0])/1920*2); 
		float mouseY = (float) ((1080/2-mousePos[1])/1080*2); 
		degX += mouseX;
		degZ += mouseY;

		float x = (float) Math.cos(degX*sense);
		float y = (float) Math.sin(degX*sense);
		
		//float xz = (float) Math.cos(degZ*sense);
		//float z= (float) Math.sin(degZ*sense);
		
		target.x = x +pos.x;
		target.y = y +pos.y;

		//Matrix4f view = Matrix4f.gluLookAt(pos, target, up);
		pv = pv.multiply(Matrix4f.translate(x, y, 0));
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
