package world;

import java.util.ArrayList;

import graphics.DetailManager;
import graphics.Window;
import maths.Vector3f;
import maths.Vector4f;
import noiseLibrary.module.source.Perlin;
import particles.Geyser;
import particles.Particle;
import particles.ParticleEmitter;
import physics.Time;

public abstract class Detail {
	static float DETAILSCALER = 1;
	static Perlin det = World.detail;
	private static Particle bigTree;

	public static DetailManager bigTrees;

	
	public static ArrayList<DetailManager> man = new ArrayList<DetailManager>();

	//spring/summer/fall/winters
	public static Vector4f[] season = {new Vector4f(.561f, .737f, .561f, 1),new Vector4f(.133f, .545f, .133f, 1),new Vector4f(.33f, .20f, .03f, 1),new Vector4f(.70f, .80f, .70f, 1)};
	public static void init() {
		bigTree = new Particle("resources/models/detail/bigTree.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		
		bigTrees = new DetailManager(bigTree, 1000, 10, new Vector4f(.561f, .737f, .561f, 1));
		
		man.add(bigTrees);
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
				//pondJungle(position);
				break;
			case Biome.hillJungle:
				//hillJungle(position);
				break;
			}
			break;
		case Biome.SWAMPWORLD:
			switch(b){
			case Biome.lakeSwamp:
				//lakeSwamp(position);
				break;
			case Biome.marshSwamp:
				//marshSwamp(position);
				break;
			case Biome.denseSwamp:
				//denseSwamp(position);
				break;
			}
			break;
		case Biome.DESERTWORLD:
			switch(b){
			case Biome.duneDesert:
				//duneDesert(position);
				break;
			case Biome.plainDesert:
				//plainDesert(position);
				break;
			case Biome.oasisDesert:
				//oasisDesert(position);
				break;
			case Biome.rockyDesert:
				//rockyDesert(position);
				break;
			}
			break;
		}
			
	}
	private static void denseJungle(Vector3f position){
		double detail = Math.abs(det.getValue(position.x / DETAILSCALER, position.y / DETAILSCALER, position.z));
		if (detail > 1 && position.z > Chunk.WATERLEVEL){//&& check(position.z, 1)) { //something about this causes clumping --> its the detail
			Particle newDetail = new Particle(bigTree);
			newDetail.translate(position);
			newDetail.placeAt(position.x, position.y, position.z / Chunk.SIZE);
			bigTrees.detailsToAdd.add(newDetail);
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
	 *            0-100 plz
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

}
