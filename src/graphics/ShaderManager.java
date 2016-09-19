package graphics;

import maths.Matrix4f;
import maths.Vector3f;

public class ShaderManager {

	private static boolean initialized = false;

	public static Shader landShader;

	public static void init() {
		initialized = true;
		landShader = new Shader("src/shaders/land.vert", "src/shaders/land.frag");
	}

	public static void setCamera(Camera camera) {
		assert initialized : "Shaders must be initialized in order to work";
	
		landShader.start();
		landShader.setUniformMatrix4f("pv", camera.pv);
		//landShader.setUniform3f("cameraPos", camera.getPos());
		landShader.stop();
	}
	@Deprecated
	public static void setCamera(Matrix4f view, Vector3f cameraPos) {
		landShader.start();
		landShader.setUniformMatrix4f("view", view);
		//this throws an error
		//landShader.setUniform3f("cameraPos", cameraPos);
		landShader.stop();
	}
	public static void setLight(Vector3f lightPos) {
		assert initialized : "Shaders must be initialized in order to work";
		landShader.start();
		//landShader.setUniform3f("lightPos", lightPos);
		landShader.stop();
	}
}
