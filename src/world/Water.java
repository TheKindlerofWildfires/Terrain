package world;

import static graphics.Shader.setUniform1f;
import static graphics.Shader.setUniformMatrix4f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import graphics.ShaderManager;
import graphics.Texture;
import graphics.Window;
import maths.Matrix4f;
import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Water extends GameObject {

	public static float WAVE_SPEED;
	public static float WAVE_STRENGTH;
	public static float NORMAL_STRENGTH;
	public static float FRESNEL_POWER;
	public static float WATER_CLARITY;
	public static Vector3f WATER_COLOUR;
	public static float MAX_DISTORTION;
	public static float REFLECTANCE;

	public static String DUDV_PATH;
	public static String NORMAL_PATH;

	private float moveFactor = 0;

	private Texture dudv;
	private Texture normal;

	public Water() {
		super("resources/models/plane.obj", "none", true);
		translate(0, 0, Chunk.WATERLEVEL);
		rotate(90, 1, 0, 0);
		scale(100, 100, 100);
		shader = ShaderManager.waterShader;
		hasMaterial = false;
		dudv = new Texture(DUDV_PATH);
		normal = new Texture(NORMAL_PATH);
	}

	@Override
	protected void renderPrep(Vector4f clipPlane) {
		moveFactor += WAVE_SPEED;
		setUniform1f("moveFactor", moveFactor);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Window.reflection.getTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, Window.refraction.getTexture());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, dudv.getId());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, normal.getId());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, Window.refraction.getDepthTexture());
		// System.out.println(Window.refraction.getDepthTexture());

		Matrix4f view = graphics.GraphicsManager.camera.view;
		setUniformMatrix4f("modelView", view.multiply(model.getMatrix()));
	}
}
