package particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import graphics.GraphicsManager;
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

	InstancedVAO vao;

	public ParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		particles = new ArrayList<Particle>();
		this.baseParticle = baseParticle;
		this.maxParticles = maxParticles;
		this.active = false;
		this.lastCreationTime = 0;
		this.creationPeriodMillis = creationPeriodMillis;
		vao = new InstancedVAO(baseParticle.getVAO(), 50);
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
		//Iterator<? extends GameObject> it = particles.iterator();
		//while (it.hasNext()) {
		//	Particle particle = (Particle) it.next();
		//	particle.render(clipPlane);
		//}
		
		vao.renderListInstanced(particles, GraphicsManager.camera.view, clipPlane);
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
