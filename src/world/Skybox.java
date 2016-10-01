package world;

import static graphics.Shader.setMaterial;
import static graphics.Shader.setUniform3f;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import maths.Matrix4f;
import maths.Vector3f;
import object.Object;

public class Skybox extends Object {

	public Skybox(String obj, String texture) {
		super(obj, texture);
		shader = graphics.ShaderManager.objectShader;
		this.material.colour = new Vector3f(0, 0, 0);
		this.material.useColour = 0;
		this.material.reflectance = 0;
		this.model = Matrix4f.scale(10, 10, 10).multiply(Matrix4f.rotate(90, 1, 0, 0));
	}

	@Override
	public void render() {
		start(shader);
		Matrix4f view = graphics.GraphicsManager.camera.view;
		view.m03 = 0;
		view.m13 = 0;
		view.m23 = 0;
		setUniformMatrix4f("modelView", view.multiply(model));
		setMaterial("material", material);
		setUniform3f("ambientLight", new Vector3f(1, 1, 1));
		glBindTexture(GL_TEXTURE_2D, texture.getId());
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		stop();
	}

}
