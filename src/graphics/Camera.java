package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class Camera {

	private Vector3f pos, target, up;

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
	 * 
	 * @param displacement
	 *            displacement vector
	 */
	public void move(Vector3f displacement) {
		pos = pos.add(displacement);
		target = target.add(displacement);
		pv = pv.multiply(Matrix4f.translate(displacement.x, displacement.y, displacement.z));
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
}
