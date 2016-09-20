package world;

import static graphics.ShaderManager.landShader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;

import graphics.VertexArrayObject;
import maths.Delaunay;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class Chunk {

	Perlin noise;
	ArrayList<Triangle> terrain = new ArrayList<Triangle>();

	private VertexArrayObject VAO;
	private int chunkX;
	private int chunkY;

	public Chunk(Perlin noise, int x, int y) {
		this.noise = noise;
		this.chunkX = x;
		this.chunkY = y;

		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();
		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = fish.points.get(i)[0] / 500f - 1;
			float fishY = fish.points.get(i)[1] / 500f - 1;
			points.add(new Vector3f(fishX, fishY, 0));
		}

		Delaunay delaunay = new Delaunay(points);
		terrain = delaunay.getTriangles();

		float[] vertices = new float[terrain.size() * 3 * 3 * 2];
		int c = 0;
		for (int i = 0; i < terrain.size(); i++) {
			Vector3f centre = terrain.get(i).getCircumcenter().add(new Vector3f(2f * chunkX, 2f * chunkY, 0));
			for (int j = 0; j < 3; j++) {
				Vector3f point = terrain.get(i).getPoint(j).add(new Vector3f(2f*chunkX, 2f*chunkY, 0));
				float pZ = (float) Math.abs(noise.getValue(point.x , point.y, 0.1)) * 4;
				float cZ = (float) Math.abs(noise.getValue(centre.x, centre.y, 0.1))*4;
				
				float g;
				float r;
				float b;

				g = (float) (cZ - 5) * (-0.1f * cZ) - 0.0f;
				b = (float) (cZ - 7) * (0.05f * cZ) + 0.4f;
				r = (float) (cZ - 7) * (-0.26f * cZ) - 2.6f;
				if (pZ > 4.7) {
					b = 0;
				}
				vertices[c++] = point.x;
				vertices[c++] = point.y;
				vertices[c++] = pZ;

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;
			}
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
