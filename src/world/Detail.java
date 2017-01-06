package world;

import java.util.ArrayList;

import entity.Time;
import graphics.DetailManager;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;
import particles.Geyser;
import particles.Particle;
import particles.ParticleEmitter;

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
	private static Particle tallBush;
	private static Particle thornBush;
	public static Particle spout;

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
	public static DetailManager tallBushs;
	public static DetailManager thornBushs;
	
	public static ArrayList<DetailManager> man = new ArrayList<DetailManager>();
	public static ArrayList<ParticleEmitter> part = new ArrayList<ParticleEmitter>();
	public static ArrayList<Vector4f> waterSpouts = new ArrayList<Vector4f>();
	//spring/summer/fall/winters
	public static Vector4f[] season = {new Vector4f(.561f, .737f, .561f, 1),new Vector4f(.133f, .545f, .133f, 1),new Vector4f(.33f, .20f, .03f, 1),new Vector4f(.70f, .80f, .70f, 1)};
	public static void init() {
		bigTree = new Particle("resources/models/detail/bigTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		forestTree = new Particle("resources/models/detail/forestTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		jungleTree = new Particle("resources/models/detail/jungleTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		lillyPad = new Particle("resources/models/detail/lillyPad.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		pineTree = new Particle("resources/models/detail/pineTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		rock = new Particle("resources/models/detail/rock.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		savannaTree = new Particle("resources/models/detail/savannaTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		seasonalTree = new Particle("resources/models/detail/seasonalTree.obj", "none", new Vector3f(0, 0, 1f),
				100000l);

		yellowBush = new Particle("resources/models/detail/yellowBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		reed = new Particle("resources/models/detail/reed.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		deadTree = new Particle("resources/models/detail/deadTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		tallBush = new Particle("resources/models/detail/tallBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		thornBush = new Particle("resources/models/detail/thornBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);

		bigTrees = new DetailManager(bigTree, 1000, 10, new Vector4f(.561f, .737f, .561f, 1));
		forestTrees = new DetailManager(forestTree, 1000, 10, new Vector4f(.133f, .545f, .133f, 1));
		jungleTrees = new DetailManager(jungleTree, 1000, 10, new Vector4f(.261f, .671f, .629f, 0.5f));
		lillyPads = new DetailManager(lillyPad, 1000, 10, new Vector4f(.086f, .725f, .117f, 1));
		pineTrees = new DetailManager(pineTree, 1000, 10, new Vector4f(.078f, 0.2f, .023f, 1));
		rocks = new DetailManager(rock, 1000, 10, new Vector4f(.9f, .77f, .1f, 1));
		savannaTrees = new DetailManager(savannaTree, 1000, 10, new Vector4f(.33f, .20f, .03f, 1));
		seasonalTrees = new DetailManager(seasonalTree, 1000, 10, season[0]);

		yellowBushs = new DetailManager(yellowBush, 1000, 10, new Vector4f(0, 1, 1, 1));
		reeds = new DetailManager(reed, 1000, 10, new Vector4f(.49f, .46f, .352f, 1));
		deadTrees = new DetailManager(deadTree, 1000, 10, new Vector4f(.33f, .20f, .03f, 1));
		tallBushs = new DetailManager(tallBush, 1000, 10, new Vector4f(.181f, .672f, .539f, 1));
		thornBushs = new DetailManager(thornBush, 1000, 10, new Vector4f(.4f, .6f, .2f, 1));
		
		//spout = new Particle("resources/models/box.obj", "none", new Vector3f(0.1f, 0.2f, 0.5f), 1000l);
		//spout.scale(.01f, .01f, .01f);
		
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
		man.add(tallBushs);
		man.add(thornBushs);
		man.stream().forEach(m -> m.activate());
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
			if (check(position.z, 50)) {
			Particle newDetail = new Particle(pineTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			pineTrees.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			if (check(position.z, 10)) {
				Particle newDetail = new Particle(thornBush);
				newDetail.translate(position);
				newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
				thornBushs.detailsToAdd.add(newDetail);
			}
		}

	}

	private static void savanna(Vector3f position) {
		double detail = Math.abs(det.getValue(Math.pow(position.x / DETAILSCALER * 2, 2),
				Math.pow(position.y / DETAILSCALER * 2, 2), position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			if (check(position.z, 50)) {
			Particle newDetail = new Particle(savannaTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			savannaTrees.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			if (check(position.z, 30)) {
			Particle newDetail = new Particle(rock);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			rocks.detailsToAdd.add(newDetail);
			}
		}

	}

	private static void mountain(Vector3f position) {
		double detail = Math
				.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			if (check(position.z, 10)) {
				//lavaSpout.detailsToAdd.add(newDetail);
				}
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
			if (check(position.z, 10)) {
			Particle newDetail = new Particle(yellowBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			yellowBushs.detailsToAdd.add(newDetail);
			}
		}

	}

	private static void ocean(Vector3f position) {
	}

	private static void swamp(Vector3f position) {
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER/2,
				position.y / DETAILSCALER/2, position.z));
		if (detail > 0.4 && position.x < Chunk.WATERLEVEL) {
			if (check(position.z, 0.1f)) {
			Particle newDetail = new Particle(lillyPad);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, Chunk.WATERLEVEL);
			lillyPads.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z < Chunk.WATERLEVEL) {
			waterSpouts.add(new Vector4f(position.x, position.y, position.z, 0));
			/*
			
			*/
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			if (check(position.z, 1)) {
			Particle newDetail = new Particle(reed);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			reeds.detailsToAdd.add(newDetail);
			}
		}

	}

	private static void forest(Vector3f position) {
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			if (check(position.z, 20)) {
			Particle newDetail = new Particle(forestTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			forestTrees.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(World.noise.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1.4) {
			if (check(position.z, 1)) {
			Particle newDetail = new Particle(bigTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			bigTrees.detailsToAdd.add(newDetail);
			}
		}

	}

	private static void seasonalForest(Vector3f position) {
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER,
				position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			if (check(position.z, 70)) {
			Particle newDetail = new Particle(seasonalTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			seasonalTrees.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			if (check(position.z, 1)) {
			Particle newDetail = new Particle(deadTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			deadTrees.detailsToAdd.add(newDetail);
			}
		}
	}

	private static void rainForest(Vector3f position) {
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER/2,
				position.y / DETAILSCALER/2, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL) {
			if (check(position.z, 90)) {
			Particle newDetail = new Particle(jungleTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			jungleTrees.detailsToAdd.add(newDetail);
			}
		}
		detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1) {
			if (check(position.z, 60)) {
			Particle newDetail = new Particle(tallBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			tallBushs.detailsToAdd.add(newDetail);
			}
		}
	}

	public static void update(long l) {
		man.stream().forEach(m -> m.update(l));
		part.stream().forEach(m -> m.update(l));
		int m = Time.getDay();
		seasonalTrees.colour = season[m%4];
		for(int i = 0; i<waterSpouts.size();i++){
			if(waterSpouts.get(i).w==0){//not GL. 
				//water(new Vector3f(waterSpouts.get(i).x,waterSpouts.get(i).y,waterSpouts.get(i).z));
				waterSpouts.get(i).w = 1;
			}
		}

	}

	public static void render(Vector4f renderClipPlane) {
		man.stream().forEach(m -> m.render(renderClipPlane));
		part.stream().forEach(p -> p.render(renderClipPlane));
	}

	/**
	 * 
	 * @param height
	 * @param chance
	 *            0-100 plz
	 * @return
	 */
	public static boolean check(float height, float chance) {
		float h = Math.abs(Math.round(height) - height) * 200;
		if (h > chance) {
			return false;
		} else {
			return true;
		}

	}
	public static void water(Vector3f position){
		Particle s = spout;
		s.translate(position);
		Geyser temp = new Geyser(s, 1000, 10,new Vector4f(0.1f, 0.2f, 0.5f,1));
		temp.activate();
		part.add(temp);
	}
}
