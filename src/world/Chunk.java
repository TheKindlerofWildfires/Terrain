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
		// Generating points is 1/4
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
				// each gen takes about 1/4 of build time
				float pZ = (float) Math.abs(noise.getValue(point.x, point.y, 0.1)) * SIZE / 2;
				float cZ = (float) Math.abs(noise.getValue(centre.x, centre.y, 0.1)) * SIZE / 2;
				//	System.out.println(pZ);
				terrain.get(i).getPoint(j).z = pZ;

				float g;
				float r;
				float b;
				b = 0.5f / (cZ + 1);
				g = 0.9f / (cZ + 1);
				r = 0.5f / (cZ + 1);
				if (pZ < WATERLEVEL) {
					b *= 0.6f;
					r *= 0.1f;
					g *= 0.2f;
					if (graphics.Window.worldRandom.nextInt(SEAWEED_PROBABILITY) == 1 && !(waterland.contains(point))) {
						waterland.add(point);
					}
				}
				if (pZ > WATERLEVEL && pZ < WATERLEVEL + BEACHSIZE) {
					b *= 0.5f;
					r *= 1.7f;
					g *= 1.2f;
				}
				if (pZ > TREELINE) {
					b *= 0.5f;
					r *= 1.4f;
					g *= 1.1f;
					if (graphics.Window.worldRandom.nextInt(TREE_PROBABILITY) == 1 && !(treeland.contains(point))) {
						treeland.add(point);
					}
				}

				//cZ = pZ;
				/*
				g = (float) (cZ - 5) * (-0.1f * cZ) - 0.0f;
				b = (float) (cZ - 7) * (0.05f * cZ) + 0.5f;
				r = (float) (cZ - 7) * (-0.26f * cZ) - 2.7f;
				*/
				vertices[c++] = point.x;
				vertices[c++] = point.y;
				vertices[c++] = pZ; //the z cordinate 

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;

				vertices[c++] = terrain.get(i).getNormal().x;
				vertices[c++] = terrain.get(i).getNormal().y;
				vertices[c++] = terrain.get(i).getNormal().z;
			}
		}
	}

	public void makeGL() {
		this.vao = new VertexArrayObject(vertices, 3);
		this.isGL = true;
	}
}
