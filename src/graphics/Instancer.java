package graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import maths.Vector3f;
import maths.Vector4f;
import models.VertexArrayObject;
import object.GameObject;
import particles.Particle;

public abstract class Instancer {
	private Vector4f[] models;
	private FloatBuffer modelBuffer;
	private VertexArrayObject vao;
	private int instanceVboID;
	protected boolean active;
	protected final Particle baseObject;
	protected final int maxObjects;
	protected Vector4f colour;

	protected List<GameObject> objects;

	public Instancer(Particle object, int numInstances) {
		objects = new ArrayList<GameObject>();
		vao = object.getVAO();
		models = new Vector4f[numInstances];
		for (int i = 0; i < numInstances; i++) {
			models[i] = new Vector4f();
		}
		modelBuffer = BufferUtils.createFloatBuffer(numInstances * 4);
		fillModelBuffer();
		createInstanceDataVBO();
		baseObject = object;
		maxObjects = numInstances;
	}

	/**
	 * creates the vbo on which the per-instance data will be stored
	 */
	private final void createInstanceDataVBO() {
		glBindVertexArray(vao.getVaoID());
		instanceVboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, instanceVboID);
		glBufferData(GL_ARRAY_BUFFER, modelBuffer, GL_STREAM_DRAW);
		int index = 3;
		glEnableVertexAttribArray(index);
		glVertexAttribPointer(index, 4, GL_FLOAT, false, 4 * 4, 0);
		glVertexAttribDivisor(index, 1);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	/**
	 * passes the modelBuffer to opengl
	 */
	protected final void passModelBuffer() {
		glBindVertexArray(vao.getVaoID());
		glBindBuffer(GL_ARRAY_BUFFER, instanceVboID);
		glBufferData(GL_ARRAY_BUFFER, NULL, GL_STREAM_DRAW);
		glBufferData(GL_ARRAY_BUFFER, modelBuffer, GL_STREAM_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	/**
	 * fills the model buffer with data from the array
	 */
	protected final void fillModelBuffer() {
		modelBuffer.clear();
		for (int i = 0; i < objects.size(); i++) {
			Vector3f pos = objects.get(i).position;
			modelBuffer.put(new Vector4f(pos, objects.get(i).scale.x).getBuffer());
		}
		if (objects.isEmpty()) {
			modelBuffer.put(new Vector4f().getBuffer());
		}
		modelBuffer.flip();
	}

	/**
	 * renders all the particles
	 * @param clipPlane the clipping plane to be used
	 */
	public final void render(Vector4f clipPlane) {
		if (!active) {
			return;
		}
		Shader.start(ShaderManager.particleShader);
		Shader.setUniformMatrix4f("view", GraphicsManager.camera.view);
		Shader.setUniform4f("clipPlane", clipPlane);
		Shader.setUniform4f("color", colour);
		glEnable(GL_CLIP_DISTANCE0);
		glBindVertexArray(vao.getVaoID());
		glDrawArraysInstanced(GL_TRIANGLES, 0, vao.getSize(), objects.size());
		glBindVertexArray(0);
		Shader.stop();
	}

	/**
	 * turns on the particle emitter
	 */
	public final void activate() {
		this.active = true;
	}

	/**
	 * turns off the particle emitter
	 */
	public final void deactivate() {
		this.active = false;
	}
	
}
