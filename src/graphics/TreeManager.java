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

public class TreeManager extends Geyser {

	public Queue<Particle> treesToAdd = new LinkedList<Particle>();

	public TreeManager(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
		super(baseParticle, maxParticles, creationPeriodMillis);
		colour = new Vector4f(.2f, 1, .2f, 1);
	}

	@Override
	protected void updatePosition(Particle particle, long elapsedTime) {
		return;
	}

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
		if (!treesToAdd.isEmpty()) {
			objects.add(treesToAdd.poll());
		}
	}
}
