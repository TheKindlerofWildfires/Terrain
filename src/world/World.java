package world;

import static graphics.ShaderManager.landShader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.Random;

import graphics.VertexArrayObject;
import maths.Delaunay;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;

public class World {
	private ArrayList<Triangle> terrain;
	private ArrayList<Vector3f> points;

	private VertexArrayObject VAO;

	private Random rng;

	public World() {
		terrain = new ArrayList<Triangle>();
		points = new ArrayList<Vector3f>();
		rng = new Random();
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();
		for (int i = 0; i < fish.points.size(); i++) {
			points.add(new Vector3f(rng.nextFloat()*2-1, rng.nextFloat()*2-1,
					0));
		}
		System.out.println("points: " + points.size());
		//float size = 2f;
		//for (int i = 0; i < 5; i++) {
		//	points.add(new Vector3f(rng.nextFloat() * size - size / 2, rng.nextFloat() * size - size / 2, .5f));
		//}
		//points.add(new Vector3f(0,.5f,0));
		//points.add(new Vector3f(.1f,.75f,0));
		//points.add(new Vector3f(0f,.25f,0));
		//points.add(new Vector3f(-.2f,.6f,0));
		//points.add(new Vector3f(-.2f,.2f,0));

		//points.add(new Vector3f(.2f,-.5f,0));
		Delaunay delaunay = new Delaunay(points);
		terrain = delaunay.getTriangles();
		float[] vertices = new float[terrain.size() * 3 * 3 * 2];
		int c = 0;
		for (int i = 0; i < terrain.size(); i++) {

			float r = rng.nextFloat();
			float g = rng.nextFloat();
			float b = rng.nextFloat();

			vertices[c++] = terrain.get(i).getPoint(0).x;
			vertices[c++] = terrain.get(i).getPoint(0).y;
			vertices[c++] = terrain.get(i).getPoint(0).z;

			vertices[c++] = r;
			vertices[c++] = g;
			vertices[c++] = b;

			vertices[c++] = terrain.get(i).getPoint(1).x;
			vertices[c++] = terrain.get(i).getPoint(1).y;
			vertices[c++] = terrain.get(i).getPoint(1).z;

			vertices[c++] = r;
			vertices[c++] = g;
			vertices[c++] = b;

			vertices[c++] = terrain.get(i).getPoint(2).x;
			vertices[c++] = terrain.get(i).getPoint(2).y;
			vertices[c++] = terrain.get(i).getPoint(2).z;

			vertices[c++] = r;
			vertices[c++] = g;
			vertices[c++] = b;

		}

		VAO = new VertexArrayObject(vertices, 2);
	}

	public void render() {
		landShader.start();
		glBindVertexArray(VAO.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, terrain.size() * 3);
		landShader.stop();
	}
}
