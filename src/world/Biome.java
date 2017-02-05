package world;

import graphics.Shader;
import graphics.ShaderManager;
import maths.Vector3f;
import maths.Vector4f;
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

	public static final int JUNGLEWORLD = 0;//diff 1
	public static final int SWAMPWORLD = 1;//diff 1
	public static final int DESERTWORLD = 2;//diff 1
	public static final int TAIGAWORLD = 3; //diff 2 
	public static final int OCEANWORLD = 4; //diff 2
	public static final int MOUNTAINWORLD = 5;//diff 2
	public static final int HIGHLANDSWORLD = 6;//diff 2.5
	public static final int ICEWORLD = 7; //diff 3
	public static final int VULCANICWORLD = 8; //diff 3
	public static final int RADIOACTIVEWORLD = 9; //diff 3
	static String biome;
	static int type;

	public static float[] getPlanet(Vector3f centre, Vector3f point) {
		float r, b, g, h;
		r = b = g = h = 0;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
		double color = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double des = Math.abs(noisy.getValue(point.x / 6, point.y / 6, 0.1)) * 3;
		type = World.planetType;
		switch (type) {
		case JUNGLEWORLD:
			r = .2f;
			g = (float) (color / 25 + .3);
			b = (float) (1 / (elev + 7) + .1);
			h = (float) elev + 1;
			/*
			 * if(h>SIZE/4+1){ r*=1.1; b*=1.1; g*=1.1; } if(h>SIZE/3+1){ r*=1.1;
			 * b*=1.1; g*=1.1; }
			 */
			switch (((int) des) % 3) {
			case 0:
				biome = "denseJungle";
				r*=0.9;
				g*=0.9;
				b*=0.9;
				h += .5;
				break;
			case 1:
				biome = "pondJungle";
				b*=1.1;
				h *= 0.7;
				break;
			case 2:
				biome = "hillJungle";
				g*= 1.1;
				h *= 1.1;
				h += .3;
				break;
			}

			break;
		case SWAMPWORLD:
			r = (float) (color / 30 + .2);
			g = (float) (color / 25 + .3);
			b = (float) (1 / (elev + 7) + .1);
			h = (float) elev + 1;
			switch (((int) des) % 3) {
			case 0:
				biome = "lakeSwamp";
				h *= .4;
				b*=1.1;
				break;
			case 1:
				biome = "denseSwamp";
				h *= 0.4;
				h += 0.2;
				r*=0.9;
				g*=0.9;
				b*=0.9;
				break;
			case 2:
				biome = "marshSwamp";
				h *= 0.7;
				h += .3;
				g*= 1.1;
				break;
			}
			break;
		case DESERTWORLD:
		r = (float) (color / 25 + .23);
		g = (float) (color / 25 + .19);
		b = (float) (color / 25 + .17);
		h = (float) elev + 1;
		switch (((int) des) % 4) {
		case 0:
			biome = "duneDesert";
			h *= 1.5;
			h+=1;
			break;
		case 1:
			biome = "plainDesert";
			r*=1.1;
			h *= 0.5;
			h += 1;
			break;
		case 2:
			biome = "oasisDesert";
			b*=1.1;
			h *= 0.2;
			h+=.95;
			break;
		case 3: biome = "rockyDesert";
			r*=0.9;
			g*=0.9;
			b*=0.9;
			h *= 2;
			h+=1;
			break;
		}
		break;
	}
		float[] returns = { r, b, g, h, type };
		return returns;

	}

	public static void updateWater(int type) {
		/*
		 * Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.5f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 10);
		 * Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
		 * Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.01f);
		 */
		switch (type) {
		case JUNGLEWORLD:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 2);
			Shader.setUniform4f("waterColour", new Vector4f(0.2f, 0.3f, 0.4f, 1));
			Shader.setUniform1f("maxDistortion", 0.1f);
			break;
		case SWAMPWORLD:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 3);
			Shader.setUniform4f("waterColour", new Vector4f(0.24f, 0.28f, 0.26f, 1));
			Shader.setUniform1f("maxDistortion", 0.001f);
			break;
		/*
		 * case Biome.SEASONALFOREST: Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.5f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 20);
		 * Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
		 * //Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.001f); break; case
		 * Biome.FOREST: Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.5f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 10);
		 * Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
		 * //Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.01f); break; case Biome.OCEAN:
		 * Shader.start(ShaderManager.waterShader);
		 * Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.1f);
		 * Shader.setUniform1f("waveStrength", 0.02f);
		 * Shader.setUniform1f("waterClarity", 10);
		 * Shader.setUniform4f("waterColour", new Vector4f(0, 0.1f, 0.25f,1));
		 * //Shader.setUniform1f("waveSpeed", 0.00001f);
		 * Shader.setUniform1f("maxDistortion",1f); break; case Biome.DESERT:
		 * Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.5f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 10);
		 * Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
		 * //Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.01f); break; case
		 * Biome.MOUNTAIN: Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 1f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 1);
		 * Shader.setUniform4f("waterColour", new Vector4f(1, .1f, .1f,1));
		 * //Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.01f); break; case
		 * Biome.SAVANNA: Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0.1f);
		 * Shader.setUniform1f("waveStrength", 0.02f);
		 * Shader.setUniform1f("waterClarity", 10);
		 * Shader.setUniform4f("waterColour", new Vector4f(0.1f, 0.1f, 0.5f,1));
		 * // Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0.1f); break; case Biome.TAIGA:
		 * Shader.start(ShaderManager.waterShader);
		 * Shader.setUniform1f("fresnelPower", 0f);
		 * Shader.setUniform1f("waveStrength", 0.002f);
		 * Shader.setUniform1f("waterClarity", 3);
		 * Shader.setUniform4f("waterColour", new Vector4f(.9f, 0.9f, 0.9f,1));
		 * // Shader.setUniform1f("waveSpeed", 0.0001f);
		 * Shader.setUniform1f("maxDistortion",0f); break;
		 */
		}

	}
}
