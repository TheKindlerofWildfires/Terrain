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
	private static Particle tallBush;
	private static Particle thornBush;
	private static Particle yellowBush;
	private static Particle lillyPad;
	private static Particle savannaTree;
	private static Particle rock;
	private static Particle spire;
	private static Particle gate;

	public static DetailManager bigTrees;
	public static DetailManager forestTrees;
	public static DetailManager tallBushs;
	public static DetailManager thornBushs;
	public static DetailManager yellowBushs;
	public static DetailManager lillyPads;
	public static DetailManager savannaTrees;
	public static DetailManager rocks;
	public static DetailManager spires;
	public static DetailManager gates;
	
	public static ArrayList<DetailManager> man = new ArrayList<DetailManager>();

	//spring/summer/fall/winters
	public static Vector4f[] season = {new Vector4f(.561f, .737f, .561f, 1),new Vector4f(.133f, .545f, .133f, 1),new Vector4f(.33f, .20f, .03f, 1),new Vector4f(.70f, .80f, .70f, 1)};
	public static void init() {
		bigTree = new Particle("resources/models/detail/bigTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		forestTree = new Particle("resources/models/detail/forestTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		tallBush = new Particle("resources/models/detail/tallBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		thornBush = new Particle("resources/models/detail/thornBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		yellowBush = new Particle("resources/models/detail/yellowBush.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		lillyPad = new Particle("resources/models/detail/lillyPad.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		savannaTree = new Particle("resources/models/detail/savannaTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		rock = new Particle("resources/models/detail/rock.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		spire = new Particle("resources/models/detail/spire.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		gate = new Particle("resources/models/detail/gate.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		
		bigTrees = new DetailManager(bigTree, 1000, 10, new Vector4f(.561f, .700f, .561f, 1));
		forestTrees = new DetailManager(forestTree, 1000, 10, new Vector4f(.5f, .637f, .5f, 1));
		tallBushs = new DetailManager(tallBush, 1000, 10, new Vector4f(.4f, .537f, .4f, 1));
		thornBushs = new DetailManager(thornBush, 1000, 10, new Vector4f(.661f, .700f, .561f, 1));
		yellowBushs = new DetailManager(yellowBush, 1000, 10, new Vector4f(.6f, .637f, .5f, 1));
		lillyPads = new DetailManager(lillyPad, 1000, 10, new Vector4f(.3f, .537f, .3f, 1));
		savannaTrees = new DetailManager(savannaTree, 1000, 10, new Vector4f(.6f, .637f, .5f, 1));
		rocks = new DetailManager(rock, 1000, 10, new Vector4f(.8f, .7f, .5f, 1));
		spires = new DetailManager(spire, 1000, 10, new Vector4f(.65f, .6f, .43f, 1));
		gates = new DetailManager(gate, 1000, 10, new Vector4f(.75f, .7f, .53f, 1));
		
		man.add(bigTrees);
		man.add(forestTrees);
		man.add(tallBushs);
		man.add(thornBushs);
		man.add(yellowBushs);
		man.add(lillyPads);
		man.add(savannaTrees);
		man.add(rocks);
		man.add(spires);
		man.add(gates);
		man.stream().forEach(m -> m.activate());
	}

	/**
	 * 
	 * @param position
	 * @param biome
	 */
	public static void detail(Vector3f position, float biome) {
		int b = (int) biome;
		switch(World.planetType){
		case Biome.JUNGLEWORLD:
			switch(b){
			case Biome.denseJungle:
				denseJungle(position);
				break;
			case Biome.pondJungle:
				pondJungle(position);
				break;
			case Biome.hillJungle:
				hillJungle(position);
				break;
			}
			break;
		case Biome.SWAMPWORLD:
			switch(b){
			case Biome.lakeSwamp:
				lakeSwamp(position);
				break;
			case Biome.marshSwamp:
				marshSwamp(position);
				break;
			case Biome.denseSwamp:
				denseSwamp(position);
				break;
			}
			break;
		case Biome.DESERTWORLD:
			switch(b){
			case Biome.duneDesert:
				duneDesert(position);
				break;
			case Biome.plainDesert:
				plainDesert(position);
				break;
			case Biome.oasisDesert:
				oasisDesert(position);
				break;
			case Biome.rockyDesert:
				rockyDesert(position);
				break;
			}
			break;
		}
			
	}
	private static void duneDesert(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.001f)){
			Particle newDetail = new Particle(gate);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			gates.detailsToAdd.add(newDetail);
		}
	}

	private static void rockyDesert(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.02f)){
			Particle newDetail = new Particle(spire);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			spires.detailsToAdd.add(newDetail);
		}
		
	}

	private static void oasisDesert(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.1f)){
			Particle newDetail = new Particle(thornBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			thornBushs.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+.1f, 0.05f)){
			Particle newDetail = new Particle(savannaTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			savannaTrees.detailsToAdd.add(newDetail);
		}
	}

	private static void plainDesert(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.01f)){
			Particle newDetail = new Particle(rock);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			rocks.detailsToAdd.add(newDetail);
		}
	}

	private static void denseSwamp(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.1f)){
			Particle newDetail = new Particle(forestTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			forestTrees.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.1f, 0.1f)){
			Particle newDetail = new Particle(thornBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			thornBushs.detailsToAdd.add(newDetail);
		}
		
	}

	private static void marshSwamp(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.1f)){
			Particle newDetail = new Particle(yellowBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			yellowBushs.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.1f, 0.1f)){
			Particle newDetail = new Particle(thornBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			thornBushs.detailsToAdd.add(newDetail);
		}
		
	}

	private static void lakeSwamp(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.1f)){
			Particle newDetail = new Particle(lillyPad);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, Chunk.WATERLEVEL);
			lillyPads.detailsToAdd.add(newDetail);
		}
		
	}

	private static void hillJungle(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.01f)){
			Particle newDetail = new Particle(bigTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			bigTrees.detailsToAdd.add(newDetail);
		}
		
	}

	private static void pondJungle(Vector3f position) {
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.1f, 0.1f)){
			Particle newDetail = new Particle(forestTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			forestTrees.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.2f, 0.2f)){
			Particle newDetail = new Particle(tallBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			tallBushs.detailsToAdd.add(newDetail);
		}
		
	}

	private static void denseJungle(Vector3f position){
		if (position.z > Chunk.WATERLEVEL&& check(position.z, 0.05f)){
			Particle newDetail = new Particle(bigTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			bigTrees.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.1f, 0.2f)){
			Particle newDetail = new Particle(forestTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			forestTrees.detailsToAdd.add(newDetail);
		}
		if (position.z > Chunk.WATERLEVEL&& check(position.z+0.2f, 0.1f)){
			Particle newDetail = new Particle(tallBush);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			tallBushs.detailsToAdd.add(newDetail);
		}
	}


	public static void update(long l) {
		man.stream().forEach(m -> m.update(l));
		//int m = Time.getDay();
		//seasonalTrees.colour = season[m%4];
		}

	public static void render(Vector4f renderClipPlane) {
		man.stream().forEach(m -> m.render(renderClipPlane));
	}

	/**
	 * 
	 * @param height
	 * @param chance
	 * @return
	 */
	public static boolean check(float height, float chance) {
		float h = Math.abs(Math.round(height) - height) * 100;
		if (h > chance) {
			return false;
		} else {
			return true;
		}

	}

	public static void clear() {
		man.stream().forEach(m -> m.detailsToAdd.clear());
		man.stream().forEach(m -> m.objects.clear());
	}

}
