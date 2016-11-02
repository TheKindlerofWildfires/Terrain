package world;

import static graphics.Shader.setMaterial;
import static graphics.Shader.setUniform3f;
import static graphics.Shader.setUniform4f;
import static graphics.Shader.setUniformMatrix4f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import maths.Matrix4f;
import maths.Transformation;
import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Skybox extends GameObject {

	public Skybox(String obj, String texture) {
		super(obj, texture,true);
		shader = graphics.ShaderManager.objectShader;
		this.material.useColour = 0;
		this.model = new Transformation();
		this.scale(15, 15, 15);
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
		setUniform3f("ambientLight", new Vector3f(1f, 1f, 1f));
		setUniformMatrix4f("model", model.getMatrix());
		setUniform4f("clipPlane", clipPlane);
	}
}
