package particles;

import java.util.Iterator;

<<<<<<< HEAD
import graphics.GraphicsManager;
import graphics.Shader;
import graphics.ShaderManager;
import maths.Vector3f;
import maths.Vector4f;
import models.VertexArrayObject;
=======
import graphics.Instancer;
>>>>>>> refs/remotes/origin/particle-wrap-up
import object.GameObject;

public abstract class ParticleEmitter extends Instancer {

	protected long creationPeriodMillis;
	protected long lastCreationTime;

	/**
	 * makes a new particle emitter
	 * @param baseParticle the particle all particles emitted will be based on
	 * @param maxParticles the max number of particles existing at any time
	 * @param creationPeriodMillis the time between particle creation calls in ms
	 */
	public ParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		super(baseParticle, maxParticles);
		this.active = false;
		this.lastCreationTime = 0;
		this.creationPeriodMillis = creationPeriodMillis;
	}

	/**
	 * updates the particles, should be called once per tick
	 * @param ellapsedTime time since last update call in ms
	 */
	public final void update(long ellapsedTime) {
		if (!active) {
			return;
		}
<<<<<<< HEAD
		modelBuffer.flip();
		// System.out.println(System.nanoTime() - start);
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
=======
>>>>>>> refs/remotes/origin/particle-wrap-up
		long now = System.currentTimeMillis();
		if (lastCreationTime == 0) {
			lastCreationTime = now;
		}
		Iterator<? extends GameObject> it = objects.iterator();
		while (it.hasNext()) {
			Particle particle = (Particle) it.next();
			if (particle.updateTtl(ellapsedTime) < 0) {
				it.remove();
			} else {
				updatePosition(particle, ellapsedTime);
			}
		}

		int length = objects.size();
		if (now - lastCreationTime >= this.creationPeriodMillis && length < maxObjects) {
			createParticle();
			this.lastCreationTime = now;
		}
		fillModelBuffer();
		passModelBuffer();
	}

<<<<<<< HEAD
	public void render(Vector4f clipPlane) {
		// Iterator<? extends GameObject> it = particles.iterator();
		// while (it.hasNext()) {
		// Particle particle = (Particle) it.next();
		// particle.render(clipPlane);
		// }

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

		// randomize velocity
		float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float xAngleInc = sign * (float) Math.random() * this.speedRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float yAngleInc = sign * (float) Math.random() * this.speedRndRange;
		float z = particle.velocity.z;
		float y = z * (float) (Math.sin(Math.toRadians(particle.angleY + yAngleInc)));
		float x = z * (float) (Math.sin(Math.toRadians(particle.angleX + xAngleInc)));
		particle.velocity = new Vector3f(x, y, z);

		// randomize position
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncX = sign * (float) Math.random() * this.positionRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncY = sign * (float) Math.random() * this.positionRndRange;
		sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float posIncZ = sign * (float) Math.random() * this.positionRndRange;
		particle.translate(posIncX, posIncY, posIncZ);

		// randomize scale
		float scaleInc = sign * (float) Math.random() * this.scaleRndRange;
		particle.setScale(.1f + scaleInc, .1f + scaleInc, .1f + scaleInc);

		particles.add(particle);
	}

	/**
	 * Updates a particle position
	 * 
	 * @param particle
	 *            The particle to update
	 * @param elapsedTime
	 *            Elapsed time in milliseconds
	 */
	private void updatePosition(Particle particle, long elapsedTime) {
		Vector3f speed = particle.velocity;
		float delta = elapsedTime / 1000f;
		float dx = speed.x * delta;
		float dy = speed.y * delta;
		float dz = speed.z * delta;
		// System.out.println(dz);
		particle.translate(dx, dy, dz);
	}

	public void activate() {
		this.active = true;
	}
=======
	/**
	 * used to create a new particle
	 * must be overridden by subclasses
	 */
	protected abstract void createParticle();
>>>>>>> refs/remotes/origin/particle-wrap-up

	/**
	 * used to update individual particles
	 * must be overridden by subclasses
	 * @param particle the particle to be updated
	 * @param elapsedTime time since last update call
	 */
	protected abstract void updatePosition(Particle particle, long elapsedTime);
}
