package world;

import graphics.VertexArrayObject;

import java.util.ArrayList;

import maths.Delaunay;
import maths.Mirror;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;
import noiseLibrary.module.source.Perlin;
import object.GameObject;

public class Chunk extends GameObject {

	public static final float SIZE = 9;

	public static final float WATERLEVEL = SIZE / 6;
	public static final float TREELINE = 2 * SIZE / 6;
	
	Perlin noise;
	
	ArrayList<Triangle> terrain = new ArrayList<Triangle>();
	float[] vertices;

	public int chunkX;
	public int chunkY;
/*
	private ArrayList<Vector3f> mxp;
	private ArrayList<Vector3f> mxn;
	private ArrayList<Vector3f> myp;
	private ArrayList<Vector3f> myn;
*/
	public boolean isGL = false;

	public Chunk(Perlin noise, int x, int y) {
		super("none", "none");
		this.noise = noise;
		this.chunkX = x;
		this.chunkY = y;
		// Generating points is 1/4
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();

		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = fish.points.get(i)[0] / 500f - 1;
			float fishY = fish.points.get(i)[1] / 500f - 1;
			points.add(new Vector3f(fishX, fishY, 0));
		}

		// what needs to happen is mirror also takes points from the nearblocks
		Mirror mirror = new Mirror(points);
		/*
		myn = mirror.getSide("yn");
		mxn = mirror.getSide("xn");
		myp = mirror.getSide("yp");
		mxp = mirror.getSide("xp");
		*/
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
				}
				if (pZ > TREELINE) {
					b *= 0.5f;
					r *= 1.4f;
					g *= 1.1f;
				}

				//cZ = pZ;
				/*
				g = (float) (cZ - 5) * (-0.1f * cZ) - 0.0f;
				b = (float) (cZ - 7) * (0.05f * cZ) + 0.5f;
				r = (float) (cZ - 7) * (-0.26f * cZ) - 2.7f;
				*/
				vertices[c++] = point.x;
				vertices[c++] = point.y;
				vertices[c++] = pZ;

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;

				vertices[c++] = terrain.get(i).getNormal().x;
				vertices[c++] = terrain.get(i).getNormal().y;
				vertices[c++] = terrain.get(i).getNormal().z;
			}
		}

		isGL = false;
		shader = graphics.ShaderManager.landShader;
	}

	public void makeGL() {
		this.vao = new VertexArrayObject(vertices, 3);
		this.isGL = true;
	}
}
