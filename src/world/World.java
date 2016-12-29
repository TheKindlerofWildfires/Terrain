package world;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import graphics.GraphicsManager;
import graphics.Window;
import maths.Vector2i;
import maths.Vector4f;
<<<<<<< HEAD
=======
import models.ModelManager;
import models.VertexArrayObject;
import noiseLibrary.NoiseQuality;
>>>>>>> master
import noiseLibrary.module.source.Perlin;

public class World {
	public static Perlin noise;
	public static Perlin noisy;
	public static Perlin detail;
	public static int perlinSeed;
<<<<<<< HEAD
	public static final int LOAD_DIST = 6;
=======
	public static final int LOAD_DIST = 9;
>>>>>>> master

	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	public static Set<Vector2i> loadedChunks = new HashSet<Vector2i>();
	public static double tracker;

	/**
	 * Building better worlds tl;Dr: Uses poisson disk, delauny and perlin noise
	 * to great a cool map
	 */
	public World() {
		loadProperties();
		noise.setSeed(perlinSeed);
<<<<<<< HEAD
		noisy.setSeed(perlinSeed * perlinSeed);
		detail.setSeed(perlinSeed / 2);

=======
		noisy.setSeed(perlinSeed+1);
		System.out.println(perlinSeed);

		noise.setNoiseQuality(NoiseQuality.FAST);
		noisy.setNoiseQuality(NoiseQuality.FAST);

		try {
			tree = ModelManager.loadGlModel("resources/models/tree.obj").vao;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
>>>>>>> master
	}

	private void loadProperties() {
		System.out.println("Loading properties from resources/properties/world.properties");

		Properties props = new Properties();
		try {
			FileReader reader = new FileReader("resources/properties/world.properties");
			props.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		noise = new Perlin();
		noise.setFrequency(Float.parseFloat(props.getProperty("perlinFrequency")));
		noise.setLacunarity(Float.parseFloat(props.getProperty("perlinLacunarity")));
		noise.setOctaveCount(Integer.parseInt(props.getProperty("perlinOctaveCount")));

		noisy = new Perlin();
		noisy.setFrequency(0.008);
		noisy.setLacunarity(2.5);
		noisy.setOctaveCount(10);
		noisy.setPersistence(.4);

		detail = new Perlin();

		Chunk.SIZE = Float.parseFloat(props.getProperty("chunkSize"));

		Chunk.WATERLEVEL = Chunk.SIZE / Float.parseFloat(props.getProperty("waterlevelDivisor"));
		Chunk.BEACHSIZE = Chunk.SIZE / Float.parseFloat(props.getProperty("beachSizeDivisor"));
		Chunk.TREELINE = Chunk.SIZE / Float.parseFloat(props.getProperty("treelineDivisor"));
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
	public void renderLand(Vector4f clipPlane) {
		chunks.stream().forEach(c -> c.render(clipPlane));
		// chunks.stream().forEach(c->c.details.stream().forEach(d ->
		// d.render(clipPlane)));
	}

	public void addChunk(Chunk c) {
		if (!c.isGL) {
			c.makeGL();
			// c.details.stream().forEach(d -> d.makeGL());
		}
		chunks.add(c);

	}
}
