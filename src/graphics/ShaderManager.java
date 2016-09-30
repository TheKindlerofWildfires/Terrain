package graphics;

import maths.Vector3f;
import maths.Vector4f;

public class ShaderManager {

	private static boolean initialized = false;

	public static Shader objectShader;
	public static Shader landShader;
	public static Shader skyboxShader;

	static Attenuation atten;
	static PointLight light;
	static Material matt;
	static DirectionalLight dirLight;
	static float lightAngle = 0;

	public static void init() {
		atten = new Attenuation();
		light = new PointLight();
		dirLight = new DirectionalLight();
		atten.constant = 0f;
		atten.exponent = .1f;
		atten.linear = .1f;
		light.att = atten;
		light.colour = new Vector3f(1, 1, 1);
		light.intensity = 1f;
		light.position = new Vector3f(0, 0, 5);
		dirLight.colour = new Vector3f(1, 1, 1);
		dirLight.direction = new Vector3f(-10, 0, 5);
		dirLight.intensity = .8f;

		matt = new Material();
		matt.colour = new Vector3f(.5f, 1, .4f);
		matt.useColour = 1;
		matt.reflectance = .05f;

		initialized = true;
		landShader = new Shader("src/shaders/land.vert", "src/shaders/land.frag");
		objectShader = new Shader("src/shaders/object.vert", "src/shaders/object.frag");
		skyboxShader = new Shader("src/shaders/skybox.vert", "src/shaders/skybox.frag");

		objectShader.start();
		objectShader.setPointLight("pointLight", light);
		objectShader.setMaterial("material", matt);
		objectShader.setUniform1f("specularPower", 1);
		objectShader.setUniform3f("ambientLight", new Vector3f(.1f, .1f, .1f));
		objectShader.stop();

		landShader.start();
		landShader.setPointLight("pointLight", light);
		landShader.setDirectionalLight("directionalLight", dirLight);
		landShader.setUniform1f("specularPower", 1);
		landShader.setUniform1f("material.reflectance", 0);
		landShader.setUniform3f("ambientLight", new Vector3f(.3f, .3f, .3f));
		landShader.stop();
	}

	public static void setCamera(Camera camera) {
		assert initialized : "Shaders must be initialized in order to work";

		landShader.start();
		landShader.setUniformMatrix4f("projection", camera.projection);
		landShader.setUniformMatrix4f("modelView", camera.view);
		Vector4f pos = camera.view.multiply(new Vector4f(light.position.x, light.position.y, light.position.z, 1));
		landShader.setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		Vector4f dir = camera.view
				.multiply(new Vector4f(dirLight.direction.x, dirLight.direction.y, dirLight.direction.z, 0));
		landShader.setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		landShader.stop();

		objectShader.start();
		objectShader.setUniformMatrix4f("projection", camera.projection);
		objectShader.setUniformMatrix4f("modelView", camera.view);
		objectShader.setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		objectShader.stop();
		// landShader.setUniform3f("cameraPos", camera.getPos());

		lightAngle += .01f;
		dirLight.direction.y = (float) Math.cos(lightAngle);
		dirLight.direction.z = (float) Math.sin(lightAngle);
		if (lightAngle >= 360) {
			lightAngle = 0;
		}
		if (lightAngle >= 80 && lightAngle <= 280) {
			float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
			dirLight.intensity = factor;
			dirLight.colour.y = Math.max(factor, 0.9f);
			dirLight.colour.z = Math.max(factor, 0.5f);
		} else {
			dirLight.intensity = 1;
			dirLight.colour.x = 1;
			dirLight.colour.y = 1;
			dirLight.colour.z = 1;
		}

	}
}
