package world;


import java.util.ArrayList;

import maths.Vector3f;
import noiseLibrary.module.source.Perlin;
import object.GameObject;

public abstract class Detail {
	static float DETAILSCALER = 1;
	static Perlin det = World.detail;
	private static GameObject object;
	private static ArrayList<GameObject> details = new ArrayList<GameObject>();
	/**
	 * 
	 * @param position
	 * @param biome
	 */
	public static ArrayList<GameObject> detail(Vector3f position, float biome) {
		int tech = (int)biome;
		switch(tech){
		case Biome.RAINFOREST:
			rainForest(position);
			break;
		case Biome.SEASONALFOREST:
			seasonalForest(position);
			break;
		case Biome.FOREST:
			forest(position);
			break;
		case Biome.SWAMP:
			swamp(position);
			break;
		case Biome.OCEAN:
			ocean(position);
			break;
		case Biome.DESERT:
			desert(position);
			break;
		case Biome.MOUNTAIN:
			mountain(position);
			break;
		case Biome.SAVANNA:
			savanna(position);
			break;
		case Biome.TAIGA:
			taiga(position);
			break;
		}

		return details;
	}

	private static void taiga(Vector3f position) {
		//cover water in ice
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place scattering of conif trees at location
			place("resources/models/tree.obj","resources/textures/wood.png", position);
		}
		
	}

	private static void savanna(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER*2, 2),
				Math.pow(position.y / DETAILSCALER*2, 2), position.z));
		if(detail>1){
			//place lonely tree at location
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place rock formation at location
		}
		
	}

	private static void mountain(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place a bolder 
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place lava spout at location
		}
		
	}

	private static void desert(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place sandstorm particles
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place oasis at location
		}
		
	}

	private static void ocean(Vector3f position) {
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place reef at location
		}
		
	}

	private static void swamp(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place lillypad in water only
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place geiser at location
		}
		//place reeds around edges
		
	}

	private static void forest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place tree at location
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place single tree/hill at location, clear nearby
		}
		
	}

	private static void seasonalForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place seasonal tree at location, changes color each daynight cycle
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place lake at location
		}
		
	}

	private static void rainForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if(detail>1){
			//place tall tree at location, they should have vines
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if(detail>1){
			//place lake at location
		}
	}
	private static void place(String model,String texture, Vector3f position){
		object = new GameObject(model, texture, false);
		object.placeAt(position.x, position.x, position.z);
		details.add(object);
		//make sure! that the object is removed when the chunk is!
		//have a render models function
	}
}
