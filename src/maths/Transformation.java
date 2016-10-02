package maths;

public class Transformation {

	private Matrix4f matrix;

	private Matrix4f translation;
	private Matrix4f rotation;
	private Matrix4f scale;

	public Transformation() {
		matrix = new Matrix4f();

		translation = new Matrix4f();
		scale = new Matrix4f();
		rotation = new Matrix4f();

	}

	public void scale(float x, float y, float z) {
		scale = scale.multiply(Matrix4f.scale(x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}

	public void translate(float x, float y, float z) {
		translation = translation.multiply(Matrix4f.translate(x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}

	public void rotate(float angle, float x, float y, float z) {
		rotation = rotation.multiply(Matrix4f.rotate(angle, x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}

	public void placeAt(float x, float y, float z) {
		translation = Matrix4f.translate(x, y, z);
		matrix = translation.multiply(rotation).multiply(scale);
	}

	public Matrix4f getMatrix() {
		return matrix;
	}
}
