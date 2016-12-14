package graphics;

import static graphics.Shader.setFog;
import static graphics.Shader.setUniform3f;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static graphics.Shader.*;

import maths.Vector3f;
import maths.Vector4f;
import world.Water;

public class ShaderManager {

	private static boolean initialized = false;

	public static int objectShader;
	public static int landShader;
	public static int waterShader;
	public static int particleShader;
	//public static int skyboxShader;

	public static void init(Camera camera, DirectionalLight dirLight, Fog fog, Vector3f ambientLight) {
		initialized = true;
		landShader = makeShader("src/shaders/land.vert", "src/shaders/land.frag");
		objectShader = makeShader("src/shaders/object.vert", "src/shaders/object.frag");
		waterShader = makeShader("src/shaders/water.vert", "src/shaders/water.frag");
		particleShader = makeShader("src/shaders/particle.vert", "src/shaders/particle.frag");

		//skyboxShader = makeShader("src/shaders/skybox.vert", "src/shaders/skybox.frag");

		start(objectShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniform3f("ambientLight", ambientLight);
		setFog("fog", fog);
		setDirectionalLight("directionalLight", dirLight);

		start(landShader);
		setUniformMatrix4f("projection", camera.projection);
		setDirectionalLight("directionalLight", dirLight);

		setUniform3f("ambientLight", ambientLight);
		setFog("fog", fog);
		setUniform1f("material.reflectance", 4);
		setDirectionalLight("directionalLight", dirLight);

		start(waterShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniform1i("reflectionTexture", 0);
		setUniform1i("refractionTexture", 1);
		setUniform1i("dudvMap", 2);
		setUniform1i("normalMap", 3);
		setUniform1i("depthMap", 4);
		setFog("fog", fog);
		setUniform3f("ambientLight", ambientLight);
		setDirectionalLight("directionalLight", dirLight);
		setUniform1f("material.reflectance", Water.REFLECTANCE);
		setUniform1f("fresnelPower", Water.FRESNEL_POWER);
		setUniform1f("waveStrength", Water.WAVE_STRENGTH);
		setUniform1f("normalStrength", Water.NORMAL_STRENGTH);
		setUniform1f("near", GraphicsManager.camera.near);
		setUniform1f("far", GraphicsManager.camera.far);
		setUniform1f("waterClarity", Water.WATER_CLARITY);
		setUniform1f("maxDistortion", Water.MAX_DISTORTION);
		setUniform4f("waterColour", new Vector4f(Water.WATER_COLOUR, 1f));

		start(particleShader);
		setUniformMatrix4f("projection", GraphicsManager.camera.projection);
		stop();
	}

	public static void setCamera(Camera camera, DirectionalLight dirLight) {
		assert initialized : "Shaders must be initialized in order to work";

		Vector4f dir = camera.view
				.multiply(new Vector4f(dirLight.direction.x, dirLight.direction.y, dirLight.direction.z, 0))
				.normalize();

		start(landShader);
		setUniformMatrix4f("modelView", camera.view);
		setDirectionalLight("directionalLight", dirLight);
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		//setUniform3f("camera_pos", camera.pos);

		start(objectShader);
		setDirectionalLight("directionalLight", dirLight);
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		//setUniform3f("camera_pos", camera.pos);

		start(waterShader);
		setDirectionalLight("directionalLight", dirLight);
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		//setUniform3f("camera_pos", camera.pos);
		stop();
	}

	public static void updateFog(Fog fog) {
		start(objectShader);
		setFog("fog", fog);
		start(landShader);
		setFog("fog", fog);
		start(waterShader);
		setFog("fog", fog);
		stop();
	}
}
