package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class Biome {
	public static float SIZE = Chunk.SIZE;
	public static float WATERLEVEL = Chunk.WATERLEVEL;
	public static float BEACHSIZE = Chunk.BEACHSIZE;
	public static float TREELINE = Chunk.TREELINE;
	static Perlin noise = Chunk.noise;
	public static float[] getValue(Vector3f centre, Vector3f point, boolean print) {
		float r, b, g, h;
		r=b=g=h=1;
		String type = null;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1));
		//remember to change seed
		float RAINSCALER = 20;
		float TEMPSCALER = 10;
		double rain = Math.abs(noise.getValue(point.x/RAINSCALER, point.y/RAINSCALER, 0.1))*100;
		double temp = Math.abs(noise.getValue(point.x/TEMPSCALER, point.y/TEMPSCALER, 0.2))*100;
		if(rain>75&&temp>75){
			type = "rainForest";
		}else if (rain>50&& temp>75){
			type = "seasonalForest";
		}else if (rain>75&& temp>50){
			type = "swamp";
		}else if (rain>50&& temp>50){
			type = "forest";
		}else if (rain>25&& temp>75){
			type = "savaana";
		}else if (rain>25&& temp>50){
			type = "wood";
		}else if (rain>25&& temp>25){
			type = "taiga";
		}else if (rain>0&& temp>75){
			type = "desert";
		}else if (rain>0&& temp>25){
			type = "grassDesert";
		}else{
			type = "tunda";
		}
		if(type == "rainForest"){
			r = 0;
		}
		if(type == "tundra"){
			r = 0.5f;
		}
		if(print){
			System.out.println(type);
		}
		h = (float) elev;
		float[] returns = { r, b, g, h };
		return returns;
	}
}
