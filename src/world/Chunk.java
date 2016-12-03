package world;

import java.util.ArrayList;

import maths.Delaunay;
import maths.Mirror;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;
import models.VertexArrayObject;
import noiseLibrary.module.source.Perlin;
import object.GameObject;

public class Chunk extends GameObject {

	public static float SIZE;

	public static float WATERLEVEL;
	public static float BEACHSIZE;
	public static float TREELINE;

	public static int SEAWEED_PROBABILITY;
	public static int TREE_PROBABILITY;

	Perlin noise;
	float[] vertices;
	ArrayList<Triangle> terrain = new ArrayList<Triangle>();
	ArrayList<Vector3f> treeland = new ArrayList<Vector3f>();
	ArrayList<Vector3f> waterland = new ArrayList<Vector3f>();
	ArrayList<GameObject> foliage = new ArrayList<GameObject>();
	public int chunkX;
	public int chunkY;

	public Chunk(Perlin noise, int x, int y) {
		super("none", "none", true);
		this.noise = noise;
		this.chunkX = x;
		this.chunkY = y;
		this.hasMaterial = false;
		genTerrain();
		genFoliage();
		isGL = false;
		shader = graphics.ShaderManager.landShader;
	}

	public void genFoliage() {
		for (int i = 0; i < treeland.size(); i++) {
			Vector3f pos = treeland.get(i);
			GameObject f = Foliage.makeTree(pos);
			f.material.colour = new Vector3f(0.10f, 0.50f, 0.10f);
			foliage.add(f);
		}
		for (int i = 0; i < waterland.size(); i++) {
			Vector3f pos = waterland.get(i);
			GameObject f = Foliage.makeSeaweed(pos);
			f.material.colour = new Vector3f(0.06f, 0.25f, 0.06f);
			foliage.add(f);
		}

	}

	public void genTerrain() {
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();

		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = (float) (fish.points.get(i)[0] / 5000f - 1);
			float fishY = (float) (fish.points.get(i)[1] / 5000f - 1);
			points.add(new Vector3f(fishX, fishY, 0));
		}

		Mirror mirror = new Mirror(points);
		mirror.acc();
		points = mirror.points();

		Delaunay delaunay = new Delaunay(points);
		terrain = delaunay.getTriangles();
		vertices = new float[terrain.size() * 3 * 3 * 3];
		int c = 0;

		for (int i = 0; i < terrain.size(); i++) {
			Vector3f centre = terrain.get(i).getCircumcenter().add(new Vector3f(2f * chunkX, 2f * chunkY, 0))
					.scale(SIZE);
			for (int j = 0; j < 3; j++) {
				Vector3f point = terrain.get(i).getPoint(j).add(new Vector3f(2f * chunkX, 2f * chunkY, 0)).scale(SIZE);
				float[] values = getValue(centre, point);
				float h = values[3];
				float r = values[0];
				float b = values[1];
				float g = values[2];
				terrain.get(i).getPoint(j).z = h;
				
				
				float pseudo = Math.abs(h - (int) h);// remainder
				pseudo = (int) (pseudo * 100);
				pseudo = pseudo % 100;
				
				vertices[c++] = point.x;
				vertices[c++] = point.y;
				vertices[c++] = h; // the z cordinate

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;

				vertices[c++] = terrain.get(i).getNormal().x;
				vertices[c++] = terrain.get(i).getNormal().y;
				vertices[c++] = terrain.get(i).getNormal().z;
			}
		}
	}

	/**
	 * 
	 * @param centre
	 * @param point
	 * @return red, blue, green, height
	 */
	private float[] getValue(Vector3f centre, Vector3f point) {
		float r, b, g, h;
		r=b=g=h=0;
		String type = null;
		double elev = Math.abs(noise.getValue(point.x, point.y, 0.1));
		double moist = Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
		double biome = Math.abs(noise.getValue(point.x/4, point.y/4, 0.2));
		float bs = (int)(biome*3.9);
		if (bs == 0) {
			type = "swamp";//swampc
		}		
		if (bs == 1) {
			type = "hill";//hillc
		}
		if (bs == 2) {
			type = "desert";//desertn
		}
		if (bs == 3) {
			type = "mountain";//mountainn
		}
		if (bs == 4) {
			type = "ocean";//oceano
		}
		if (type == "swamp") {
			h = (float) (elev * SIZE /16+29*WATERLEVEL/32);
			r = (float) (0.2f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "hill") {
			h = (float) (elev * SIZE / 2);
			r = (float) (0.3f / (moist + 1));
			b = (float) (0.3f / (moist + 1));
			g = (float) (0.62f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		
		if (type == "desert") {
			h = (float) (elev * SIZE / 8+WATERLEVEL);
			r = (float) (0.3f / (moist + 1));
			b = (float) (0.2f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "mountain") {
			h = (float) (elev * SIZE+4*WATERLEVEL/8);
			r = (float) (0.4f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.4f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		if (type == "ocean") {
			h = (float) (elev * SIZE / 16+4*WATERLEVEL/8);
			r = (float) (0.4f / (moist + 1));
			b = (float) (0.4f / (moist + 1));
			g = (float) (0.6f / (moist + 1));
			if (h < WATERLEVEL) {
				b *= 0.6f;
				r *= 0.1f;
				g *= 0.2f;
			}
			if (h > WATERLEVEL && h < WATERLEVEL + BEACHSIZE) {
				b *= 0.5f;
				r *= 1.7f;
				g *= 1.2f;
			}
			if (h > WATERLEVEL + BEACHSIZE && h < TREELINE) {
				b *= 0.5f;
				r *= 1.2f;
				g *= 1.2f;
			}
			if (h > TREELINE) {
				b *= 0.5f;
				r *= 1.4f;
				g *= 1.1f;
			}
		}
		float[] returns = { r, b, g, h };
		return returns;
	}

	public void makeGL() {
		this.vao = new VertexArrayObject(vertices, 3);
		this.isGL = true;
	}
}
