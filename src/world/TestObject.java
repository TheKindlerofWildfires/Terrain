package world;

import maths.Vector3f;
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
	int[] indices = new int[] { 0, 1, 3, 3, 1, 2,
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

	public TestObject() {
		texture = new Texture();
		try {
			mesh	=	OBJLoader.loadMesh("src/models/drone.obj");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		render = new Render();

	}

	public void render() {
		render.render(mesh);
	}
}
