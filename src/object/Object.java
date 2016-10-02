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
import maths.BoundingBox;
import maths.Matrix4f;
import maths.Transformation;
import maths.Vector3f;

public class Object {
	protected Texture texture;
	protected VertexArrayObject vao;
	protected Material material;
	protected Transformation model;

	protected int shader;

	public BoundingBox boundingBox;

	/**
	 * Creates a new object with the model and texture given
	 * 
	 * @param modelPath path to model
	 * @param texturePath path to texture
	 * @param box Bounding Box
	 */
	public Object(String modelPath, String texturePath, BoundingBox box) {
		try {
			vao = OBJLoader.loadMesh(modelPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		material = new Material();
		texture = new Texture(texturePath);
		model = new Transformation();
		shader = graphics.ShaderManager.objectShader;
		material.colour = new Vector3f(1, 1, 1);
		material.reflectance = 1;
		material.useColour = 1;
		boundingBox = box;
	}

	public void scale(float x, float y, float z) {
		model.scale(x, y, z);
		boundingBox.x *= x;
		boundingBox.y *= y;
		boundingBox.z *= z;
	}

	public void translate(float x, float y, float z) {
		model.translate(x, y, z);
		boundingBox.centre.x += x;
		boundingBox.centre.y += y;
		boundingBox.centre.z += z;
	}

	public void translate(Vector3f displacement) {
		translate(displacement.x, displacement.y, displacement.z);
	}

	public void placeAt(float x, float y, float z) {
		model.placeAt(x, y, z);
		boundingBox.centre.x = x;
		boundingBox.centre.y = y;
		boundingBox.centre.z = z;
	}

	/**
	 * Renders the specified object
	 */
	public void render() {
		start(shader);
		setUniformMatrix4f("modelView", graphics.GraphicsManager.camera.view.multiply(model.getMatrix()));
		setMaterial("material", material);
		glBindTexture(GL_TEXTURE_2D, texture.getId());
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		stop();
	}
}
