package world;

import java.util.ArrayList;

import graphics.Window;
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

	public void update() {
		float cameraX = graphics.GraphicsManager.camera.pos.x;
		float cameraY = graphics.GraphicsManager.camera.pos.y;

		int chunkX = (int) (cameraX / Chunk.SIZE / 2);
		int chunkY = (int) (cameraY / Chunk.SIZE / 2);
		/*
		 * This commented line is dumb and should be removed entirly but exists to show you what not todo
		 */
		//if (!Window.chunkLoader.chunks.get(new int[] { chunkX,chunkY })) {
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					Window.chunkLoader.chunksToLoad.add(new int[] { chunkX + x,chunkY + y });
				}
			}
		//}

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
