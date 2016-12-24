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
		double color = elev /SIZE*2;
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
		if (rain >high && temp <med) {
			type = TAIGA;
		}
		if (rain > high && temp > med && temp < high) {
			type = SWAMP;
		}
		if (rain > med && rain < high && temp > high ) {
			type = FOREST;
		}
		if (rain < med && temp < high && temp > med) {
			type = SAVANNA;
		}
		if (temp < med && rain < high && rain > med) {
			type = MOUNTAIN;
		}
		if (rain < med && temp < med) {
			type = OCEAN;
		}
		if (type == -1) {
			System.out.println("not a biome");
		}
		type = TAIGA;
		if (type == RAINFOREST) {// Tall trees and vines?
			
			h = WATERLEVEL*7/8;
			maxSize = h+ SIZE/2;
			h += (float) elev; 
			r = (float) (0.04 * (color + 0.2));
			g = (float) (0.20 * (color + 0.2));
			b = (float) (0.05 * (color + 0.2));
		}
		if (type == SEASONALFOREST) {//After each day night cycle cycle colors, leaf particle from tree
			h = WATERLEVEL*2/3;
			maxSize = h+ SIZE/2;
			h += (float) elev; 
			r = (float) (0.20 * (color+.1));
			g = (float) (0.50 * (color+.1));
			b = (float) (0.04 * (color+.1));
		}
		if (type == FOREST) {//These should have oak tree forests and on hills a single big tree
			h = WATERLEVEL*2/3;
			maxSize = h+ SIZE/2;
			h += (float) elev; 
			r = (float) (0.10 * (color+.1));
			g = (float) (0.36 * (color+.1));
			b = (float) (0.14 * (color+.1));
		}
		if (type == SWAMP) { //This should have fountains and small reeds
			h = WATERLEVEL*7/8;
			maxSize = h+ SIZE/16;
			h += (float) elev/8; 
			r = (float) (0.25 * (color+.1));
			g = (float) (0.41 * (color+.1));
			b = (float) (0.11 * (color+.1));
		}
		if (type == DESERT) { //sand storm effect, with oasis vegetation 
			h = WATERLEVEL;
			maxSize = h+ SIZE/2;
			h += (float) elev; 
			r = (float) (0.86 * (color+.2));
			g = (float) (0.80 * (color+.2));
			b = (float) (0.60 * (color+.2));
		}
		if (type == TAIGA) {//some conifers and maybe ice the water
			h = WATERLEVEL*7/8;
			maxSize = h+ SIZE/2*1.5f;
			h += (float) elev*1.5; 
			r = (float) (0.90 * (color+.1));
			g = (float) (0.88 * (color+.1));
			b = (float) (0.83 * (color+.1));
		}
		if (type == OCEAN) { // add a reef or maybe a 
			maxSize = h+ SIZE/16;
			h += (float) elev/8;
			r = (float) (0.10 * (color+.1));
			g = (float) (0.10 * (color+.1));
			b = (float) (0.40 * (color+.1));
		}
		if (type == MOUNTAIN) { // Caps are either mountain lake, lava spout, or caps  
			h = WATERLEVEL;
			maxSize = h+ SIZE*SIZE/4;
			h += (float) elev*elev/2;
			r = (float) (0.90 * (color+.1));
			g = (float) (0.91 * (color+.1));
			b = (float) (0.89 * (color+.1));
		}
		if (type == SAVANNA) {// some lonely trees and tall grass with an occasional rock
			h = WATERLEVEL;
			maxSize = h+ SIZE/2;
			h += (float) elev/2;
			r = (float) (0.90 * (color+.1));
			g = (float) (0.77 * (color+.1));
			b = (float) (0.10 * (color+.1));
		}
		if(h>maxSize/3){
			r*=0.5;
			g*=0.5;
			b*=0.5;
		}
		if(h>maxSize/1.5){
			r*=0.5;
			g*=0.5;
			b*=0.5;
		}
		if(h>maxSize){
			h = maxSize;
		}
		if (print) {
			System.out.println(type);
		}		
		
		r = (float) (r + color/75);
		b = (float) (b + rain/750);
		g = (float) (g+ temp/750);
		float[] returns = { r, b, g, h,type };
		return returns;
	}
}
