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

/**
 * @author TheKingInYellow & HMSRothman
 */
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
	ArrayList<GameObject> details = new ArrayList<GameObject>();
	public int chunkX;
	public int chunkY;

	public Chunk(Perlin noise, int x, int y) {
		super("none", "none", true);
		this.noise = noise;
		this.chunkX = x;
		this.chunkY = y;
		this.hasMaterial = false;
		genTerrain();
		isGL = false;
		shader = graphics.ShaderManager.landShader;
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
			int p1 = 0;
			for (int j = 0; j < 3; j++) {
				Vector3f point = terrain.get(i).getPoint(j).add(new Vector3f(2f * chunkX, 2f * chunkY, 0)).scale(SIZE);
				float[] values = Biome.getPlanet(centre, point);
				float h = values[3];
				float r = values[0];
				float b = values[1];
				float g = values[2];
				point.add(new Vector3f(0, 0, h));

				Detail.detail(point, values[4]);
				terrain.get(i).getPoint(j).z = h;

				float p = Math.abs(h - (int) h);// remainder
				p = (int) (p * 100);
				p1 = (int) p % 100;
				vertices[c++] = point.x;
				vertices[c++] = point.y;
				vertices[c++] = h;
				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;
				vertices[c++] = terrain.get(i).getNormal().x;
				vertices[c++] = terrain.get(i).getNormal().y;
				vertices[c++] = terrain.get(i).getNormal().z;

			}
			if(p1 == 0){
				for(int q = 1; q<27;q++){
					vertices[c-q] = 0;
				}
			}
		}
	}

	public void makeGL() {
		this.vao = new VertexArrayObject(vertices, 3);
		this.isGL = true;
	}

}
