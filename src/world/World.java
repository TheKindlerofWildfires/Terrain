package world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import graphics.WaterFBO;
import graphics.Window;
import maths.Triangle;
import maths.Vector2i;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;

public class World {
	public static Perlin noise;
	public static int perlinSeed;
	public static final int LOAD_DIST = 2;

	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public static Set<Vector2i> loadedChunks = new HashSet<Vector2i>();

	Water water;
	WaterFBO waterFBO;

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
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				loadedChunks.add(new Vector2i(0 + x, 0 + y));
			}
		}
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
	public void render(Vector4f clipPlane) {
		chunks.stream().forEach(c -> c.render(clipPlane));
	}

	public void addChunk(Chunk c) {
		if (!c.isGL) {
			c.makeGL();
		}
		chunks.add(c);
	}

	private static Chunk getChunk(Vector3f position) {
		int chunkX = Math.round(position.x / 2 / Chunk.SIZE);
		int chunkY = Math.round(position.y / 2 / Chunk.SIZE);
		for (Chunk chunk : chunks) {
			if (chunkX == chunk.chunkX && chunkY == chunk.chunkY) {
				return chunk;
			}
		}
		return null;
	}

	/**
	 * finds triangle nearest position
	 * @param position the position we are looking at
	 * @return nearest triangle
	 */
	public static Triangle getTerrainTriangle(Vector3f position) {
		Chunk chunk = getChunk(position);
		float minDist = 100;
		Triangle closestTri = chunk.terrain.get(0);
		for (Triangle tri : chunk.terrain) {
			float dist = position.subtract(
					tri.getCircumcenter().add(new Vector3f(2f * chunk.chunkX, 2f * chunk.chunkY, 0)).scale(Chunk.SIZE))
					.length();
			if (dist < minDist) {
				minDist = dist;
				closestTri = tri;
			}
		}
		System.out.println(minDist);
		return closestTri;
	}
}
