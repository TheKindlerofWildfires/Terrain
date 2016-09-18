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
	public static final float PERLINSCALER = 20;//higher = smoother
	Perlin noise = new Perlin();
	private VertexArrayObject VAO;
	
	public static int perlinSeed;
	/**
	 * Building better worlds
	 * 		tl;Dr: Uses poisson disk, delauny and perlin noise to great a cool map
	 */
	public World() {
		
		noise.setFrequency(1);
		noise.setLacunarity(2);
		noise.setOctaveCount(3);
		noise.setSeed(perlinSeed);
		terrain = new ArrayList<Triangle>();
		int[] offset = {0,0};
		float[] zero = Chunk(offset);
		offset[0] = 1;
		float[] one = Chunk(offset);
		float[] fin = new float[zero.length+one.length];
		for(int i = 0; i<fin.length;i++){
			if(i<zero.length){
				fin[i]=zero[i];
			}else{
				fin[i] = one[i-zero.length];
			}
		}
		VAO = new VertexArrayObject(zero, 2);
	}
	private float[] Chunk(int[] offset) {
		points = new ArrayList<Vector3f>();
		System.out.println(offset[0] + " " +offset[1]);
		PoissonGenerator fish = new PoissonGenerator();
		fish.generate();
		for (int i = 0; i < fish.points.size(); i++) {
			float fishX = fish.points.get(i)[0]/500f-1f;//+(offset[0]);
			float fishY = fish.points.get(i)[1]/500f-1f;//+(offset[1]);
			Vector3f thisVec = new Vector3f(fishX, fishY, 0);
			points.add(thisVec);
		}
		Delaunay delaunay = new Delaunay(points,offset);
		terrain = delaunay.getTriangles();
		float[] vertices = new float[terrain.size() * 3 * 3 * 2];
		int c = 0;
		float mx = 0;
		for (int i = 0; i < terrain.size(); i++) {
			for (int j = 0; j < 3; j++) {
				float pZ = (float) Math
						.abs(noise.getValue(terrain.get(i).getPoint(j).x/PERLINSCALER, terrain.get(i).getPoint(j).y/PERLINSCALER, 0.1))*4;
				/*float nX = (terrain.get(i).getPoint(0).x+terrain.get(i).getPoint(1).x+terrain.get(i).getPoint(2).x)/3;
				float nY = (terrain.get(i).getPoint(0).y+terrain.get(i).getPoint(1).y+terrain.get(i).getPoint(2).y)/3;
				float nZ = (float)Math.abs(noise.getValue(nX/PERLINSCALER, nY/PERLINSCALER, 0.1));
				float qZ = pZ;
				pZ = nZ;
				*/
				//0-4.7
				
				if (pZ>mx){
					mx = pZ;
				}
				float g;
				float r;
				float b;
				g = (float)(pZ -5)*(-0.1f*pZ)-0.0f;
				b = (float)(pZ -7)*(0.05f*pZ)+0.4f;
				r = (float)(pZ -7)*(-0.26f*pZ)-2.6f;
				if(pZ>4.7){
					b = 0;
				}
				vertices[c++] = terrain.get(i).getPoint(j).x;
				vertices[c++] = terrain.get(i).getPoint(j).y;
				vertices[c++] = pZ;//could be qZ

				vertices[c++] = r;
				vertices[c++] = g;
				vertices[c++] = b;
			}
		}
		return vertices;
		
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
