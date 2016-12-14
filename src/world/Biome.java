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
		double moist = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double temp = Math.abs(noise.getValue(point.x/16, point.y/16, 0.2));	
		double trace = Math.abs(noise.getValue(point.x/64, point.y/64, 0.1)) * SIZE;
		float bs = (int) (temp*5.1);
		if (bs == 0) {
			type = "ocean";//island, shallow, deep
		}
		if (bs == 1) {
			type = "wetland"; //swamp --> plains-->hill
		}
		if (bs == 2) {
			type = "desert"; //hill--> mountain --> lake
			
		}
		if (bs == 3) {
			type = "mountainPeak"; //hill--> mountain --> peak
		}
		if(bs == 4){
			type = "mountainLake";
		}
		if (bs == 5 || bs == 6) {
			type = "tundra"; //ice--> tundra --> mountain
		}
		if(bs>6){
			r = 1;
			g = 1;
			b = 1;
		}
		/*water =2
		 *beach = 1
		 *treeline = 6.6
		 */
		if (type == "ocean") {
			h = (float) (elev*SIZE/16);
			b = (float) (0.3/(moist+1));
			g = (float) (0.2/(moist+1));
			r = (float) (0.2/(moist+1));
			if(trace<WATERLEVEL-BEACHSIZE){	
				type = "ocean:depth";
				h -= WATERLEVEL/2;
				b *= (float) 0.4;
				r *= (float) 0.3;
				g *= (float) 0.2;
			}else if(trace > WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
				type = "ocean:reef";
				h -= WATERLEVEL/4;
				b *= (float) 0.4;
				r *= (float) 0.3;
				g *= (float) 0.2;
			}else if (trace > WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
				type = "ocean:shallows";
				h += WATERLEVEL/4;
				b *= (float) 0.3;
				r *= (float) 0.2;
				g *= (float) 0.5;
			}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
				type = "ocean:island";
				h += WATERLEVEL/2;
				b *= (float) 0.5;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}
			
		}
		if (type == "wetland") {
			h = (float) (elev*SIZE/32);
			b = (float) (0.2/(moist+1));
			g = (float) (0.3/(moist+1));
			r = (float) (0.2/(moist+1));
			if(trace<WATERLEVEL-BEACHSIZE){
				type = "wetland:swamp";
				h += 31*WATERLEVEL/32;
				b *= (float) 0.4;
				r *= (float) 0.3;
				g *= (float) 0.4;
			}else if(trace > WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
				type = "wetland:plains";
				h*= 2;
				h += 15*WATERLEVEL/16;
				b *= (float) 0.3;
				r *= (float) 0.3;
				g *= (float) 0.4;
			}else if (trace >WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
				type = "wetland:forest";//run forrest run!
				h*= 3;
				h += 7*WATERLEVEL/8;
				b *= (float) 0.2;
				r *= (float) 0.2;
				g *= (float) 0.4;
			}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
				type = "wetland:hill";
				h*= 4;
				h += 7*WATERLEVEL/8;
				b *= (float) 0.2;
				r *= (float) 0.2;
				g *= (float) 0.3;
			}
		}
		if (type == "mountainLake") {
			h = (float) (elev*SIZE/4);
			b = (float) (0.2/(moist+1));
			g = (float) (0.3/(moist+1));
			r = (float) (0.3/(moist+1));
			if(trace<WATERLEVEL-BEACHSIZE){
				type = "mountainLake:hill";
				h += 7*WATERLEVEL/8;
				b *= (float) 0.2;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if(trace >  WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
				type = "mountainLake:mountainEdge";
				h *= (float) 2;
				h += 3*WATERLEVEL/4;
				b *= (float) 0.3;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if (trace > WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
				type = "mountainLake:mountain";
				h *= (float) 4;
				h += 1*WATERLEVEL/2;
				b *= (float) 0.3;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
				type = "mountainLake:lake";
				b *= (float) 0.3;
				r *= (float) 0.1;
				g *= (float) 0.1;
			}
		}
		if (type == "mountainPeak") {
			h = (float) (elev*SIZE/4);
			b = (float) (0.2/(moist+1));
			g = (float) (0.3/(moist+1));
			r = (float) (0.3/(moist+1));
			if(trace<WATERLEVEL-BEACHSIZE){
				type = "mountainPeak:hill";
				h += 7*WATERLEVEL/8;
				b *= (float) 0.2;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if(trace >  WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
				type = "mountainPeak:mountainEdge";
				h *= (float) 1.5;
				h += 7*WATERLEVEL/8;
				b *= (float) 0.3;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if (trace > WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
				type = "mountainPeak:mountain";
				h *= (float) 3;
				h += 7*WATERLEVEL/8;
				b *= (float) 0.3;
				r *= (float) 0.2;
				g *= (float) 0.2;
			}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
				type = "mountainPeak:mountainPeak";
				h *= (float) 6;
				h += 7*WATERLEVEL/8;
				b *= (float) 0.7;
				r *= (float) 0.6;
				g *= (float) 0.6;
			}
		}
		if (type == "tundra") {
			h = (float) (elev*SIZE/8);
			b = (float) (0.5/(moist+1));
			g = (float) (0.45/(moist+1));
			r = (float) (0.45/(moist+1));
				if(trace<WATERLEVEL-BEACHSIZE){
					type = "tundra:ice";
					h += 16*WATERLEVEL/16;
					b *= (float) 0.2;
					r *= (float) 0.2;
					g *= (float) 0.2;
				}else if(trace >  WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
					type = "tundra:tundra";
					h *= (float) 1.5;
					h += 17*WATERLEVEL/16;
					b *= (float) 0.3;
					r *= (float) 0.3;
					g *= (float) 0.3;
				}else if (trace > WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
					type = "tundra:glacier";
					h *= (float) 3;
					h += 18*WATERLEVEL/16;
					b *= (float) 0.4;
					r *= (float) 0.4;
					g *= (float) 0.4;
				}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
					type = "tundra:mountainPeak";
					h *= (float) 6;
					h += 19*WATERLEVEL/16;
					b *= (float) 0.5;
					r *= (float) 0.5;
					g *= (float) 0.5;
				}
		}
		if (type == "desert") {
			h = (float) (elev*SIZE/8);
			b = (float) (0.45/(moist+1));
			g = (float) (0.70/(moist+1));
			r = (float) (0.85/(moist+1));
				if(trace<WATERLEVEL-BEACHSIZE){
					type = "desert:edge";
					h += WATERLEVEL;
					b *= (float) 0.2;
					r *= (float) 0.2;
					g *= (float) 0.2;
				}else if(trace >  WATERLEVEL-BEACHSIZE&& trace<WATERLEVEL){
					type = "desert:centre";
					h *= (float) 2;
					h += WATERLEVEL;
					b *= (float) 0.3;
					r *= (float) 0.3;
					g *= (float) 0.3;
				}else if (trace > WATERLEVEL && trace < WATERLEVEL+0.5*BEACHSIZE) {
					type = "desert:hill";
					h *= (float) 4;
					h += WATERLEVEL;
					b *= (float) 0.4;
					r *= (float) 0.4;
					g *= (float) 0.4;
				}else if (trace > WATERLEVEL+0.5*BEACHSIZE) {
					type = "desert:oasis";
					h *= (float) 0.5;
					h += WATERLEVEL;
					b *= (float) 0.6;
					r *= (float) 0.4;
					g *= (float) 0.4;
				}
		}
		if(print){
			System.out.println(type);
		}
		float[] returns = { r, b, g, h };
		return returns;
	}
}
