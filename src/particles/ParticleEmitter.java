package particles;

import java.util.Iterator;

import graphics.Instancer;
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
}
