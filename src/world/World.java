package world;

import graphics.GraphicsManager;
import graphics.Window;
import maths.Vector2i;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class World {
	public static Perlin noise;
	public static Perlin noisy;
	public static Perlin detail;
	public static int perlinSeed;
	public static final int LOAD_DIST = 6;
	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();

	public static Set<Vector2i> loadedChunks = new HashSet<Vector2i>();
	public static int planetType;
	public static int[] dimensionID = new int[2];
	public static int difficulty;

	/**
	 * Building better worlds tl;Dr: Uses poisson disk, delauny and perlin noise
	 * to great a cool map
	 */
	public World(int type, int seed) {
		perlinSeed = seed;
		planetType = type;
		loadProperties();
		noise.setSeed(perlinSeed);
		noisy.setSeed(perlinSeed * perlinSeed);
		detail.setSeed(perlinSeed / 2);
		dimensionID[0] = perlinSeed;
		dimensionID[1] = planetType;
		difficulty = checkDif(type);
	}

	private int checkDif(int type) {
		int r = 1;
		switch(type){
		case Biome.JUNGLEWORLD:
			r = 1;
			break;
		case Biome.DESERTWORLD:
			r = 1;
			break;
		case Biome.SWAMPWORLD:
			r = 1;
			break;
		case Biome.TAIGAWORLD:
			r = 2;
			break;
			/*
		public static final int OCEANWORLD = 4; //diff 2
		public static final int MOUNTAINWORLD = 5;//diff 2
		public static final int HIGHLANDSWORLD = 6;//diff 2.5
		public static final int ICEWORLD = 7; //diff 3
		public static final int VULCANICWORLD = 8; //diff 3
		public static final int RADIOACTIVEWORLD = 9; //diff 3
		*/
		}
		return r;
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

	}

	public void renderTrees(Vector4f clipPlane) {
		//change water colour
		//Shader.start(ShaderManager.waterShader);
		//Shader.setUniform3f("waterColour", new Vector3f(0,0,0)); //makes water black
		
		//sunrise change tree colour
		//trees.colour = new colour;
	}

	public void addChunk(Chunk c) {
		if (!c.isGL) {
			c.makeGL();
			// c.details.stream().forEach(d -> d.makeGL());
		}
		chunks.add(c);

	}
}
