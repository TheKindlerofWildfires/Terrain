package world;

import static graphics.ShaderManager.landShader;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import graphics.VertexArrayObject;

import java.util.ArrayList;

import maths.Delaunay;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class World {
	private ArrayList<Triangle> terrain;
	private ArrayList<Vector3f> points;
	public static final float PERLINSCALER = 2;

	private VertexArrayObject VAO;

	public static int perlinSeed;
	/**
	 * Building better worlds
	 * 		tl;Dr: Uses poisson disk, delauny and perlin noise to great a cool map
	 */
	public World() {
		terrain = new ArrayList<Triangle>();
		points = new ArrayList<Vector3f>();
		Perlin noise = new Perlin();
		noise.setFrequency(1);
		noise.setLacunarity(2);
		noise.setOctaveCount(3);
		noise.setSeed(perlinSeed);
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();
		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = fish.points.get(i)[0] / 500f - 1;
			float fishY = fish.points.get(i)[1] / 500f - 1;
			Vector3f thisVec = new Vector3f(fishX, fishY, 0);
			points.add(thisVec);
		}
		Delaunay delaunay = new Delaunay(points);
		terrain = delaunay.getTriangles();
		float[] vertices = new float[terrain.size() * 3 * 3 * 2];
		int c = 0;
		for (int i = 0; i < terrain.size(); i++) {
			for (int j = 0; j < 3; j++) {
				float pZ = (float) Math
						.abs(noise.getValue(terrain.get(i).getPoint(j).x/PERLINSCALER, terrain.get(i).getPoint(j).y/PERLINSCALER, 0.1));

				float g = -0.25f*pZ*pZ +0.25f*pZ+0.15f;
				float r;
				float b;
				if (pZ>0.5){
					r = -0.5f*pZ*pZ +0.75f*pZ-0.1f;
					b = 0;
				}else{
					r = 0;
					b =-2*pZ +0.6f;
				}
				vertices[c++] = terrain.get(i).getPoint(j).x;
				vertices[c++] = terrain.get(i).getPoint(j).y;
				vertices[c++] = pZ;

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;
			}
		}

		VAO = new VertexArrayObject(vertices, 2);
	}
	/**
	 * Going to assume this drawls
	 */
	public void render() {
		landShader.start();
		glBindVertexArray(VAO.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, terrain.size() * 3);
		landShader.stop();
	}
}
