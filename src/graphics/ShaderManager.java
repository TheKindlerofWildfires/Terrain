package graphics;

import static graphics.Shader.setFog;
import static graphics.Shader.setUniform3f;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static graphics.Shader.*;

import maths.Vector3f;
import maths.Vector4f;

public class ShaderManager {

	private static boolean initialized = false;

	public static int objectShader;
	public static int landShader;
	public static int waterShader;
	//public static int skyboxShader;

	public static void init(DirectionalLight dirLight, Fog fog, Vector3f ambientLight) {
		initialized = true;
		landShader = makeShader("src/shaders/land.vert", "src/shaders/land.frag");
		objectShader = makeShader("src/shaders/object.vert", "src/shaders/object.frag");
		waterShader = makeShader("src/shaders/water.vert", "src/shaders/water.frag");
		//skyboxShader = makeShader("src/shaders/skybox.vert", "src/shaders/skybox.frag");

		start(objectShader);
		setUniform1f("specularPower", 1);
		setUniform3f("ambientLight", ambientLight);
		setFog("fog", fog);
		setDirectionalLight("directionalLight", dirLight);

		start(landShader);
		setDirectionalLight("directionalLight", dirLight);
		setUniform1f("specularPower", 1);
		setUniform1f("material.reflectance", 0);
		setUniform3f("ambientLight", ambientLight);
		setFog("fog", fog);
		setDirectionalLight("directionalLight", dirLight);

		start(waterShader);
		setUniform1i("reflectionTexture", 0);
		setUniform1i("refractionTexture", 1);
		setUniform1i("dudvMap", 2);
		setUniform1i("normalMap", 3);
		setFog("fog", fog);
		setUniform3f("ambientLight", ambientLight);
		stop();
	}

	public static void setCamera(Camera camera, DirectionalLight dirLight) {
		assert initialized : "Shaders must be initialized in order to work";

		//fog.colour.x+=.002f;
		//if(fog.colour.x>1){
		//	fog.colour.x=0;
		//}

		start(landShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniformMatrix4f("modelView", camera.view);
		Vector4f dir = camera.view
				.multiply(new Vector4f(dirLight.direction.x, dirLight.direction.y, dirLight.direction.z, 0))
				.normalize();
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		//setUniform3f("camera_pos", camera.pos);

		start(objectShader);
		setUniformMatrix4f("projection", camera.projection);
		dir = camera.view.multiply(new Vector4f(dirLight.direction.x, dirLight.direction.y, dirLight.direction.z, 0))
				.normalize();
		setUniform3f("directionalLight.direction", new Vector3f(dir.x, dir.y, dir.z));
		//setUniform3f("camera_pos", camera.pos);

		start(waterShader);
		setUniformMatrix4f("projection", camera.projection);
		setUniform3f("cameraPos", camera.pos);
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
