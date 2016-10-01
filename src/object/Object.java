package object;

import static graphics.Shader.setMaterial;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import graphics.Material;
import graphics.Texture;
import graphics.VertexArrayObject;
import maths.Matrix4f;
import maths.Vector3f;

public class Object {
	protected Texture texture;
	protected VertexArrayObject vao;
	protected Material material;
	protected Matrix4f model;

	protected int shader;

	/**
	 * Creates a new object with the model and texture given
	 * 
	 * @param modelPath
	 * @param texturePath
	 */
	public Object(String modelPath, String texturePath) {
		try {
			vao = OBJLoader.loadMesh(modelPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		material = new Material();
		texture = new Texture(texturePath);
		model = new Matrix4f().multiply(Matrix4f.translate(6, 8, 9));
		shader = graphics.ShaderManager.objectShader;
		material.colour = new Vector3f(1, 1, 1);
		material.reflectance = 0;
		material.useColour = 1;
	}

	/**
	 * Renders the specified object
	 */
	public void render() {
		start(shader);
		setUniformMatrix4f("modelView", graphics.GraphicsManager.camera.view.multiply(model));
		setMaterial("material", material);
		glBindTexture(GL_TEXTURE_2D, texture.getId());
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		stop();
	}
}
