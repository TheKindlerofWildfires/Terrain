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
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.io.IOException;

import graphics.Material;
import graphics.ShaderManager;
import graphics.Texture;
import maths.BoundingBox;
import maths.Transformation;
import maths.Vector3f;
import maths.Vector4f;
import models.ModelManager;
import models.VertexArrayObject;

/**
 * @author TheKingInYellow & HMSRothman
 */
public class GameObject {
	private static final Vector3f GRAVITY = new Vector3f(0, 0, -1);
	protected Texture texture;
	protected VertexArrayObject vao;
	protected float[] vaoData;
	public Material material;
	public Transformation model;
	public boolean isGL;
	public Vector3f scale = new Vector3f(1, 1, 1);

	protected boolean textured;
	protected boolean hasMaterial = true;

	public int shader;
	public Vector3f velocity = new Vector3f();
	public Vector3f position = new Vector3f();
	public float mass = 10;
	public Vector3f acceleration = new Vector3f();
	public Vector3f force = new Vector3f();
	public BoundingBox boundingBox;
	public boolean enabled = true;
	public boolean resting = false;

	/**
	 * Creates a new object with the model and texture given
	 * 
	 * @param modelPath
	 *            path to model
	 * @param texturePath
	 *            path to texture
	 * @param box
	 *            Bounding Box
	 */
	public GameObject(String modelPath, String texturePath, boolean isGL) {
		this.isGL = isGL;
		if (modelPath != "none") {
			boundingBox = new BoundingBox(new Vector3f(0, 0, 0), 1, 1, 1);
			if (isGL) {
				try {
					vao = ModelManager.loadGlModel(modelPath).vao;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					vaoData = ModelManager.loadModel(modelPath).vaoData;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		material = new Material();
		if (texturePath != "none" && isGL) {
			texture = new Texture(texturePath);
			textured = true;
		} else {
			textured = false;
		}
		model = new Transformation();
		shader = graphics.ShaderManager.objectShader;
		material.colour = new Vector3f(1f, 1f, 1f);
		material.reflectance = 1;
		material.useColour = 1;
	}

	/**
	 * copy constructor
	 * @param baseObject base object
	 */
	public GameObject(GameObject baseObject) {
		this.texture = baseObject.texture;
		this.vao = baseObject.vao;
		this.vaoData = baseObject.vaoData;
		this.isGL = baseObject.isGL;

		this.material = new Material(baseObject.material);
		this.textured = baseObject.textured;
		this.hasMaterial = baseObject.hasMaterial;
		this.shader = baseObject.shader;

		this.model = new Transformation(baseObject.model);
		this.scale = new Vector3f(baseObject.scale);

		this.velocity = new Vector3f(baseObject.velocity);
		this.position = new Vector3f(baseObject.position);
		this.mass = baseObject.mass;
		this.acceleration = new Vector3f(baseObject.acceleration);
		this.force = new Vector3f(baseObject.force);

		this.boundingBox = baseObject.boundingBox;
		this.enabled = baseObject.enabled;
	}

	public GameObject() {

	}

	public void setScale(float x, float y, float z) {
		model.setScale(x, y, z);
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}

	public void scale(float x, float y, float z) {
		model.scale(x, y, z);
		boundingBox.x *= x;
		boundingBox.y *= y;
		boundingBox.z *= z;
		scale.x *= x;
		scale.y *= y;
		scale.z *= z;

	}

	public void translate(float x, float y, float z) {
		model.translate(x, y, z);
		boundingBox.centre.x += x;
		boundingBox.centre.y += y;
		boundingBox.centre.z += z;
		position = position.add(new Vector3f(x, y, z));
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
		// at some point the bounding box should rotate too
	}

	public void translate(Vector3f displacement) {
		translate(displacement.x, displacement.y, displacement.z);
		position.add(displacement);
	}

	public void placeAt(float x, float y, float z) {
		model.placeAt(x, y, z);
		boundingBox.centre.x = x;
		boundingBox.centre.y = y;
		boundingBox.centre.z = z;
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void physic() {
		if (enabled) {
			if(!resting){
				acceleration = GRAVITY.scale(.1f);
			}else{
				acceleration = new Vector3f(0,0,0);
				if(velocity.z <0){
					velocity.z = 0;
				}
			}
			velocity = velocity.add(acceleration);
			velocity = velocity.scale(.9f);
			this.translate(velocity);
			//position = boundingBox.centre;
		}
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
		glEnable(GL_CLIP_DISTANCE0);
		if (shader == ShaderManager.objectShader) {
			setUniformMatrix4f("model", model.getMatrix());
		}
		setUniform4f("clipPlane", clipPlane);
	}

	public void makeGL() {
		assert !isGL : "Game Object is GL";
		vao = new VertexArrayObject(vaoData, 3);
		this.isGL = true;
	}

	public int getShader() {
		return shader;
	}

	public VertexArrayObject getVAO() {
		return vao;
	}
	//Unclear if i use vec3 or 4
	public Vector4f[] getNorm(){
		Vector4f[] axes = new Vector4f[vao.getSize()/3]; //correct number of vertices
		for (int i = 0; i < vao.getSize()/3; i++) {
			Vector4f transit = new Vector4f(vao.getNorm(i), 1);
			transit = model.getMatrix().multiply(transit);
		  axes[i] = transit;
		}
		return axes;
		}
	/**
	 * Mix max
	 * @param axis
	 * @return
	 */
	public float[] project(Vector4f axis){ 
		Vector4f transit = new Vector4f(vao.getVert(0), 1);
		transit = model.getMatrix().multiply(transit);
		float min = axis.dot(transit);
		float max = min;
		for (int i = 1; i < vao.getSize(); i++) {
			transit = new Vector4f(vao.getVert(i), 1);
			transit = model.getMatrix().multiply(transit);
			float p = axis.dot(transit);
			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}
		float [] b = new float[2];
		b[0] = min;
		b[1] = max;
		return b;
	}
}
