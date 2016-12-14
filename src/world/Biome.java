package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;
/**
 * @author TheKingInYellow
 */
public class Biome {
	public static float SIZE = Chunk.SIZE;
	public static float WATERLEVEL = Chunk.WATERLEVEL;
	public static float BEACHSIZE = Chunk.BEACHSIZE;
	public static float TREELINE = Chunk.TREELINE;
	static float RAINSCALER = 20;
	static float TEMPSCALER = 20;
	static float DETAILSCALER = 1;
	public static float high = 40;
	public static float med = 20;
	public static float low = 0;
	static Perlin noise = World.noise;
	static Perlin noisy = World.noisy;

	public static float[] getValue(Vector3f centre, Vector3f point, boolean print) {
		float r, b, g, h;
		r = b = g = h = 0;
		String type = null;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;

		double rain = Math.abs(noise.getValue(point.x / RAINSCALER, point.y / RAINSCALER, elev)) * 100;
		double temp = Math.abs(noisy.getValue(point.x / TEMPSCALER, point.y / TEMPSCALER, elev)) * 100;
		double detail = Math.abs(noise.getValue(Math.pow(point.x / DETAILSCALER,2),Math.pow(point.y / DETAILSCALER,2), 0.2));
		double color = elev /SIZE*2;
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
			type = "mountain";
		}
		if (rain < med && temp < med) {
			type = "ocean";
		}
		if (type == null) {
			System.out.println("not a biome");
		}
		if (type == "rainForest") {// Tall trees and vines?
			h = WATERLEVEL*7/8;
			h += (float) elev; 
			r = (float) (0.04 * (color + 0.2));
			g = (float) (0.20 * (color + 0.2));
			b = (float) (0.05 * (color + 0.2));
		}
		if (type == "seasonalForest") {//After each day night cycle cycle colors, leaf particle from tree
			h = WATERLEVEL*2/3;
			h += (float) elev; 
			r = (float) (0.20 * (color+.1));
			g = (float) (0.50 * (color+.1));
			b = (float) (0.04 * (color+.1));
		}
		if (type == "forest") {//These should have oak tree forests and on hills a single big tree
			h = WATERLEVEL*2/3;
			h += (float) elev; 
			r = (float) (0.10 * (color+.1));
			g = (float) (0.36 * (color+.1));
			b = (float) (0.14 * (color+.1));
		}
		if (type == "swamp") { //This should have fountains and small reeds
			h = WATERLEVEL*7/8;
			h += (float) elev/8; 
			r = (float) (0.25 * (color+.1));
			g = (float) (0.41 * (color+.1));
			b = (float) (0.11 * (color+.1));
		}
		if (type == "desert") { //sand storm effect, with oasis vegetation 
			h = WATERLEVEL;
			h += (float) elev; 
			r = (float) (0.86 * (color+.2));
			g = (float) (0.80 * (color+.2));
			b = (float) (0.60 * (color+.2));
		}
		if (type == "taiga") {//some conifers and maybe ice the water
			h = WATERLEVEL*7/8;
			h += (float) elev*1.5; 
			r = (float) (0.90 * (color+.1));
			g = (float) (0.88 * (color+.1));
			b = (float) (0.83 * (color+.1));
		}
		if (type == "ocean") { // add a reef or maybe a 
			h += (float) elev/8;
			r = (float) (0.10 * (color+.1));
			g = (float) (0.10 * (color+.1));
			b = (float) (0.40 * (color+.1));
		}
		if (type == "mountain") { // Caps are either mountain lake, lava spout, or caps  
			h = WATERLEVEL;
			h += (float) elev*elev/2;
			r = (float) (0.90 * (color+.1));
			g = (float) (0.91 * (color+.1));
			b = (float) (0.89 * (color+.1));
		}
		if (type == "savanna") {// some lonely trees and tall grass with an occasional rock
			h = WATERLEVEL;
			h += (float) elev/2;
			r = (float) (0.90 * (color+.1));
			g = (float) (0.77 * (color+.1));
			b = (float) (0.10 * (color+.1));
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
		if(detail>1){
			/*
			r = 1;
			g = 1;
			b = 1;
			*/
		}
		float[] returns = { r, b, g, h };
		return returns;
	}
}
