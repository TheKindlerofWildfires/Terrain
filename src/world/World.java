package world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import graphics.GraphicsManager;
import graphics.Window;
import maths.Triangle;
import maths.Vector2i;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;
import object.GameObject;

public class World {
	public static Perlin noise;
	public static int perlinSeed;
	public static final int LOAD_DIST = 5;

	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public static ArrayList<Vector3f> treePositions = new ArrayList<Vector3f>();
	public static ArrayList<Vector3f> seiweedsPositions = new ArrayList<Vector3f>();

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
		noise.setSeed(perlinSeed);
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
		float cameraX = GraphicsManager.camera.pos.x;
		float cameraY = GraphicsManager.camera.pos.y;

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
		if (Window.chunkLoader.chunksToLoad.size() != 0) {
			synchronized (Window.chunkLoader.lock) {
				Window.chunkLoader.lock.notify();
			}
		}
		chunks.removeIf(c -> c.chunkX > chunkX + LOAD_DIST || c.chunkX < chunkX - LOAD_DIST
				|| c.chunkY > chunkY + LOAD_DIST || c.chunkY < chunkY - LOAD_DIST);
		loadedChunks.removeIf(v -> v.x > chunkX + LOAD_DIST || v.x < chunkX - LOAD_DIST || v.y > chunkY + LOAD_DIST
				|| v.y < chunkY - LOAD_DIST);
	}

	/**
	 * Going to assume this drawls
	 */
	public void render(Vector4f clipPlane) {
		chunks.stream().forEach(c -> c.render(clipPlane));
		//foliage.stream().forEach(f -> f.render(clipPlane));
	}

	public void addChunk(Chunk c) {
		if (!c.isGL) {
			c.makeGL();
		}
		chunks.add(c);
		c.foliage.stream().forEach(f -> f.makeGL());
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
