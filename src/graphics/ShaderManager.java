package graphics;

import maths.Vector3f;
import maths.Vector4f;
import static graphics.Shader.*;

public class ShaderManager {

	private static boolean initialized = false;

	public static int objectShader;
	public static int landShader;
	public static int skyboxShader;

	static Attenuation atten;
	static PointLight light;
	static Material matt;
	static DirectionalLight dirLight;
	static Fog fog;
	static float lightAngle = 45;

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

		fog = new Fog();
		fog.active = 1;
		fog.density = .05f;
		fog.colour = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());

		initialized = true;
		landShader = makeShader("src/shaders/land.vert", "src/shaders/land.frag");
		objectShader = makeShader("src/shaders/object.vert", "src/shaders/object.frag");
		skyboxShader = makeShader("src/shaders/skybox.vert", "src/shaders/skybox.frag");

		start(objectShader);
		setPointLight("pointLight", light);
		setMaterial("material", matt);
		setUniform1f("specularPower", 1);
		setUniform3f("ambientLight", new Vector3f(.1f, .1f, .1f));
		setFog("fog", fog);

		start(landShader);
		setPointLight("pointLight", light);
		setDirectionalLight("directionalLight", dirLight);
		setUniform1f("specularPower", 1);
		setUniform1f("material.reflectance", 0);
		setUniform3f("ambientLight", new Vector3f(.3f, .3f, .3f));
		setFog("fog", fog);
		stop();
	}

	public static void setCamera(Camera camera) {
		assert initialized : "Shaders must be initialized in order to work";

		start(landShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniformMatrix4f("modelView", camera.view);
		Vector4f pos = camera.view.multiply(new Vector4f(light.position.x, light.position.y, light.position.z, 1));
		setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		Vector4f dir = camera.view
				.multiply(new Vector4f(dirLight.direction.x, dirLight.direction.y, dirLight.direction.z, 0));
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));

		start(objectShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniformMatrix4f("modelView", camera.view);
		setUniform3f("pointLight.position", new Vector3f(pos.x, pos.y, pos.z));
		stop();
		// landShader.setUniform3f("cameraPos", camera.getPos());

		//lightAngle += .01f;
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
