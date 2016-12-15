package world;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public abstract class Detail {
	static float DETAILSCALER = 1;
	static Perlin det = World.detail;
	/**
	 * Should be a void that places stuff but I'm lazy
	 * 
	 * @param frequency >0.5
	 * @param size <TEMPSCALER, RAINSCALER
	 * @param type "exp, norm"
	 * @param position
	 */
	public static void detail(Vector3f position, float biome) {
		int tech = (int)biome;
		switch(tech){
		case Biome.RAINFOREST:
			rainForest(position);
			break;
		case Biome.SEASONALFOREST:
			seasonalForest(position);
			break;
		case Biome.FOREST:
			break;
		case Biome.SWAMP:
			break;
		case Biome.OCEAN:
			break;
		case Biome.DESERT:
			break;
		case Biome.MOUNTAIN:
			break;
		case Biome.SAVANNA:
			break;
		case Biome.TAIGA:
			break;
		}
	}

	private static void seasonalForest(Vector3f position) {
		// TODO Auto-generated method stub
		
	}

	private static void rainForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place tall tree at location
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place lake at location
		}
	}
}
