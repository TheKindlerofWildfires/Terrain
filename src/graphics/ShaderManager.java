package graphics;

import maths.Matrix4f;
import maths.Vector3f;
import maths.Vector4f;

public class ShaderManager {

	private static boolean initialized = false;

	public static Shader objectShader;
	public static Shader landShader;

	static Attenuation atten;
	static PointLight light;
	static Material matt;

	public static void init() {
		atten = new Attenuation();
		light = new PointLight();
		atten.constant = 0f;
		atten.exponent = 0f;
		atten.linear = .5f;
		light.att = atten;
		light.colour = new Vector3f(1, 1, 1);
		light.intensity = 1f;
		light.position = new Vector3f(0, 0, 5);

		matt = new Material();
		matt.colour = new Vector3f(.5f, 1, .4f);
		matt.useColour = 1;
		matt.reflectance = .05f;

		initialized = true;
		landShader = new Shader("src/shaders/land.vert", "src/shaders/land.frag");
		objectShader = new Shader("src/shaders/object.vert", "src/shaders/object.frag");

		objectShader.start();
		objectShader.setPointLight("pointLight", light);
		objectShader.setMaterial("material", matt);
		objectShader.stop();

		landShader.start();
		landShader.setPointLight("pointLight", light);
		landShader.stop();
	}

	public static void setCamera(Camera camera) {
		assert initialized : "Shaders must be initialized in order to work";

		landShader.start();
		landShader.setUniformMatrix4f("projection", camera.projection);
		landShader.setUniformMatrix4f("modelView", camera.view);
		Vector4f pos = camera.view.multiply(new Vector4f(light.position.x, light.position.y, light.position.z, 1));
		landShader.setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		landShader.stop();

		objectShader.start();
		objectShader.setUniformMatrix4f("projection", camera.projection);
		objectShader.setUniformMatrix4f("modelView", camera.view);
		objectShader.setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		objectShader.stop();
		//landShader.setUniform3f("cameraPos", camera.getPos());
	}

	@Deprecated
	public static void setCamera(Matrix4f view, Vector3f cameraPos) {
		landShader.start();
		landShader.setUniformMatrix4f("view", view);
		landShader.stop();

		objectShader.start();
		objectShader.setUniformMatrix4f("view", view);
		objectShader.stop();

		//landShader.setUniform3f("cameraPos", cameraPos);
	}
}
