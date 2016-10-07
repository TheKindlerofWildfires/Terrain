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
	public static final int LOAD_DIST = 2;

	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public static Set<Vector2i> loadedChunks = new HashSet<Vector2i>();

	/**
	 * Building better worlds
	 * 		tl;Dr: Uses poisson disk, delauny and perlin noise to great a cool map
	 */
	public World() {
		noise = new Perlin();
		noise.setFrequency(0.02);
		noise.setLacunarity(2);
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

	public void update() {
		float cameraX = graphics.GraphicsManager.camera.pos.x;
		float cameraY = graphics.GraphicsManager.camera.pos.y;

		int chunkX = Math.round(cameraX / 2 / Chunk.SIZE);
		int chunkY = Math.round(cameraY / 2 / Chunk.SIZE);
		Vector2i xy = new Vector2i(chunkX, chunkY);

		for (int x = -LOAD_DIST; x < LOAD_DIST + 1; x++) {
			for (int y = -LOAD_DIST; y < LOAD_DIST + 1; y++) {
				xy = new Vector2i(x + chunkX, y + chunkY);
				if (!setContains(loadedChunks, xy)) {
					loadedChunks.add(xy);
					Window.chunkLoader.chunksToLoad.add(xy);
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
