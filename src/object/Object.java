package object;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import graphics.Texture;
import graphics.VertexArrayObject;

public class Object {
	Texture texture;

	VertexArrayObject vao;

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
		texture = new Texture(texturePath);
	}

	/**
	 * Renders the specified object
	 */
	public void render() {
		graphics.ShaderManager.objectShader.start();
		glBindTexture(GL_TEXTURE_2D, texture.getId());
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		graphics.ShaderManager.objectShader.stop();

	}
}
