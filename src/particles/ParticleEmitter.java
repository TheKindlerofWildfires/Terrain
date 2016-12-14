package particles;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
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
import maths.Matrix4f;
import maths.Vector3f;
import maths.Vector4f;
import models.VertexArrayObject;
import object.GameObject;

public class ParticleEmitter {

	private int maxParticles;
	public boolean active;
	private final Particle baseParticle;
	private long creationPeriodMillis;
	private long lastCreationTime;
	private float speedRndRange = 10;
	private float positionRndRange = 0;
	private float scaleRndRange = .01f;
	private List<Particle> particles;
	private Vector4f[] models;
	private FloatBuffer modelBuffer;
	private VertexArrayObject vao;
	private int instanceVboID;

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
	 * this is surprisingly fast
	 */
	private void passModelBuffer() {
		glBindVertexArray(vao.getVaoID());
		glBindBuffer(GL_ARRAY_BUFFER, instanceVboID);
		glBufferData(GL_ARRAY_BUFFER, NULL, GL_STREAM_DRAW);
		glBufferData(GL_ARRAY_BUFFER, modelBuffer, GL_STREAM_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	/**
	 * This gets slow at high particle amounts
	 */
	private void fillModelBuffer() {
		modelBuffer.clear();
		for (int i = 0; i < particles.size(); i++) {
			Vector3f pos = particles.get(i).position;
			modelBuffer.put(new Vector4f(pos, particles.get(i).scale.x).getBuffer());
		}
		modelBuffer.flip();
		//System.out.println(System.nanoTime() - start);
	}

	private void createInstanceDataVBO() {
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

	public void update(long ellapsedTime) {
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

	public void render(Vector4f clipPlane) {
		//		Iterator<? extends GameObject> it = particles.iterator();
		//		while (it.hasNext()) {
		//			Particle particle = (Particle) it.next();
		//			particle.render(clipPlane);
		//		}

		Shader.start(ShaderManager.particleShader);
		Shader.setUniformMatrix4f("view", GraphicsManager.camera.view);
		Shader.setUniform4f("clipPlane", clipPlane);
		glBindVertexArray(vao.getVaoID());
		glDrawArraysInstanced(GL_TRIANGLES, 0, vao.getSize(), particles.size());
		glBindVertexArray(0);
		Shader.stop();
	}

	private void createParticle() {
		Particle particle = new Particle(baseParticle);

		//randomize velocity
		float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float xAngleInc = sign * (float) Math.random() * this.speedRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float yAngleInc = sign * (float) Math.random() * this.speedRndRange;
		float z = particle.velocity.z;
		float y = z * (float) (Math.sin(Math.toRadians(particle.angleY + yAngleInc)));
		float x = z * (float) (Math.sin(Math.toRadians(particle.angleX + xAngleInc)));
		particle.velocity = new Vector3f(x, y, z);

		//randomize position
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncX = sign * (float) Math.random() * this.positionRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncY = sign * (float) Math.random() * this.positionRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncZ = sign * (float) Math.random() * this.positionRndRange;
		particle.translate(posIncX, posIncY, posIncZ);

		//randomize scale
		float scaleInc = sign * (float) Math.random() * this.scaleRndRange;
		particle.setScale(.1f + scaleInc, .1f + scaleInc, .1f + scaleInc);

		particles.add(particle);
	}

	/**
	 * Updates a particle position
	 * @param particle The particle to update
	 * @param elapsedTime Elapsed time in milliseconds
	 */
	private void updatePosition(Particle particle, long elapsedTime) {
		Vector3f speed = particle.velocity;
		float delta = elapsedTime / 1000f;
		float dx = speed.x * delta;
		float dy = speed.y * delta;
		float dz = speed.z * delta;
		//System.out.println(dz);
		particle.translate(dx, dy, dz);
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}
}