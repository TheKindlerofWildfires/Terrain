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
		/*
		 * This is mitchell
		 * Signing off
		 * Each zone doesn't have enough space to itself
		 * Which is probably fixable but I'm so tired
		 * Its like 12:20
		 * And there is nothing but sad here
		 * 
		 */
		float r, b, g, h;
		r=b=g=h=1;
		String type = null;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1));
		double moist = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double temp = Math.abs(noise.getValue(point.x/8, point.y/8, 0.2));
		double trace = temp*SIZE;
		float bs = (int) (temp*4.5);
		if (bs == 0) {
			type = "ocean";//island, shallow, deep
		}
		if (bs == 1) {
			type = "wetland"; //swamp --> plains-->hill
		}
		if (bs == 2) {
			type = "mountainLake"; //hill--> mountain --> lake
		}
		if (bs == 3) {
			type = "mountainPeak"; //hill--> mountain --> peak
		}
		if (bs == 4|| bs == 5) {
			type = "tundra"; //ice--> tundra --> mountain
		}
		if (type == "ocean") {
			if(trace<WATERLEVEL){
				type = "depth";
				h = (float)(elev*SIZE/16-WATERLEVEL/2);
				b = (float) (0.3f/(moist + 1));
				g = (float) (0.1f/(moist + 1));
				r = (float) (0.0f/(moist + 1));
			}else if(trace > WATERLEVEL&& trace<WATERLEVEL+BEACHSIZE){
				type = "reef";
				h = (float)(elev*SIZE/8+WATERLEVEL/8);
				b = (float) (0.5f/(moist + 1));
				g = (float) (0.2f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if (trace > WATERLEVEL + BEACHSIZE && trace < TREELINE) {
				type = "shallows";
				h = (float)(elev*SIZE/8+WATERLEVEL/4);
				b = (float) (0.4f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.1f/(moist + 1));
			}else if (trace > TREELINE) {
				type = "island";
				h = (float)(elev*SIZE/2+WATERLEVEL/2);
				b = (float) (0.1f/(moist + 1));
				g = (float) (0.4f/(moist + 1));
				r = (float) (0.3f/(moist + 1));
			}
		}
		if (type == "wetland") {
			if(trace<WATERLEVEL){
				type = "swamp";
				h = (float)(elev*SIZE/32+31*WATERLEVEL/32);
				b = (float) (0.3f/(moist + 1));
				g = (float) (0.5f/(moist + 1));
				r = (float) (0.3f/(moist + 1));
			}else if(trace > WATERLEVEL&& trace<WATERLEVEL+BEACHSIZE){
				type = "plains";
				h = (float)(elev*SIZE/16+15*WATERLEVEL/16);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.4f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if (trace > WATERLEVEL + BEACHSIZE && trace < TREELINE) {
				type = "forest";//run forrest run!
				h = (float)(elev*SIZE/4+15*WATERLEVEL/16);
				b = (float) (0.1f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.1f/(moist + 1));
			}else if (trace > TREELINE) {
				type = "hill";
				h = (float)(elev*SIZE/2+3*WATERLEVEL/4);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}
		}
		if (type == "mountainLake") {
			if(trace<WATERLEVEL){
				type = "hill";
				h = (float)(elev*SIZE/2+3*WATERLEVEL/4);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if(trace > WATERLEVEL&& trace<WATERLEVEL+BEACHSIZE){
				type = "mountainEdge";
				h = (float)(elev*SIZE/2+WATERLEVEL);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.3f/(moist + 1));
			}else if (trace > WATERLEVEL + BEACHSIZE && trace < TREELINE) {
				type = "mountain";
				h = (float)(elev*SIZE/2+WATERLEVEL*2);
				b = (float) (0.1f/(moist + 1));
				g = (float) (0.2f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if (trace > TREELINE) {
				type = "lake";
				h = (float)(elev*SIZE/8);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.1f/(moist + 1));
				r = (float) (0.1f/(moist + 1));
			}
		}
		if (type == "mountainPeak") {
			if(trace<WATERLEVEL){
				type = "hill";
				h = (float)(elev*SIZE/2+3*WATERLEVEL/4);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if(trace > WATERLEVEL&& trace<WATERLEVEL+BEACHSIZE){
				type = "mountainEdge";
				h = (float)(elev*SIZE/2+WATERLEVEL);
				b = (float) (0.2f/(moist + 1));
				g = (float) (0.3f/(moist + 1));
				r = (float) (0.3f/(moist + 1));
			}else if (trace > WATERLEVEL + BEACHSIZE && trace < TREELINE) {
				type = "mountain";
				h = (float)(elev*SIZE/2+WATERLEVEL*2);
				b = (float) (0.1f/(moist + 1));
				g = (float) (0.2f/(moist + 1));
				r = (float) (0.2f/(moist + 1));
			}else if (trace > TREELINE) {
				type = "mountainPeak";
				h = (float)(elev*SIZE+WATERLEVEL*3);
				b = (float) (0.5f/(moist + 1));
				g = (float) (0.5f/(moist + 1));
				r = (float) (0.5f/(moist + 1));
			}
		}
		if (type == "tundra") {
			if(trace<WATERLEVEL){
				type = "ice";
				h = (float)(elev*SIZE/32+31*WATERLEVEL/32);
				b = (float) (0.7f/(moist + 1));
				g = (float) (0.6f/(moist + 1));
				r = (float) (0.6f/(moist + 1));
			}else if(trace > WATERLEVEL&& trace<WATERLEVEL+BEACHSIZE){
				type = "tundra"; //more tunrtdra am I right
				h = (float)(elev*SIZE/16+WATERLEVEL);
				b = (float) (0.5f/(moist + 1));
				g = (float) (0.5f/(moist + 1));
				r = (float) (0.5f/(moist + 1));
			}else if (trace > WATERLEVEL + BEACHSIZE && trace < TREELINE) {
				type = "glacier";
				h = (float)(elev*SIZE/16+WATERLEVEL*1.5);
				b = (float) (0.7f/(moist + 1));
				g = (float) (0.6f/(moist + 1));
				r = (float) (0.6f/(moist + 1));
			}else if (trace > TREELINE) {
				type = "mountainPeak";
				h = (float)(elev*SIZE+WATERLEVEL*3);
				b = (float) (0.5f/(moist + 1));
				g = (float) (0.5f/(moist + 1));
				r = (float) (0.5f/(moist + 1));
			}
		}
		if(print){
			System.out.println(type);
		}
		float[] returns = { r, b, g, h };
		return returns;
	}
}
