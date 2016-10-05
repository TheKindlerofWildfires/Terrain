package graphics;

import static org.lwjgl.glfw.GLFW.*;
import input.KeyboardInput;
import input.MouseInput;
import maths.Vector3f;

class Attenuation {
	float constant;
	float linear;
	float exponent;
}

class PointLight {
	Vector3f colour;
	Vector3f position;
	float intensity;
	Attenuation att;
}

class DirectionalLight {
	Vector3f colour;
	Vector3f direction;
	float intensity;
}

public class GraphicsManager {

	/**
	 * Inits camera contents
	 */
	private static final Vector3f cameraStartPos = new Vector3f(5, 0, 10);// usual
																			// 0,2,0
	private static final Vector3f cameraStartTarget = new Vector3f(3, 0, 15);
	private static final Vector3f up = new Vector3f(0, 0, 1);
	private static final float fov = 45f;
	private static final float aspect = 16f / 9;
	private static final float near = 0.1f;
	private static final float far = 300f;

	public static Camera camera;

	public GraphicsManager() {
		ShaderManager.init();
		camera = new Camera(cameraStartPos, cameraStartTarget, up, fov, aspect, near, far);
		ShaderManager.setCamera(camera);
	}

	/**
	 * Move this to player control
	 */
	public void update() {

		double[] mousePos = MouseInput.pos();
		camera.rotateCamera(mousePos);
		ShaderManager.setCamera(camera);
		if (KeyboardInput.isKeyDown(GLFW_KEY_D)) {
			camera.moveCamera("LEFT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_A)) {
			camera.moveCamera("RIGHT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_W)) {
			camera.moveCamera("FORWARD");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_S)) {
			camera.moveCamera("BACK");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_SPACE)) {
			camera.moveCamera("UP");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			camera.moveCamera("DOWN");
		}
		
	}
}
