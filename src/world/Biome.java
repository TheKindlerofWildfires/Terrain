package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class Biome {
	public static float SIZE = Chunk.SIZE;
	public static float WATERLEVEL = Chunk.WATERLEVEL;
	public static float BEACHSIZE = Chunk.BEACHSIZE;
	public static float TREELINE = Chunk.TREELINE;
	public static float high = 50;
	public static float med = 25;
	public static float low = 0;
	static Perlin noise = Chunk.noise;

	public static float[] getValue(Vector3f centre, Vector3f point, boolean print) {
		float r, b, g, h;
		r = b = g = h = 0;
		String type = null;
		noise.setSeed(World.perlinSeed);
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
		float RAINSCALER = 1;
		float TEMPSCALER = 1;
		noise.setSeed(World.rainSeed);
		double rain = Math.abs(noise.getValue(point.x / RAINSCALER, point.y / RAINSCALER, 0.1)) * 100;
		noise.setSeed(World.tempSeed);
		double temp = Math.abs(noise.getValue(point.x / TEMPSCALER, point.y / TEMPSCALER, 0.1)) * 100;
		noise.setSeed(World.perlinSeed);
		double color = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
		temp = 26;
		rain = 26;
		if (rain > high && temp > high) {
			type = "rainForest";
		}
		if (rain > med && rain < high && temp > med && temp < high) {
			type = "seasonalForest";
		}
		if (rain < med && temp > high) {
			type = "desert";
		}
		if (rain >high && temp <med) {
			type = "taiga";
		}
		if (rain > high && temp > med && temp < high) {
			type = "swamp";
		}
		if (rain > med && rain < high && temp > high ) {
			type = "forest";
		}
		if (rain < med && temp < high && temp > med) {
			type = "savaana";
		}
		if (temp < med && rain < high && rain > med) {
			type = "tundra";
		}
		if (rain < med && temp < med) {
			type = "snow";
		}
		if (type == null) {
			System.out.println("not a biome");
		}
		if (type == "rainForest") {
			r = (float) (0.6 / (color + 1));
			g = (float) (0.6 / (color + 1));
			b = (float) (0.6 / (color + 1));
		}
		if (type == "seasonalForest") {
			r = (float) (0.4 / (color + 1));
			g = (float) (0.4 / (color + 1));
			b = (float) (0.6 / (color + 1));
		}
		if (type == "forest") {
			r = (float) (0.6 / (color + 1));
			g = (float) (0.4 / (color + 1));
			b = (float) (0.4 / (color + 1));
		}
		if (type == "swamp") {
			r = (float) (0.4 / (color + 1));
			g = (float) (0.6 / (color + 1));
			b = (float) (0.4 / (color + 1));
		}
		
		if (type == "desert") {
			r = (float) (0.6 / (color + 1));
			g = (float) (0.6 / (color + 1));
			b = (float) (0.6 / (color + 1));
		}
		if (type == "taiga") {
			r = (float) (0.4 / (color + 1));
			g = (float) (0.4 / (color + 1));
			b = (float) (0.6 / (color + 1));
		}
		if (type == "snow") {
			r = (float) (0.6 / (color + 1));
			g = (float) (0.4 / (color + 1));
			b = (float) (0.4 / (color + 1));
		}
		if (type == "tundra") {
			r = (float) (0.4 / (color + 1));
			g = (float) (0.6 / (color + 1));
			b = (float) (0.4 / (color + 1));
		}
		if (type == "savaana") {
			r = (float) (0.4 / (color + 1));
			g = (float) (0.6 / (color + 1));
			b = (float) (0.4 / (color + 1));
		}
		if (print) {
			System.out.println(type);
		}
		//h = (float) elev;
		r = (float) (0.6 / (color + 1));
		g = (float) (0.6 / (color + 1));
		b = (float) (0.6 / (color + 1));
		h = WATERLEVEL *2;
		float[] returns = { r, b, g, h };
		return returns;
	}
}
