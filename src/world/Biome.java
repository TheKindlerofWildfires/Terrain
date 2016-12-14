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
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
		float RAINSCALER = 50;
		float TEMPSCALER = 100;
		double rain = Math.abs(noise.getValue(point.x / RAINSCALER, point.y / RAINSCALER, 0.1)) * 100;
		double temp = Math.abs(noise.getValue(point.x / TEMPSCALER, point.y / TEMPSCALER, 0.2)) * 100;
		double color = Math.abs(noise.getValue(point.x, point.y, 0.1));
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
			type = "savanna";
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
		if (type == "rainForest") {// Tall tree me! --> Vines?
			h = WATERLEVEL*7/8;
			h += (float) elev; 
			r = (float) (0.04 * (color + 0.2));
			g = (float) (0.20 * (color + 0.2));
			b = (float) (0.05 * (color + 0.2));
		}
		if (type == "seasonalForest") {//Should these change color?
			h = WATERLEVEL*2/3;
			h += (float) elev; 
			r = (float) (0.20 * (color+.1));
			g = (float) (0.50 * (color+.1));
			b = (float) (0.04 * (color+.1));
		}
		if (type == "forest") {//maybe animal life 
			h = WATERLEVEL*2/3;
			h += (float) elev; 
			r = (float) (0.05 * (color+.1));
			g = (float) (0.18 * (color+.1));
			b = (float) (0.07 * (color+.1));
		}
		if (type == "swamp") { //geisers
			h = WATERLEVEL*7/8;
			h += (float) elev/8; 
			r = (float) (0.25 * (color));
			g = (float) (0.41 * (color));
			b = (float) (0.11 * (color));
		}
		if (type == "desert") { //maybe a sand storm effect
			h = WATERLEVEL;
			h += (float) elev; 
			r = (float) (0.78 * (color+.2));
			g = (float) (0.70 * (color+.2));
			b = (float) (0.55 * (color+.2));
		}
		if (type == "taiga") {
			h = WATERLEVEL*7/8;
			h += (float) elev*2; 
			r = (float) (0.90 * (color));
			g = (float) (0.88 * (color));
			b = (float) (0.83 * (color));
		}
		if (type == "snow") {
			h = WATERLEVEL/2;
			h += (float) elev;
			r = (float) (0.88 * (color));
			g = (float) (0.93 * (color));
			b = (float) (0.95 * (color));
		}
		if (type == "tundra") {
			h = WATERLEVEL;
			h += (float) elev/2;
			r = (float) (0.90 * (color));
			g = (float) (0.90 * (color));
			b = (float) (0.90 * (color));
		}
		if (type == "savanna") {
			h = WATERLEVEL;
			h += (float) elev/2;
			r = (float) (0.98 * (color+.1));
			g = (float) (0.79 * (color+.1));
			b = (float) (0.09 * (color+.1));
		}
		if(h>WATERLEVEL+BEACHSIZE){
			r*=0.5;
			g*=0.5;
			b*=0.5;
		}
		if(h>WATERLEVEL+BEACHSIZE*2){
			r*=0.5;
			g*=0.5;
			b*=0.5;
		}
		if (print) {
			System.out.println(type);
		}
		float[] returns = { r, b, g, h };
		return returns;
	}
}
