package models;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import graphics.Shader;
import maths.Utilities;
import maths.Vector3f;
import maths.Vector4f;

public class VertexArrayObject {

	public static int numberOfVAOS = 0;

	protected int vaoID;
	protected int size;
	protected float[] vertices;

	/**
	 * Create VAO with vertices and indices
	 * 
	 * @param vertices
	 *            vertex array
	 * @param indices
	 *            indices array
	 * @param numberOfVec3s
	 *            number of vector3fs per vertex (x,y,z,etc)
	 */
	public VertexArrayObject(float[] vertices, byte[] indices, int numberOfVec3s) {
		numberOfVAOS++;
		size = indices.length;
		this.vertices = vertices;
		createArrayObject(vertices, indices, numberOfVec3s);
	}

	/**
	 * Create VAO with vertices
	 * 
	 * @param vertices
	 *            vertex array
	 * @param numberOfVec3s
	 *            number of vector3fs per vertex (x,y,z,etc)
	 */
	public VertexArrayObject(float[] vertices, int numberOfVec3s) {
		numberOfVAOS++;
		this.vertices = vertices;
		size = vertices.length / 3 / numberOfVec3s;
		createArrayObject(vertices, numberOfVec3s);
	}

	/**
	 * Create empty VAO object; not really for public usage
	 */
	protected VertexArrayObject() {
	}

	/**
	 * Creates Array Object with vertices and indices
	 * 
	 * @param positions
	 *            data array
	 * @param indices
	 *            indices array
	 * @param numberOfVec3s
	 *            number of vector4fs per vertex (x,y,z,etc)
	 */
	public void createArrayObject(float[] positions, byte[] indices, int numberOfVec3s) {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		createVerticesBuffer(positions, numberOfVec3s);
		createIndicesBuffer(indices);

		glBindVertexArray(0);
	}

	/**
	 * Create Array Object with vertices
	 * 
	 * @param positions
	 *            data array
	 * @param numberOfVec3s
	 *            number of vector4fs per vertex (x,y,z,etc)
	 */
	public void createArrayObject(float[] positions, int numberOfVec3s) {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		createVerticesBuffer(positions, numberOfVec3s);
		glBindVertexArray(0);
	}

	/**
	 * creates vertex buffer
	 * 
	 * @param positions
	 *            data array
	 * @param numberOfVec3s
	 *            number of vector4fs per vertex (x,y,z,etc)
	 */
	private void createVerticesBuffer(float[] positions, int numberOfVec3s) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, Utilities.createFloatBuffer(positions), GL_STATIC_DRAW);
		for (int i = 0; i < numberOfVec3s; i++) {
			glEnableVertexAttribArray(i);
			glVertexAttribPointer(i, 3, GL_FLOAT, false, 4 * 3 * numberOfVec3s, i * 3 * 4); // send
																							// positions
																							// on
																							// pipe
																							// i
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Creates Index Buffer
	 * 
	 * @param indices
	 *            indices
	 */
	private void createIndicesBuffer(byte[] indices) {
		int ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utilities.createByteBuffer(indices), GL_STATIC_DRAW);
	}

	/**
	 * returns the ID of the VAO, used for rendering
	 * 
	 * @return
	 */
	public int getVaoID() {
		return this.vaoID;
	}

	/**
	 * returns the size of the VAO, used for rendering
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	protected void initRender(Vector4f clipPlane) {
		glBindVertexArray(vaoID);
		glEnable(GL_CLIP_DISTANCE0);
		Shader.setUniform4f("clipPlane", clipPlane);
	}
	public Vector3f getVert(int i){
		
		return new Vector3f(vertices[i],vertices[i+1],vertices[i+2]);
	}
}
