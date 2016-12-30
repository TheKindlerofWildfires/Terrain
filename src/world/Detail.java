package world;

import java.util.ArrayList;

import graphics.DetailManager;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;
import particles.Particle;

public abstract class Detail {
	static float DETAILSCALER = 1;
	static Perlin det = World.detail;
	private static Particle bigTree;
	private static Particle forestTree;
	private static Particle jungleTree;
	private static Particle lillyPad;
	private static Particle pineTree;
	private static Particle rock;
	private static Particle savannaTree;
	private static Particle seasonalTree;
	
	private static Particle yellowBush;
	private static Particle reed;
	private static Particle deadTree;
	private static Particle normalBush;
	private static Particle thornBush;
	
	public static DetailManager bigTrees;
	public static DetailManager forestTrees;
	public static DetailManager jungleTrees;
	public static DetailManager lillyPads;
	public static DetailManager pineTrees;
	public static DetailManager rocks;
	public static DetailManager savannaTrees;
	public static DetailManager seasonalTrees;
	
	public static DetailManager yellowBushs;
	public static DetailManager reeds;
	public static DetailManager deadTrees;
	public static DetailManager normalBushs;
	public static DetailManager thornBushs;
	public static ArrayList<DetailManager> man = new ArrayList<DetailManager>();
	
	public static void init(){
		bigTree = new Particle("resources/models/detail/bigTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		forestTree = new Particle("resources/models/detail/forestTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		jungleTree = new Particle("resources/models/detail/jungleTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		lillyPad = new Particle("resources/models/detail/lillyPad.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		pineTree = new Particle("resources/models/detail/pineTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		rock = new Particle("resources/models/detail/rock.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		savannaTree = new Particle("resources/models/detail/savannaTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		
		yellowBush = new Particle("resources/models/detail/yellowBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		reed = new Particle("resources/models/detail/reed.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		deadTree = new Particle("resources/models/detail/deadTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		normalBush = new Particle("resources/models/detail/normalBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		thornBush = new Particle("resources/models/detail/thornBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		
		bigTrees = new DetailManager(bigTree, 1000, 10, new Vector4f(1,0,0,1));
		forestTrees = new DetailManager(forestTree, 1000, 10, new Vector4f(0,0,1,1));
		jungleTrees = new DetailManager(jungleTree, 1000, 10, new Vector4f(0,1,0,1));
		lillyPads = new DetailManager(lillyPad, 1000, 10, new Vector4f(0,1,1,1));
		pineTrees = new DetailManager(pineTree, 1000, 10, new Vector4f(1,0,1,1));
		rocks = new DetailManager(rock, 1000, 10, new Vector4f(1,1,0,1));
		savannaTrees = new DetailManager(savannaTree, 1000, 10, new Vector4f(1,1,1,1));
		seasonalTrees = new DetailManager(seasonalTree, 1000, 10, new Vector4f(0,0,0,1));
		
		yellowBushs = new DetailManager(yellowBush, 1000, 10, new Vector4f(0,1,1,1));
		reeds = new DetailManager(reed, 1000, 10, new Vector4f(1,0,1,1));
		deadTrees = new DetailManager(deadTree, 1000, 10, new Vector4f(1,1,0,1));
		normalBushs = new DetailManager(normalBush, 1000, 10, new Vector4f(1,1,1,1));
		thornBushs = new DetailManager(thornBush, 1000, 10, new Vector4f(0,0,0,1));
		
		man.add(bigTrees);
		man.add(forestTrees);
		man.add(jungleTrees);
		man.add(lillyPads);
		man.add(pineTrees);
		man.add(rocks);
		man.add(savannaTrees);
		man.add(seasonalTrees);
		man.add(yellowBushs);
		man.add(reeds);
		man.add(deadTrees);
		man.add(normalBushs);
		man.add(thornBushs);
		man.stream().forEach(m->m.activate());
	}
	/**
	 * 
	 * @param position
	 * @param biome
	 */
	public static void detail(Vector3f position, float biome) {
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
	}

	private static void taiga(Vector3f position) {
		// iceWater
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(pineTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			pineTrees.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z>Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(thornBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			thornBushs.detailsToAdd.add(newDetail);
		}

	}

	private static void savanna(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER * 2, 2),
				Math.pow(position.y / DETAILSCALER * 2, 2), position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(savannaTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			savannaTrees.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			Particle newDetail = new Particle(rock);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			rocks.detailsToAdd.add(newDetail);
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
		if (detail > 1) {
			Particle newDetail = new Particle(yellowBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			yellowBushs.detailsToAdd.add(newDetail);
		}

	}

	private static void ocean(Vector3f position) {
		// strongWater
	}

	private static void swamp(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 0.4 && position.x<Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(lillyPad);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, Chunk.WATERLEVEL);
			lillyPads.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z < Chunk.WATERLEVEL) {
			//// placeAnimation(spout,waterSpout, position);
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			Particle newDetail = new Particle(reed);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			reeds.detailsToAdd.add(newDetail);
		}

	}

	private static void forest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1&& position.z>Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(forestTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			forestTrees.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1.2) {
			Particle newDetail = new Particle(bigTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			bigTrees.detailsToAdd.add(newDetail);
		}

	}

	private static void seasonalForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1&& position.z>Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(seasonalTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			seasonalTrees.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			Particle newDetail = new Particle(deadTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			deadTrees.detailsToAdd.add(newDetail);
		}
	}

	private static void rainForest(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER, 2),
				Math.pow(position.y / DETAILSCALER, 2), position.z));
		if (detail > 1&& position.z>Chunk.WATERLEVEL) {
			Particle newDetail = new Particle(jungleTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			jungleTrees.detailsToAdd.add(newDetail);
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			Particle newDetail = new Particle(normalBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z/Chunk.SIZE);
			normalBushs.detailsToAdd.add(newDetail);
		}
	}


	public static void update(long l) {
		man.stream().forEach(m->m.update(l));
		
	}
	public static void render(Vector4f renderClipPlane) {
		man.stream().forEach(m->m.render(renderClipPlane));
	}
}
