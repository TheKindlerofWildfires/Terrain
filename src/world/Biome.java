package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class Biome {
	public static float SIZE = Chunk.SIZE;
	public static float WATERLEVEL = Chunk.WATERLEVEL;
	public static float BEACHSIZE = Chunk.BEACHSIZE;
	public static float TREELINE = Chunk.TREELINE;
	static Perlin noise = Chunk.noise;
	public static float[] getValue(Vector3f centre, Vector3f point) {
		float r, b, g, h;
		r=b=g=h=1;
		String type = null;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1));
		double moist = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double temp = Math.abs(noise.getValue(point.x/4, point.y/4, 0.2));
		float bs = (int)(temp*4.9);
		if (bs == 0) {
			type = "ocean";
		}
		if (bs == 1) {
			type = "beach";
		}
		if (bs == 2) {
			type = "swamp";
		}
		if (bs == 3) {
			type = "desert";
		}
		if (bs == 4) {
			type = "hill";
		}
		if (bs == 5) {
			type = "mountainEdge";
		}
		if (bs == 6) {
			type = "mountain";
		}
		if (bs == 7) {
			type = "mountainTop";
		}
		if (type == "ocean") {
			h = (float) (elev * SIZE / 16+1*WATERLEVEL/2);
			r = (float) (0.4f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.6f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.2f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.1f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		
		if (type == "beach") {
			h = (float) (elev * SIZE /12+3*WATERLEVEL/4);
			r = (float) (0.2f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.4f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "swamp") {
			h = (float) (elev * SIZE /8+7*WATERLEVEL/8);
			r = (float) (0.2f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.4f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
			
		if (type == "desert") {
			h = (float) (elev * SIZE / 6+15*WATERLEVEL/16);
			r = (float) (0.3f / (moist + 1));
			b = (float) (0.2f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.3f;
				r *= 0.3f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "hill") {
			h = (float) (elev * SIZE / 2+31/64*WATERLEVEL);
			r = (float) (0.3f / (moist + 1));
			b = (float) (0.3f / (moist + 1));
			g = (float) (0.62f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.4f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "mountainEdge") {
			h = (float) (elev * SIZE*1+31*WATERLEVEL/32);
			r = (float) (0.3f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.2f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 1.0f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 1.0f;
				r *= 1.1f;
				g *= 1.1f;
			}
			if (h > TREELINE) {
				b *= 1.2f;
				r *= 1.3f;
				g *= 1.1f;
			}
		}
		if (type == "mountain") {
			h = (float) (elev * SIZE*1.4+WATERLEVEL);
			r = (float) (0.4f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.2f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.0f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.0f;
				g *= 1.0f;
			}
		}
		
		if (type == "mountainTop") {
			h = (float) (elev * SIZE *2+WATERLEVEL);
			r = (float) (0.4f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.6f;
				g *= 0.6f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 1.1f;
				r *= 1.1f;
				g *= 1.1f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 1.6f;
				r *= 1.6f;
				g *= 1.6f;
			}
			if (h > TREELINE) {
				b *= 2.1f;
				r *= 2.1f;
				g *= 2.1f;
			}
		}
		float[] returns = { r, b, g, h };
		return returns;
	}
}
