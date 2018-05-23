package world;

import maths.Matrix4f;
import maths.Transformation;
import maths.Vector4f;
import object.GameObject;

import static graphics.Shader.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Skybox extends GameObject {

	public Skybox(String obj, String texture) {
		super(obj, texture, true);
		shader = graphics.ShaderManager.objectShader;
		this.material.useColour = 0;
		this.model = new Transformation();
		this.scale(18, 18, 18);
	}

	protected void renderPrep(Vector4f clipPlane) {
		Matrix4f view = graphics.GraphicsManager.camera.view.multiply(model.getMatrix());
		view.m03 = 0;
		view.m13 = 0;
		view.m23 = 0;
		setUniformMatrix4f("modelView", view);
		if (hasMaterial) {
			setMaterial("material", material);
		}
		if (textured) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		setUniformMatrix4f("model", model.getMatrix());
		setUniform4f("clipPlane", clipPlane);
	}
}
