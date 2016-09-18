package graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import input.KeyboardInput;
import input.MouseInput;
import maths.Vector3f;

public class GraphicsManager {

	// Camera init constants
	private static final Vector3f cameraStartPos = new Vector3f(1, 0, 30f);
	private static final Vector3f cameraStartTarget = new Vector3f(0, 0, 0);
	private static final Vector3f up = new Vector3f(0, 0, 1);
	private static final float fov = 45f;
	private static final float aspect = 16f / 9;
	private static final float near = 0.1f;
	private static final float far = 100f;

	// Light init constants
	private static final Vector3f lightStartPos = new Vector3f(0, 0, 1);

	Camera camera;
	private Vector3f lightPos;

	public GraphicsManager() {
		ShaderManager.init();
		camera = new Camera(cameraStartPos, cameraStartTarget, up, fov, aspect,
				near, far);
		ShaderManager.setCamera(camera);
		lightPos = lightStartPos;
		ShaderManager.setLight(lightPos);
		
	}
	@Deprecated
	public void moveCamera(Vector3f displacement) {
		camera.move(displacement);
		ShaderManager.setCamera(camera);
	}
	/**
	 * For some odd reason the camera is in the graphics manager and the player needs to control the camera
	 */
	public void update() {
		double[] mousePos = MouseInput.pos();
		camera.rotateCamera(mousePos);
		//ShaderManager.setCamera(camera);
		if (KeyboardInput.isKeyDown(GLFW_KEY_D)) {
			camera.moveCamera("LEFT");
			ShaderManager.setCamera(camera);
			System.out.println("left");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_A)) {
			camera.moveCamera("RIGHT");
			ShaderManager.setCamera(camera);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_W)) {
			camera.moveCamera("FORWARD");
			ShaderManager.setCamera(camera);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_S)) {
			camera.moveCamera("BACK");
			ShaderManager.setCamera(camera);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_SPACE)) {
			camera.moveCamera("UP");
			ShaderManager.setCamera(camera);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			camera.moveCamera("DOWN");
			ShaderManager.setCamera(camera);
		}
	}
}
