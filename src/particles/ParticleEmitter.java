package particles;

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
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;

import graphics.GraphicsManager;
import graphics.Shader;
import graphics.ShaderManager;
import maths.Vector3f;
import maths.Vector4f;
import models.VertexArrayObject;
import object.GameObject;

public abstract class ParticleEmitter {

	protected int maxParticles;
	private boolean active;
	public List<Particle> particles;

	protected final Particle baseParticle;
	private Vector4f[] models;
	private FloatBuffer modelBuffer;
	private VertexArrayObject vao;
	private int instanceVboID;

	protected long creationPeriodMillis;
	protected long lastCreationTime;

	/**
	 * makes a new particle emitter
	 * @param baseParticle the particle all particles emitted will be based on
	 * @param maxParticles the max number of particles existing at any time
	 * @param creationPeriodMillis the time between particle creation calls in ms
	 */
	public ParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		particles = new ArrayList<Particle>();
		this.baseParticle = baseParticle;
		this.maxParticles = maxParticles;
		this.active = false;
		this.lastCreationTime = 0;
		this.creationPeriodMillis = creationPeriodMillis;
		vao = baseParticle.getVAO();
		models = new Vector4f[maxParticles];
		for (int i = 0; i < maxParticles; i++) {
			models[i] = new Vector4f();
		}
		modelBuffer = BufferUtils.createFloatBuffer(maxParticles * 4);
		fillModelBuffer();
		createInstanceDataVBO();
	}

	/**
	 * passes the modelBuffer to opengl
	 */
	private final void passModelBuffer() {
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
	private final void fillModelBuffer() {
		modelBuffer.clear();
		for (int i = 0; i < particles.size(); i++) {
			Vector3f pos = particles.get(i).position;
			modelBuffer.put(new Vector4f(pos, particles.get(i).scale.x).getBuffer());
		}
		modelBuffer.flip();
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
	 * updates the particles, should be called once per tick
	 * @param ellapsedTime time since last update call in ms
	 */
	public final void update(long ellapsedTime) {
		if(!active){
			return;
		}
		long now = System.currentTimeMillis();
		if (lastCreationTime == 0) {
			lastCreationTime = now;
		}
		Iterator<? extends GameObject> it = particles.iterator();
		while (it.hasNext()) {
			Particle particle = (Particle) it.next();
			if (particle.updateTtl(ellapsedTime) < 0) {
				it.remove();
			} else {
				updatePosition(particle, ellapsedTime);
			}
		}

		int length = particles.size();
		if (now - lastCreationTime >= this.creationPeriodMillis && length < maxParticles) {
			createParticle();
			this.lastCreationTime = now;
		}
		fillModelBuffer();
		passModelBuffer();
	}

	/**
	 * renders all the particles
	 * @param clipPlane the clipping plane to be used
	 */
	public final void render(Vector4f clipPlane) {
		if(!active){
			return;
		}
		Shader.start(ShaderManager.particleShader);
		Shader.setUniformMatrix4f("view", GraphicsManager.camera.view);
		Shader.setUniform4f("clipPlane", clipPlane);
		glEnable(GL_CLIP_DISTANCE0);
		glBindVertexArray(vao.getVaoID());
		glDrawArraysInstanced(GL_TRIANGLES, 0, vao.getSize(), particles.size());
		glBindVertexArray(0);
		Shader.stop();
	}

	/**
	 * used to create a new particle
	 * must be overridden by subclasses
	 */
	protected abstract void createParticle();

	/**
	 * used to update individual particles
	 * must be overridden by subclasses
	 * @param particle the particle to be updated
	 * @param elapsedTime time since last update call
	 */
	protected abstract void updatePosition(Particle particle, long elapsedTime);

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
