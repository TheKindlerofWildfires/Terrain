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
		int tech = (int) biome;
		switch (tech) {
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
		// iceWater
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			// place("resources/models/pineTree.obj",pineTree, position);
		}

	}

	private static void savanna(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER * 2, 2),
				Math.pow(position.y / DETAILSCALER * 2, 2), position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			// place(savannaTree,savannaTree, position);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			// place(rock,rock, position);
		}

	}

	private static void mountain(Vector3f position) {
		// lavaWater
		double detail = Math
				.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			// placeAnimation(spout,lavaSpout, position);
		}
	}

	private static void desert(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1.1) {
			// placeAnimation(sandStorm,sandStorm, position);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));

	}

	private static void ocean(Vector3f position) {
		// strongWater
	}

	private static void swamp(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1) {
			// place("resources/models/lillyPad.obj",lillyPad, position);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z < Chunk.WATERLEVEL) {
			//// placeAnimation(spout,waterSpout, position);
		}

	}

	private static void forest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1) {
			// place(tree.obj,tree, position);
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			// place(bigTree,bigTree, position);
			/*
			 * breaker = True while(breaker){ for(int i = 0;
			 * i<details.size();i++){
			 * if(details.get(i).position.subtract(position).length < 2 ||
			 * details.get(i).position.add(position).length < 2){
			 * details.remove(i) }else{ breaker = false } } }
			 */
		}

	}

	private static void seasonalForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1) {
			// placeAnimation(seasonalTree,seasonalTree, position);
		}
	}

	private static void rainForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1) {
			// place(vineTree,vineTree, position);
		}
	}

	private static void place(String model, String texture, Vector3f position) {
		object = new GameObject(model, texture, false);
		object.placeAt(position.x, position.x, position.z);
		details.add(object);
		// make sure! that the object is removed when the chunk is!
		// have a render models function
	}

	private static void placeAnimation(String type, Vector3f color, Vector3f position) {
		// details.add(animation);
		// make sure! that the object is removed when the chunk is!
		// have a render models function
	}
}
