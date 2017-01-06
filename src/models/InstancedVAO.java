package models;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
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
import graphics.ShaderManager;
import graphics.Window;
import maths.Matrix4f;
import maths.Vector4f;
import object.GameObject;

public class InstancedVAO extends VertexArrayObject {

	private static final int FLOAT_SIZE_BYTES = 4;
	public static final int VECTOR4F_SIZE_BYTES = 4 * FLOAT_SIZE_BYTES;

	private static final int MATRIX_SIZE_FLOATS = 4 * 4;
	private static final int MATRIX_SIZE_BYTES = MATRIX_SIZE_FLOATS * FLOAT_SIZE_BYTES;

	public static final int INSTANCE_SIZE_BYTES = MATRIX_SIZE_BYTES;
	public static final int INSTANCE_SIZE_FLOATS = MATRIX_SIZE_FLOATS;

	private int instanceDataVBO;
	private FloatBuffer instanceDataBuffer;
	private final int numInstances;

	public InstancedVAO(float[] vertices, byte[] indices, int numberOfVec3s, int numInstances) {
		super(vertices, indices, numberOfVec3s);
		this.numInstances = numInstances;

		glBindVertexArray(vaoID);

		createInstanceDataBuffer();

		glBindVertexArray(0);
	}

	public InstancedVAO(float[] vertices, int numberOfVec3s, int numInstances) {
		super(vertices, numberOfVec3s);
		this.numInstances = numInstances;

		glBindVertexArray(vaoID);

		createInstanceDataBuffer();

		glBindVertexArray(0);
	}

	public InstancedVAO(VertexArrayObject vao, int numInstances) {
		this.numInstances = numInstances;
		this.size = vao.size;
		this.vaoID = vao.vaoID;

		glBindVertexArray(vaoID);
		// createInstanceDataBuffer();

		glBindVertexArray(0);
	}

	private void createInstanceDataBuffer() {
		instanceDataVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
		instanceDataBuffer = BufferUtils.createFloatBuffer(numInstances * INSTANCE_SIZE_FLOATS);
		glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_STATIC_DRAW);
		int index = 3;
		int strideStart = 0;
		for (int i = 0; i < 4; i++) {
			glEnableVertexAttribArray(index);
			glVertexAttribPointer(index, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(index, 1);
			index++;
			strideStart += VECTOR4F_SIZE_BYTES;
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	

	public void renderListInstanced(List<? extends GameObject> gameObjects, Matrix4f viewMatrix, Vector4f clipPlane) {

		if (gameObjects.size() != 0) {

			Shader.start(ShaderManager.objectShader);

			//initRender(clipPlane);

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
		// this.instanceDataBuffer.clear();

		for (GameObject gameObject : gameObjects) {
			//Matrix4f modelMatrix = gameObject.model.getMatrix();
			//Matrix4f modelViewMatrix = modelMatrix.multiply(viewMatrix);
			// instanceDataBuffer.put(modelViewMatrix.getBuffer());
			gameObject.render(Window.renderClipPlane);
		}

		// glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
		// glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_DRAW);

		glDrawArraysInstanced(GL_TRIANGLES, getSize(), 0, 1);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

}
