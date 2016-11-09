package models;

public class Mesh {

	public boolean isGL = false;
	public float[] vaoData;
	public VertexArrayObject vao;

	public Mesh(float[] vaoData) {
		this.vaoData = vaoData;
		this.isGL = false;
	}

	public void makeGL(int numberOfVec3s) {
		vao = new VertexArrayObject(vaoData, numberOfVec3s);
		this.isGL = true;
	}

}
