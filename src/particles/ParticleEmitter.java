package particles;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;

import graphics.GraphicsManager;
import graphics.Window;
import maths.Matrix4f;
import maths.Vector3f;
import maths.Vector4f;
import models.InstancedVAO;
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
	private List<Matrix4f> models;

	InstancedVAO vao;

	public ParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		particles = new ArrayList<Particle>();
		this.baseParticle = baseParticle;
		this.maxParticles = maxParticles;
		this.active = false;
		this.lastCreationTime = 0;
		this.creationPeriodMillis = creationPeriodMillis;
		vao = new InstancedVAO(baseParticle.getVAO(), 50);
		models = new ArrayList<Matrix4f>();
		for (int i = 0; i < maxParticles; i++) {
			models.add(new Matrix4f(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
		}
		createInstanceDataBuffer();
	}

	int instanceDataVBO;
	FloatBuffer instanceDataBuffer;

	private void createInstanceDataBuffer() {
		instanceDataVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
		instanceDataBuffer = BufferUtils.createFloatBuffer(maxParticles * InstancedVAO.INSTANCE_SIZE_FLOATS);
		for (Matrix4f matrix : models) {
			Matrix4f modelMatrix = matrix;
			Matrix4f modelViewMatrix = modelMatrix.multiply(GraphicsManager.camera.view);
			instanceDataBuffer.put(modelViewMatrix.getBuffer());
		}
		glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_STATIC_DRAW);
		int index = 2;
		int strideStart = 0;
		for (int i = 0; i < 4; i++) {
			glEnableVertexAttribArray(index);
			glVertexAttribPointer(index, 4, GL_FLOAT, false, InstancedVAO.INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(index, 1);
			index++;
			strideStart += InstancedVAO.VECTOR4F_SIZE_BYTES;
		}
		glBindBuffer(GL_ARRAY_BUFFER, 0);
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
	}

	public void render(Vector4f clipPlane) {
		Iterator<? extends GameObject> it = particles.iterator();
		while (it.hasNext()) {
			Particle particle = (Particle) it.next();
			particle.render(clipPlane);
		}

		//	vao.renderListInstanced(particles, GraphicsManager.camera.view, clipPlane);
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
