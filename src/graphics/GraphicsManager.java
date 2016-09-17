package graphics;

import maths.Vector3f;

public class GraphicsManager {

	//Camera init constants
	private static final Vector3f cameraStartPos = new Vector3f(2, 0, 5f);
	private static final Vector3f cameraStartTarget = new Vector3f(0, 0, 0);
	private static final Vector3f up = new Vector3f(0, 0, 1);
	private static final float fov = 45f;
	private static final float aspect = 16f / 9;
	private static final float near = 0.1f;
	private static final float far = 100f;

	//Light init constants
	private static final Vector3f lightStartPos = new Vector3f(0, 0, 15);

	private Camera camera;
	private Vector3f lightPos;

	public GraphicsManager() {
		ShaderManager.init();
		camera = new Camera(cameraStartPos, cameraStartTarget, up, fov, aspect, near, far);
		ShaderManager.setCamera(camera);
		lightPos = lightStartPos;
		ShaderManager.setLight(lightPos);
	}

	public void moveCamera(Vector3f displacement) {
		camera.move(displacement);
		ShaderManager.setCamera(camera);
	}
}
