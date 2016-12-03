package particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maths.Vector3f;
import object.GameObject;

public class ParticleEmitter {

	private int maxParticles;
	private boolean active;
	private final Particle baseParticle;
	private long creationPeriodMillis;
	private long lastCreationTime;
	private float speedRndRange;
	private float positionRndRange;
	private float scaleRndRange;
	private List<Particle> particles;

	public ParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		particles = new ArrayList<Particle>();
		this.baseParticle = baseParticle;
		this.maxParticles = maxParticles;
		this.active = false;
		this.lastCreationTime = 0;
		this.creationPeriodMillis = creationPeriodMillis;
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

	private void createParticle() {
		Particle particle = new Particle(baseParticle);
		// Add a little bit of randomness of the particle
		float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
		float speedInc = sign * (float) Math.random() * this.speedRndRange;
		float posInc = sign * (float) Math.random() * this.positionRndRange;
		float scaleInc = sign * (float) Math.random() * this.scaleRndRange;
		particle.translate(posInc, posInc, posInc);
		particle.velocity.add(new Vector3f(speedInc, speedInc, speedInc));
		particle.setScale(1 + scaleInc, 1 + scaleInc, 1 + scaleInc);
		particles.add(particle);
	}

	/**
	 * Updates a particle position
	 * @param particle The particle to update
	 * @param elapsedTime Elapsed time in milliseconds
	 */
	public void updatePosition(Particle particle, long elapsedTime) {
		Vector3f speed = particle.velocity;
		float delta = elapsedTime / 1000.0f;
		float dx = speed.x * delta;
		float dy = speed.y * delta;
		float dz = speed.z * delta;
		particle.translate(dx, dy, dz);
	}
}
