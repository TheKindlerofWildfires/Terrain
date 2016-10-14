package object;

import static graphics.Shader.setMaterial;
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

import graphics.Material;
import graphics.Texture;
import graphics.VertexArrayObject;
import maths.BoundingBox;
import maths.Transformation;
import maths.Vector3f;
import maths.Vector4f;

public class GameObject {
	private static final Vector3f GRAVITY = new Vector3f(0, 0, 0);
	private Vector3f fakeFriction = new Vector3f();
	protected Texture texture;
	protected VertexArrayObject vao;
	protected Material material;
	protected Transformation model;

	protected boolean textured;
	protected boolean hasMaterial = true;

	protected int shader;
	public Vector3f velocity = new Vector3f();
	public Vector3f position = new Vector3f();
	public float mass = 10;
	public Vector3f acceleration = new Vector3f();
	public Vector3f force = new Vector3f();
	public BoundingBox boundingBox;

	/**
	 * Creates a new object with the model and texture given
	 * 
	 * @param modelPath path to model
	 * @param texturePath path to texture
	 * @param box Bounding Box
	 */
	public GameObject(String modelPath, String texturePath) {
		if (modelPath != "none") {
			try {
				vao = OBJLoader.loadMesh(modelPath);
				boundingBox = OBJLoader.loadBox(modelPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		material = new Material();
		if (texturePath != "none") {
			texture = new Texture(texturePath);
			System.out.println(texturePath);
			textured = true;
		} else {
			textured = false;
		}
		model = new Transformation();
		shader = graphics.ShaderManager.objectShader;
		material.colour = new Vector3f(1, 1, 1);
		material.reflectance = 1;
		material.useColour = 1;

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

	public void rotate(int angle, int x, int y, int z) throws IllegalArgumentException {
		if (angle % 90 != 0) {
			throw new IllegalArgumentException("you can only rotate bounding boxes by right angles");
		}
		if (angle < 0) {
			angle = 360 + angle;
		}
		if (!(x >= 1 ^ y >= 1 ^ z >= 1)) {
			throw new IllegalArgumentException("pls don't rotate in multiple axes at once");
		}
		model.rotate(angle, x, y, z);
		int a = angle / 90;
		boundingBox.rotate(a * x, a * y, a * z);
		//at some point the bounding box should rotate too
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

	public void physic() {
		fakeFriction = velocity.negate().scale(0.01f);
		acceleration = force.scale(1 / mass).add(GRAVITY);
		velocity = velocity.add(acceleration).add(fakeFriction);
		position = boundingBox.centre;

		/* for all objects:
		calculate acceleration from mass and force
		calculate velocity from mass,current velocity, acceleration and gravity
		apply relevant frictions 
		move them by their velocity
		check bounding box
		
		
		*/

	}

	public void setForce(Vector3f force) {
		this.force = force;
	}

	/**
	 * Renders the specified object
	 */
	public void render(Vector4f clipPlane) {
		start(shader);
		renderPrep(clipPlane);
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		stop();
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
		setUniform4f("clipPlane", clipPlane);
	}
}
