package world;

import java.util.ArrayList;

import maths.Delaunay;
import noiseLibrary.module.source.Perlin;

public class World {
	Perlin noise;
	public static int perlinSeed;
	public static long aTime;
	public static int iters;

	ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	
	/**
	 * Building better worlds
	 * 		tl;Dr: Uses poisson disk, delauny and perlin noise to great a cool map
	 */
	public World() {
		noise = new Perlin();
		noise.setFrequency(0.1);
		noise.setLacunarity(2);
		noise.setOctaveCount(30);
		noise.setPersistence(0.4);
		noise.setSeed(perlinSeed);
		/*
		 * Testing reveals 
		 * Perlin:1/2
		 * Delauny:1/4
		 * Poisson:1/4
		 */
		for(int x=0;x<10;x++){
			for(int y=0;y<10;y++){
				chunks.add(new Chunk(noise,x,y));
			}
		}
	}

	/**
	 * Going to assume this drawls
	 */
	public void render() {
		chunks.stream().forEach(c->c.render());
	}
}
