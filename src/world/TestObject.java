package world;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import graphics.VertexArrayObject;
import object.Mesh;
import object.OBJLoader;
import object.Render;
import object.Texture;
public class TestObject {
	float[] positions = new float[] {
			// VO
			-0.5f, 0.5f, 0.5f,
			// V1
			-0.5f, -0.5f, 0.5f,
			// V2
			0.5f, -0.5f, 0.5f,
			// V3
			0.5f, 0.5f, 0.5f,
			// V4
			-0.5f, 0.5f, -0.5f,
			// V5
			0.5f, 0.5f, -0.5f,
			// V6
			-0.5f, -0.5f, -0.5f,
			// V7
			0.5f, -0.5f, -0.5f, };
	byte[] indices = new byte[] { 0, 1, 3, 3, 1, 2,
			// Top Face
			4, 0, 3, 5, 4, 3,
			// Right face
			3, 2, 7, 5, 3, 7,
			// Left face
			0, 1, 6, 4, 0, 6,
			// Bottom face
			6, 1, 2, 7, 6, 2,
			// Back face
			4, 6, 7, 5, 4, 7, };
	float[] textureCoords = new float[] { 0, 0, 0, 0.5f, 0.5f, 0.5f, 0.5f, 0,
			0, 0.5f, 0.5f, 0.5f, 0, 1, 0.5f, 1 };
	Texture texture;
	Mesh mesh;
	Render render;
	VertexArrayObject vao;
	public TestObject() {
		texture = new Texture();
		try {
			mesh	=	OBJLoader.loadMesh("src/models/drone1.obj");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		float[] positions = mesh.getPos();
		float[] textCoords = mesh.getTextCoords();
		float[] normals = mesh.getNormals();
		float[] vertices=new float[positions.length*textCoords.length*normals.length];
		for(int i= 0; i<vertices.length;i+=3){
			vertices[i]=positions[i/(textCoords.length*normals.length)];
			vertices[i+1]=textCoords[i/(positions.length*normals.length)];
			vertices[i+2]=normals[i/(textCoords.length*positions.length)];
		}
		System.out.println(vertices[2]);
		vao = new VertexArrayObject(vertices,indices, 3);

	}

	public void render() {
		//render.render(mesh);
		graphics.ShaderManager.objectShader.start();
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, positions.length);
		graphics.ShaderManager.objectShader.stop();
		//objectShader.
		
	}
}
