package models;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import graphics.Shader;
import maths.Matrix4f;
import maths.Vector4f;
import object.GameObject;

public class InstancedVAO extends VertexArrayObject {

	private static final int FLOAT_SIZE_BYTES = 4;
	private static final int VECTOR4F_SIZE_BYTES = 4 * FLOAT_SIZE_BYTES;

	private static final int MATRIX_SIZE_FLOATS = 4 * 4;
	private static final int MATRIX_SIZE_BYTES = MATRIX_SIZE_FLOATS * FLOAT_SIZE_BYTES;

	private static final int INSTANCE_SIZE_BYTES = MATRIX_SIZE_BYTES;
	private static final int INSTANCE_SIZE_FLOATS = MATRIX_SIZE_FLOATS;

	private final int instanceDataVBO;
	private final FloatBuffer instanceDataBuffer;
	private final int numInstances;

	public InstancedVAO(float[] vertices, byte[] indices, int numberOfVec3s, int numInstances) {
		super(vertices, indices, numberOfVec3s);
		this.numInstances = numInstances;

		glBindVertexArray(this.getVaoID());

		instanceDataVBO = glGenBuffers();
		this.instanceDataBuffer = BufferUtils.createFloatBuffer(numInstances * INSTANCE_SIZE_FLOATS);
		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);

		int start = 5;
		int strideStart = 0;
		for (int i = 0; i < 4; i++) {
			glVertexAttribPointer(start, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(start, 1);
			start++;
			strideStart += VECTOR4F_SIZE_BYTES;
		}

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public InstancedVAO(float[] vertices, int numberOfVec3s, int numInstances) {
		super(vertices, numberOfVec3s);

		this.numInstances = numInstances;

		glBindVertexArray(this.getVaoID());

		instanceDataVBO = glGenBuffers();
		this.instanceDataBuffer = BufferUtils.createFloatBuffer(numInstances * INSTANCE_SIZE_FLOATS);
		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);

		int start = 5;
		int strideStart = 0;
		for (int i = 0; i < 4; i++) {
			glVertexAttribPointer(start, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(start, 1);
			start++;
			strideStart += VECTOR4F_SIZE_BYTES;
		}

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public InstancedVAO(VertexArrayObject vao, int numInstances) {
		this.numInstances = numInstances;
		this.size = vao.size;
		this.vaoID = vao.vaoID;

		glBindVertexArray(this.getVaoID());

		instanceDataVBO = glGenBuffers();
		this.instanceDataBuffer = BufferUtils.createFloatBuffer(numInstances * INSTANCE_SIZE_FLOATS);
		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);

		int start = 5;
		int strideStart = 0;
		for (int i = 0; i < 4; i++) {
			glVertexAttribPointer(start, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(start, 1);
			start++;
			strideStart += VECTOR4F_SIZE_BYTES;
		}

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	@Override
	protected void initRender(Vector4f clipPlane) {
		super.initRender(clipPlane);

		int start = 5;
		int numElements = 4 * 2 + 1;
		for (int i = 0; i < numElements; i++)

		{
			glEnableVertexAttribArray(start + i);
		}

	}

	public void renderListInstanced(List<? extends GameObject> gameObjects, Matrix4f viewMatrix, Vector4f clipPlane) {

		if (gameObjects.size() != 0) {
			Shader.start(gameObjects.get(0).getShader());

			initRender(clipPlane);

			int chunkSize = numInstances;
			int length = gameObjects.size();
			for (int i = 0; i < length; i += chunkSize) {
				int end = Math.min(length, i + chunkSize);
				List<? extends GameObject> subList = gameObjects.subList(i, end);
				renderChunkInstanced(subList, viewMatrix);
			}

			Shader.stop();
		}
	}

	private void renderChunkInstanced(List<? extends GameObject> gameObjects, Matrix4f viewMatrix) {
		this.instanceDataBuffer.clear();

		for (GameObject gameObject : gameObjects) {
			Matrix4f modelMatrix = gameObject.model.getMatrix();
			Matrix4f modelViewMatrix = modelMatrix.multiply(viewMatrix);
			instanceDataBuffer.put(modelViewMatrix.getBuffer());
		}

		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
		glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_DRAW);

		glDrawArraysInstanced(GL_TRIANGLES, getSize(), 0, gameObjects.size());

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

}
