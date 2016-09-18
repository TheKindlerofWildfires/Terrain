package world;

import static graphics.ShaderManager.landShader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;

import noiseLibrary.module.source.Perlin;
import graphics.VertexArrayObject;
import graphics.Window;
import maths.Delaunay;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;

public class World {
	private ArrayList<Triangle> terrain;
	private ArrayList<Vector3f> points;

	private VertexArrayObject VAO;

	public static int perlinSeed;

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
			//points.add(new Vector3f(rng.nextFloat()*2-1, rng.nextFloat()*2-1,0));
			float fishX = fish.points.get(i)[0] / 320f - 1;
			float fishY = fish.points.get(i)[1] / 240f - 1;

			Vector3f thisVec = new Vector3f(fishX, fishY, 0);
			points.add(thisVec);

			//System.out.println("x "+fishX+"y "+fishY);
		}
		//System.out.println("points: " + points.size());
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
			/*
			float r = (terrain.get(i).getPoint(0).z + terrain.get(i).getPoint(1).z +terrain.get(i).getPoint(2).z)/10;
			float g = (terrain.get(i).getPoint(0).z + terrain.get(i).getPoint(1).z +terrain.get(i).getPoint(2).z)/2;
			float b = (terrain.get(i).getPoint(0).z + terrain.get(i).getPoint(1).z +terrain.get(i).getPoint(2).z)/1;
			*/
			//float pZ =  (float) Math.abs(noise.getValue(terrain.get(i).getCircumcenter().x, terrain.get(i).getCircumcenter().y, 0.1));


			for (int j = 0; j < 3; j++) {
				float pZ = (float) Math
						.abs(noise.getValue(terrain.get(i).getPoint(j).x, terrain.get(i).getPoint(j).y, 0.1));

				float g = -0.25f*pZ*pZ +0.25f*pZ+0.1f;
				float r;
				float b;
				if (pZ>0.5){
					r = -0.5f*pZ*pZ +0.75f*pZ-0.15f;
					b = 0;
				}else{
					r = 0;
					b =-2*pZ +0.5f;
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

	public void render() {
		landShader.start();
		glBindVertexArray(VAO.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, terrain.size() * 3);
		landShader.stop();
	}
}
