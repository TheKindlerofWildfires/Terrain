package graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import input.KeyboardInput;
import input.MouseInput;
import maths.Vector3f;

class Attenuation {
	float constant;
	float linear;
	float exponent;

	Attenuation(float constant, float linear, float exponent) {
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}
}

class PointLight {
	Vector3f colour;
	Vector3f position;
	float intensity;
	Attenuation att;

	PointLight(Vector3f colour, Vector3f position, float intensity, Attenuation att) {
		this.colour = colour;
		this.position = position;
		this.intensity = intensity;
		this.att = att;
	}
}

class DirectionalLight {
	Vector3f colour;
	Vector3f direction;
	float intensity;

	DirectionalLight(Vector3f colour, Vector3f direction, float intensity) {
		this.colour = colour;
		this.direction = direction;
		this.intensity = intensity;
	}
}

class Fog {
	Vector3f colour;
	int active;
	float density;
	float exponent;

	Fog(Vector3f colour, int active, float density, float exponent) {
		this.colour = colour;
		this.active = active;
		this.density = density;
		this.exponent = exponent;
	}
}

public class GraphicsManager {

	public static Camera camera;

	private static DirectionalLight dirLight;
	private static float lightAngle = 0;
	private static float sunSpeed;
	private static Vector3f ambientLight;

	private static Fog fog;

	public static Properties props;

	private static void loadProperties() {
		props = new Properties();
		try {
			FileReader reader = new FileReader("resources/properties/graphics.properties");
			props.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Vector3f colour = Vector3f.parseVector(props.getProperty("directionalLightColour"));
		Vector3f direction = Vector3f.parseVector(props.getProperty("directionalLightDirection"));
		float intensity = Float.parseFloat(props.getProperty("directionalLightIntensity"));
		dirLight = new DirectionalLight(colour, direction, intensity);

		int active = Integer.parseInt(props.getProperty("fogActive"));
		colour = Vector3f.parseVector(props.getProperty("fogColour"));
		float density = Float.parseFloat(props.getProperty("fogDensity"));
		float exponent = Float.parseFloat(props.getProperty("fogExponent"));
		fog = new Fog(colour, active, density, exponent);

		Vector3f position = Vector3f.parseVector(props.getProperty("cameraStartPosition"));
		Vector3f target = Vector3f.parseVector(props.getProperty("cameraStartTarget"));
		Vector3f up = Vector3f.parseVector(props.getProperty("up"));
		float fov = Float.parseFloat(props.getProperty("fov"));
		float aspect = Float.parseFloat(props.getProperty("aspect"));
		float near = Float.parseFloat(props.getProperty("near"));
		float far = Float.parseFloat(props.getProperty("far"));
		camera = new Camera(position, target, up, fov, aspect, near, far);

		sunSpeed = Float.parseFloat(props.getProperty("sunSpeed"));

		ambientLight = Vector3f.parseVector(props.getProperty("ambientLight"));
	}

	public GraphicsManager() {
		loadProperties();
		ShaderManager.init(dirLight, fog, ambientLight);
		ShaderManager.setCamera(camera, dirLight);
	}

	/**
	 * Move this to player control
	 */
	public void update() {
		double[] mousePos = MouseInput.pos();
		camera.rotateCamera(mousePos);
		ShaderManager.setCamera(camera, dirLight);
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
		dayNightCycle();
	}

	private static void dayNightCycle() {
		lightAngle += sunSpeed;
		float angleRad = (float) Math.toRadians(lightAngle);
		float x = (float) Math.cos(angleRad);
		float z = (float) Math.sin(angleRad);
		dirLight.direction.x = x;
		dirLight.direction.z = z;

	}

	public static void toggleFog() {
		fog.active = fog.active == 1 ? 0 : 1;
		props.setProperty("fogActive", "" + fog.active);
		ShaderManager.updateFog(fog);
	}
}
