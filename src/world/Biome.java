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

	public static final int JUNGLEWORLD = 0;
	public static final int REDWOODWORLD = 1;
	public static final int DESERTWORLD = 2;
	public static final int TAIGAWORLD = 3;
	public static final int ABORIALWORLD = 5;
	public static final int OCEANWORLD = 6;
	public static final int MOUNTAINWORLD = 7;
	public static final int PLAINSWORLD = 8;
	public static final int ICEWORLD = 9;
	public static final int VULCANICWORLD = 10;
	public static final int RADIOACTIVEWORLD = 11;
	static String biome;
	static int type;

	
	public static float[] getPlanet(Vector3f centre, Vector3f point){
		float r, b, g, h;
		r = b = g = h = 0;
		double elev = Math.abs(noise.getValue(point.x/2, point.y/2, 0.1)) * SIZE / 2;
		double color = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double des = Math.abs(noisy.getValue(point.x/6, point.y/6, 0.1)) * SIZE / 2;
		type = World.planetType;
		switch(type){
			case JUNGLEWORLD:
				r = .2f;
				b = (float) (1/(elev+7)+.1);
				g = (float) (color/25+.3);
				h = (float) elev+1;
				if(h>SIZE/4+1){
					r*=1.1;
					b*=1.1;
					g*=1.1;
				}
				if(h>SIZE/3+1){
					r*=1.1;
					b*=1.1;
					g*=1.1;
				}
				switch(((int)des)%3){
					case 0:
						biome = "denseJungle";
						h*=1.1;
						h+=0.5;	
						b*=.8;
						r*=.8;
						g*=.8;
						break;
					case 1:
						biome = "pondJungle";
						h*=0.5;
						r*=1.1;
						break;
					case 2:
						biome = "hillJungle";
						h*=1.3;
						h+=.5;
						g*=1.1;
				}
				break;
			case REDWOODWORLD:
				r = 1;
				b = 0;
				g = (float) (1/(elev+1));
				h = (float) elev;
				break;
		}
		float[] returns = { r, b, g, h, type };
		return returns;
		
	}
	/*
	public static void effect() {
		/*
		 	Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
		switch (type) {
		case Biome.RAINFOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 2);
			Shader.setUniform4f("waterColour", new Vector4f(0.2f, 0.3f, 0.4f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",0.1f);
			break;
		case Biome.SEASONALFOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 20);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.001f);
			break;
		case Biome.FOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.SWAMP:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 3);
			Shader.setUniform4f("waterColour", new Vector4f(0.1f, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",0.001f);
			break;
		case Biome.OCEAN:
			Shader.start(ShaderManager.waterShader);
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.02f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.1f, 0.25f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",1f);
			break;
		case Biome.DESERT:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.MOUNTAIN:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 1f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 1);
			Shader.setUniform4f("waterColour", new Vector4f(1, .1f, .1f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.SAVANNA:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.02f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0.1f, 0.1f, 0.5f,1));
		//	Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.1f);
			break;
		case Biome.TAIGA:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 3);
			Shader.setUniform4f("waterColour", new Vector4f(.9f, 0.9f, 0.9f,1));
		//	Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0f);
			break;
		}
		*/


	
	}
