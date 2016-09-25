package world;

//import static graphics.ShaderManager.landShader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import graphics.VertexArrayObject;

import java.util.ArrayList;

import maths.Delaunay;
import maths.Mirror;
import maths.PoissonGenerator;
import maths.Triangle;
import maths.Vector3f;
import noiseLibrary.module.source.Perlin;

public class Chunk {

	private static final float WATERLEVEL = 1.3f;
	Perlin noise;
	ArrayList<Triangle> terrain = new ArrayList<Triangle>();

	private VertexArrayObject VAO;
	private int chunkX;
	private int chunkY;

	public Chunk(Perlin noise, int x, int y) {
		this.noise = noise;
		this.chunkX = x;
		this.chunkY = y;
		//Generating points is 1/4
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();
		
		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = fish.points.get(i)[0] / 500f - 1;
			float fishY = fish.points.get(i)[1] / 500f - 1;
			points.add(new Vector3f(fishX, fishY, 0));
		}
	
		//Delaunay takes 1/4
		Mirror mirror = new Mirror(points);
		points = mirror.give();
		Delaunay delaunay = new Delaunay(points);
		terrain = delaunay.getTriangles();
		//Shear shear = new Shear(terrain,fish);
		//terrain = shear.fix();
		float[] vertices = new float[terrain.size() * 3 * 3 * 2];
		int c = 0;
		
		for (int i = 0; i < terrain.size(); i++) {
			Vector3f centre = terrain.get(i).getCircumcenter().add(new Vector3f(2f * chunkX, 2f * chunkY, 0));
			for (int j = 0; j < 3; j++) {
				Vector3f point = terrain.get(i).getPoint(j).add(new Vector3f(2f*chunkX, 2f*chunkY, 0));
				//each gen takes about 1/4 of build time
				float pZ = (float) Math.abs(noise.getValue(point.x , point.y, 0.1)) *4;
				float cZ = (float) Math.abs(noise.getValue(centre.x, centre.y, 0.1))*4;

				float g;
				float r;
				float b;
				if(pZ<WATERLEVEL){
					cZ*=0.7;
				}
				g = (float) (cZ - 5) * (-0.1f * cZ) - 0.0f;
				b = (float) (cZ - 7) * (0.05f * cZ) + 0.5f;
				r = (float) (cZ - 7) * (-0.26f * cZ) - 2.7f;

				if (cZ > 4.7) {
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
		graphics.ShaderManager.landShader.start();
		glBindVertexArray(VAO.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, terrain.size() * 3);
		graphics.ShaderManager.landShader.stop();
		
	}
}
