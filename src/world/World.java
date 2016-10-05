package world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import graphics.Window;
import maths.Vector2i;
import noiseLibrary.module.source.Perlin;

public class World {
	public static Perlin noise;
	public static int perlinSeed;
	public static final int chunkY = 10;
	public static final int chunkX = 10;
	public static final int chunkS = chunkY * chunkX;
	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	/**
	 * Building better worlds
	 * 		tl;Dr: Uses poisson disk, delauny and perlin noise to great a cool map
	 */
	public World() {
		noise = new Perlin();
		noise.setFrequency(0.04);
		noise.setLacunarity(2.2);
		noise.setOctaveCount(10);
		//noise.setPersistence(0.4);
		noise.setSeed(perlinSeed);
		//	for (int x = 0; x < chunkX; x++) {
		//		for (int y = 0; y < chunkY; y++) {
		//			chunks.add(new Chunk(noise, x, y,true));
		//		}
		//	}
	}

	public boolean setContains(Set<?> set, Object o) {
		Iterator<?> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(o)) {
				return true;
			}
		}
		return false;
	}

	int renderDist = 2;

	public void update() {
		float cameraX = graphics.GraphicsManager.camera.pos.x;
		float cameraY = graphics.GraphicsManager.camera.pos.y;

		int chunkX = Math.round(cameraX / 2 / Chunk.SIZE);
		int chunkY = Math.round(cameraY / 2 / Chunk.SIZE);
		Vector2i xy = new Vector2i(chunkX, chunkY);

		if (!Window.chunkLoader.loadingChunks) {
			for (int x = -renderDist; x <= renderDist; x++) {
				for (int y = -renderDist; y <= renderDist; y++) {
					xy.x = chunkX + x;
					xy.y = chunkY + y;
					if (!setContains(new HashSet<Vector2i>(Window.chunkLoader.loaded), xy)) {
						Window.chunkLoader.chunksToLoad.add(xy);
					//	Window.chunkLoader.loadChunks();
						synchronized (Window.chunkLoader.lock) {
							Window.chunkLoader.wakeup = true;
							Window.chunkLoader.lock.notifyAll();
						}
					}
				}
			}
		}
	}

	/**
	 * Going to assume this drawls
	 */
	public void render() {
		chunks.stream().forEach(c -> c.render());
	}

	public void addChunk(Chunk c) {
		if (!c.isGL) {
			c.makeGL();
		}
		chunks.add(c);
	}
}
