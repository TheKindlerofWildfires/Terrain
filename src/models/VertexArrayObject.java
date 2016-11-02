package models;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import maths.Utilities;

public class VertexArrayObject {

	public static int numberOfVAOS = 0;

	private int vaoID;
	private int size;

	public VertexArrayObject(float[] vertices, byte[] indices, int numberOfVec3s) {
		numberOfVAOS++;
		size = indices.length;
		createArrayObject(vertices, indices, numberOfVec3s);
	}

	public VertexArrayObject(float[] vertices, int numberOfVec3s) {
		numberOfVAOS++;
		size = vertices.length / 3 / numberOfVec3s;
		createArrayObject(vertices, numberOfVec3s);
	}

	public void createArrayObject(float[] positions, byte[] indices,
			int numberOfVec3s) {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		createVerticesBuffer(positions, numberOfVec3s);
		createIndicesBuffer(indices);

		glBindVertexArray(0);
	}

	public void createArrayObject(float[] positions, int numberOfVec3s) {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		createVerticesBuffer(positions, numberOfVec3s);
		glBindVertexArray(0);
	}

	private void createVerticesBuffer(float[] positions, int numberOfVec3s) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, Utilities.createFloatBuffer(positions),
				GL_STATIC_DRAW);
		for (int i = 0; i < numberOfVec3s; i++) {
			glEnableVertexAttribArray(i);
			glVertexAttribPointer(i, 3, GL_FLOAT, false, 4 * 3 * numberOfVec3s,
					i * 3 * 4); // send positions on pipe i
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private void createIndicesBuffer(byte[] indices) {
		int ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER,
				Utilities.createByteBuffer(indices), GL_STATIC_DRAW);
	}

	public int getVaoID() {
		return this.vaoID;
	}

	public int getSize() {
		return this.size;
	}
}
