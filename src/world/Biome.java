package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

/**
 * @author TheKingInYellow
 */
public abstract class Biome {
	public static float SIZE = Chunk.SIZE;
	public static float WATERLEVEL = Chunk.WATERLEVEL;
	public static float BEACHSIZE = Chunk.BEACHSIZE;
	public static float TREELINE = Chunk.TREELINE;
	static float RAINSCALER = 20;
	static float TEMPSCALER = 20;
	public static float high = 40;
	public static float med = 20;
	public static float low = 0;
	static Perlin noise = World.noise;
	static Perlin noisy = World.noisy;
	static float maxSize = 0;

	public static final int RAINFOREST = 0;
	public static final int SEASONALFOREST = 1;
	public static final int DESERT = 2;
	public static final int TAIGA = 3;
	public static final int SWAMP = 4;
	public static final int FOREST = 5;
	public static final int OCEAN = 6;
	public static final int MOUNTAIN = 7;
	public static final int SAVANNA = 8;

	public static float[] getValue(Vector3f centre, Vector3f point, boolean print) {
		float r, b, g, h;
		r = b = g = h = 0;
		int type = -1;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
		double color = elev / SIZE * 2;
		double rain = Math.abs(noise.getValue(point.x / RAINSCALER, point.y / RAINSCALER, color)) * 100;
		double temp = Math.abs(noisy.getValue(point.x / TEMPSCALER, point.y / TEMPSCALER, color)) * 100;

		if (rain > high && temp > high) {
			type = RAINFOREST;
		}
		if (rain > med && rain < high && temp > med && temp < high) {
			type = SEASONALFOREST;
		}
		if (rain < med && temp > high) {
			type = DESERT;
		}
		if (rain > high && temp < med) {
			type = TAIGA;
		}
		if (rain > high && temp > med && temp < high) {
			type = SWAMP;
		}
		if (rain > med && rain < high && temp > high) {
			type = FOREST;
		}
		if (rain < med && temp < high && temp > med) {
			type = SAVANNA;
		}
		if (temp < med && rain < high && rain > med) {
			type = OCEAN;
		}
		if (rain < med && temp < med) {
			type = MOUNTAIN;
		}
		if (type == -1) {
			System.out.println("not a biome");
		}
		//type = RAINFOREST;
		if (type == RAINFOREST) {
			h = WATERLEVEL * 7 / 8;
			maxSize = h + SIZE / 2;
			h += (float) elev;
			r = (float) (0.04 * (color + 0.2));
			g = (float) (0.20 * (color + 0.2));
			b = (float) (0.05 * (color + 0.2));
		}
		if (type == SEASONALFOREST) {
			h = WATERLEVEL * 1 / 2;
			maxSize = h + SIZE / 2;
			h += (float) elev;
			r = (float) (0.20 * (color + .1));
			g = (float) (0.50 * (color + .1));
			b = (float) (0.04 * (color + .1));
		}
		if (type == FOREST) {
			h = WATERLEVEL * 2 / 3;
			maxSize = h + SIZE / 2;
			h += (float) elev;
			r = (float) (0.10 * (color + .1));
			g = (float) (0.36 * (color + .1));
			b = (float) (0.14 * (color + .1));
		}
		if (type == SWAMP) {
			h = WATERLEVEL * 7 / 8;
			maxSize = h + SIZE / 16;
			h += (float) elev / 8;
			r = (float) (0.25 * (color + .1));
			g = (float) (0.41 * (color + .1));
			b = (float) (0.11 * (color + .1));
		}
		if (type == DESERT) {
			h = WATERLEVEL;
			maxSize = h + SIZE / 2;
			h += (float) elev;
			r = (float) (0.86 * (color + .2));
			g = (float) (0.80 * (color + .2));
			b = (float) (0.60 * (color + .2));
		}
		if (type == TAIGA) {
			h = WATERLEVEL * 6 / 8;
			maxSize = h + SIZE / 2 * 1.5f;
			h += (float) elev/2;
			r = (float) (0.90 * (color + .1));
			g = (float) (0.88 * (color + .1));
			b = (float) (0.83 * (color + .1));
		}
		if (type == OCEAN) {
			maxSize = h + SIZE / 16;
			h += (float) elev / 8;
			r = (float) (0.10 * (color + .1));
			g = (float) (0.10 * (color + .1));
			b = (float) (0.40 * (color + .1));
		}
		if (type == MOUNTAIN) {
			h = WATERLEVEL/32*31;
			maxSize = h + SIZE * SIZE / 4;
			h += (float) elev * elev / 2;
			r = (float) (0.11 * (color + .1));
			g = (float) (0.12 * (color + .1));
			b = (float) (0.10 * (color + .1));
			if (h > maxSize-4){
				//System.out.println(point.x + " " + point.y+ " " + point.z);
			}
		}
		if (type == SAVANNA) {
			h = WATERLEVEL*3/4;
			maxSize = h + SIZE / 2;
			h += (float) elev / 2;
			r = (float) (0.90 * (color + .1));
			g = (float) (0.77 * (color + .1));
			b = (float) (0.10 * (color + .1));
		}
		if (h > maxSize / 3) {
			r *= 0.65;
			g *= 0.65;
			b *= 0.65;
		}
		if (h > maxSize / 1.5) {
			r *= 0.65;
			g *= 0.65;
			b *= 0.65;
		}
		if (h > maxSize) {
			h = maxSize;
		}
		if (print) {
			System.out.println(type);
		}
		float[] returns = { r, b, g, h, type };
		return returns;
	}
}
