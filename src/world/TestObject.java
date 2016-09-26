package world;

import graphics.VertexArrayObject;
import object.Mesh;
import object.OBJLoader;
import object.Renderer;
import object.Texture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL11.*;

public class TestObject {
	float[] positions = new float[] {
			// VO
			-0.5f,0.5f,0.5f,
			// V1
			-0.5f,-0.5f,0.5f,
			// V2
			0.5f,-0.5f,0.5f,
			// V3
			0.5f,0.5f,0.5f,
			// V4
			-0.5f,0.5f,-0.5f,
			// V5
			0.5f,0.5f,-0.5f,
			// V6
			-0.5f,-0.5f,-0.5f,
			// V7
			0.5f,-0.5f,-0.5f, };
	byte[] indices = new byte[] { 0,1,3,3,1,2,
	// Top Face
	4,0,3,5,4,3,
	// Right face
	3,2,7,5,3,7,
	// Left face
	0,1,6,4,0,6,
	// Bottom face
	6,1,2,7,6,2,
	// Back face
	4,6,7,5,4,7, };
	float[] textureCoords = new float[] { 0,0,0,0.5f,0.5f,0.5f,0.5f,0,0,0.5f,0.5f,0.5f,0,1,0.5f,1 };
	Texture texture;
	Mesh mesh;
	Renderer render;
	VertexArrayObject vao;

	public TestObject() {
		texture = new Texture();
		try {
			vao = OBJLoader.loadMesh("src/models/drone1.obj");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*float[] positions = mesh.getPos();
		float[] textCoords = mesh.getTextCoords();
		float[] normals = mesh.getNormals();
		float[] tCoords = new float[positions.length];
		float[] vertices = new float[positions.length * 3];
		
		int c = 0;
		for (int i = 0; i < tCoords.length; i++) {
			if (i % 3 < 2) {
				tCoords[i] = textCoords[c++];
			} else {
				tCoords[i] = 0;
			}
		}
		
		c = 0;
		for (int i = 0; i < positions.length; i += 3) {
			//System.out.println(i + " , " + c);
			vertices[c++] = positions[i];
			vertices[c++] = positions[i + 1];
			vertices[c++] = positions[i + 2];
		
			vertices[c++] = normals[i];
			vertices[c++] = normals[i + 1];
			vertices[c++] = normals[i + 2];
		
			vertices[c++] = tCoords[i];
			vertices[c++] = tCoords[i + 1];
			vertices[c++] = tCoords[i + 2];
		}
		*/
	}

	public void render() {
		//render.render(mesh);
		graphics.ShaderManager.objectShader.start();
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, indices.length);
		graphics.ShaderManager.landShader.stop();
		//objectShader.

	}
}
