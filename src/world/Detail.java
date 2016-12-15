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
	public static float[] detail(float frequency,float lacun,float pert, int size, String type, Vector3f position) {
		det = new Perlin();
		det.setFrequency(frequency);
		det.setLacunarity(lacun);
		det.setPersistence(pert);
		DETAILSCALER = size;
		double detail;
		if(type == "exp"){
			detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		}else if(type == "norm"){
			detail = Math.abs(det.getValue(position.x / DETAILSCALER,
					position.y / DETAILSCALER, position.z));
		}else{
			detail = 0;
		}
		//detail = detail*frequency;
		int r, g, b;
		r = g = b = 0;
		if (detail > 1) {
			r = 1;
			b = 1;
			g = 1;
			return new float[] { r, g, b };
		}else{
			return new float[]{-1,-1,-1};
		}
		
	}
}
