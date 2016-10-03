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
import maths.Transformation;
import maths.Vector3f;

public class Object {
	private static final Vector3f GRAVITY = new Vector3f(0,0,0);
	private Vector3f FRICTION = new Vector3f();
	protected Texture texture;
	protected VertexArrayObject vao;
	protected Material material;
	protected Transformation model;

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
	public void rotate(int angle, int x, int y, int z) {
		model.rotate(angle, x, y, z);
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

	public void physic() {
		FRICTION = velocity.negate().scale(0.1f);
		force = force.add(FRICTION);
		acceleration= force.scale(1/mass).add(GRAVITY);
		velocity = velocity.add(acceleration);
		position = boundingBox.centre;
		System.out.println(boundingBox.centre);
		System.out.println(position);
		
		/* for all objects:
		calculate acceleration from mass and force
		calculate velocity from mass,current velocity, acceleration and gravity
		apply relevant frictions 
		move them by their velocity
		check bounding box
		
		
		*/
		
	}


}
