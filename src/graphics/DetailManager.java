package graphics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import maths.Vector4f;
import object.GameObject;
import particles.Geyser;
import particles.Particle;
import world.Chunk;
import world.World;

/**
 * Manages details, needs a rework
 * 
 * @author TheKingInYellow
 *
 */
public class DetailManager extends Geyser {

	public Queue<Particle> detailsToAdd = new LinkedList<Particle>();

	/**
	 * Initialises the detail manager
	 * 
	 * @param baseParticle
	 * @param maxParticles
	 * @param creationPeriodMillis
	 * @param colour
	 */
	public DetailManager(Particle baseParticle, int maxParticles, long creationPeriodMillis, Vector4f colour) {
		super(baseParticle, maxParticles, creationPeriodMillis, colour);
		this.colour = colour;// new Vector4f(.2f, 1, .2f, 1);
	}

	/**
	 * This class bothers me--> I don't know what it does
	 */
	@Override
	protected void updatePosition(Particle particle, long elapsedTime) {
		return;
	}

	/**
	 * Creates a 'particle' at the position, but it needs reworking
	 */
	@Override
	protected void createParticle() {
		Iterator<? extends GameObject> it = objects.iterator();

		float cameraX = GraphicsManager.camera.pos.x;
		float cameraY = GraphicsManager.camera.pos.y;

		int chunkX = Math.round(cameraX / 2 / Chunk.SIZE);
		int chunkY = Math.round(cameraY / 2 / Chunk.SIZE);

		while (it.hasNext()) {
			Particle particle = (Particle) it.next();
			int particleChunkX = Math.round(particle.position.x / 2 / Chunk.SIZE);
			int particleChunkY = Math.round(particle.position.y / 2 / Chunk.SIZE);

			if (Math.abs(particleChunkX - chunkX) > World.LOAD_DIST
					|| Math.abs(particleChunkY - chunkY) > World.LOAD_DIST) {
				it.remove();
			}
		}
		if (!detailsToAdd.isEmpty()) {
			objects.add(detailsToAdd.poll());
		}
	}
}
