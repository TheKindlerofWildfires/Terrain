package world;

import static graphics.Shader.setUniform1f;
import static graphics.Shader.setUniformMatrix4f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import graphics.ShaderManager;
import graphics.Texture;
import graphics.Window;
import maths.Matrix4f;
import maths.Vector4f;
import object.GameObject;

public class Water extends GameObject {

	public static final float WAVE_SPEED = .00001f;

	private float moveFactor = 0;

	Texture dudv;
	Texture normal;

	public Water() {
		super("resources/models/plane.obj", "none", true);
		translate(0, 0, Chunk.WATERLEVEL);
		rotate(90, 1, 0, 0);
		scale(100, 100, 100);
		shader = ShaderManager.waterShader;
		hasMaterial = false;
		dudv = new Texture("resources/textures/dudv.png");
		normal = new Texture("resources/textures/normal.png");
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

		Matrix4f view = graphics.GraphicsManager.camera.view;
		//view.m03 = 0;
		//view.m13 = 0;
		//	view.m23 = 0;
		setUniformMatrix4f("modelView", view.multiply(model.getMatrix()));
	}
}
