package world;

import java.util.ArrayList;

import graphics.DetailManager;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;
import object.GameObject;
import particles.Particle;

public abstract class Detail {
	static float DETAILSCALER = 1;
	static Perlin det = World.detail;
	private static GameObject object;
	private static ArrayList<GameObject> details = new ArrayList<GameObject>();
	private static Particle bigTree;
	private static Particle forestTree;
	private static Particle jungleTree;
	private static Particle lillyPad;
	private static Particle pineTree;
	private static Particle rock;
	private static Particle savannaTree;
	private static Particle seasonalTree;
	public static DetailManager bigTrees;
	public static DetailManager forestTrees;
	public static DetailManager jungleTrees;
	public static DetailManager lillyPads;
	public static DetailManager pineTrees;
	public static DetailManager rocks;
	public static DetailManager savannaTrees;
	public static DetailManager seasonalTrees;
	public static ArrayList<DetailManager> man = new ArrayList<DetailManager>();
	
	public static void init(){
		bigTree = new Particle("resources/models/detail/bigTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		forestTree = new Particle("resources/models/detail/forestTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		jungleTree = new Particle("resources/models/detail/jungleTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		lillyPad = new Particle("resources/models/detail/lillyPad.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		pineTree = new Particle("resources/models/detail/pineTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		rock = new Particle("resources/models/detail/rock.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		savannaTree = new Particle("resources/models/detail/savannaTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		seasonalTree = new Particle("resources/models/detail/seasonalTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		
		bigTrees = new DetailManager(bigTree, 1000, 10, new Vector4f(1,1,1,1));
		forestTrees = new DetailManager(forestTree, 1000, 10, new Vector4f(1,1,1,1));
		jungleTrees = new DetailManager(jungleTree, 1000, 10, new Vector4f(1,1,1,1));
		lillyPads = new DetailManager(lillyPad, 1000, 10, new Vector4f(1,1,1,1));
		pineTrees = new DetailManager(pineTree, 1000, 10, new Vector4f(1,1,1,1));
		rocks = new DetailManager(rock, 1000, 10, new Vector4f(1,1,1,1));
		savannaTrees = new DetailManager(savannaTree, 1000, 10, new Vector4f(1,1,1,1));
		seasonalTrees = new DetailManager(seasonalTree, 1000, 10, new Vector4f(1,1,1,1));
		
		man.add(bigTrees);
		man.add(forestTrees);
		man.add(jungleTrees);
		man.add(lillyPads);
		man.add(pineTrees);
		man.add(rocks);
		man.add(savannaTrees);
		man.add(seasonalTrees);
		
		man.stream().forEach(m->m.activate());
		
		ArrayList<Particle> treeees = new ArrayList<Particle>();

		for (int i = 0; i < 10; i++) {
			Particle newTree = new Particle(pineTree);
			Vector3f displacement = new Vector3f((float) Math.random() * 100, (float) Math.random() * 100, 4);
			newTree.translate(displacement);
			treeees.add(newTree);
		}

		pineTrees.detailsToAdd.addAll(treeees);
		
	}
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
	public static void update(long l) {
		//pineTrees.update(l);
		man.stream().forEach(m->m.update(l));
		
	}
	public static void render(Vector4f renderClipPlane) {
		//pineTrees.render(renderClipPlane)
		man.stream().forEach(m->m.render(renderClipPlane));
	}
}
