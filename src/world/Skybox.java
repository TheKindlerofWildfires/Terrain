package world;

import static graphics.Shader.setMaterial;
import static graphics.Shader.setUniform3f;
import static graphics.Shader.setUniform4f;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import maths.Matrix4f;
import maths.Transformation;
import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Skybox extends GameObject {

	public Skybox(String obj, String texture) {
		super(obj, texture);
		shader = graphics.ShaderManager.objectShader;
		this.material.useColour = 0;
		this.model = new Transformation();
		this.scale(15, 15, 15);
	}

	protected void renderPrep(Vector4f clipPlane) {
		setUniformMatrix4f("modelView", graphics.GraphicsManager.camera.view.multiply(model.getMatrix()));
		if (hasMaterial) {
			setMaterial("material", material);
		}
		if (textured) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		setUniform3f("ambientLight", new Vector3f(.6f, .6f, .6f));
		setUniformMatrix4f("model", model.getMatrix());
		setUniform4f("clipPlane", clipPlane);
	}
}
