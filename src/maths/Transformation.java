package maths;
/**
 * Does matrix work for a model
 * @author TheKingInYellow
 *
 */
public class Transformation {

	private Matrix4f matrix;

	private Matrix4f translation;
	private Matrix4f rotation;
	private Matrix4f scale;
	/**
	 * Makes a new matrix with translation, scale and rotation
	 */
	public Transformation() {
		matrix = new Matrix4f();

		translation = new Matrix4f();
		scale = new Matrix4f();
		rotation = new Matrix4f();

	}
	/**
	 * Duplicates a model
	 * @param model
	 */
	public Transformation(Transformation model) {
		this.matrix = new Matrix4f(model.matrix);
		this.translation = new Matrix4f(model.translation);
		this.scale = new Matrix4f(model.scale);
		this.rotation = new Matrix4f(model.rotation);
	}
	/**
	 * Scales the matrix
	 * @param x
	 * @param y
	 * @param z
	 */
	public void scale(float x, float y, float z) {
		scale = scale.multiply(Matrix4f.scale(x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}
	/**
	 * Sets the matrix scale
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setScale(float x, float y, float z) {
		scale = (Matrix4f.scale(x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}
	/**
	 * Moves the matrix
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate(float x, float y, float z) {
		translation = translation.multiply(Matrix4f.translate(x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}
	/**
	 * Rotates the matrix
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotate(float angle, float x, float y, float z) {
		rotation = rotation.multiply(Matrix4f.rotate(angle, x, y, z));
		matrix = translation.multiply(rotation).multiply(scale);
	}
	/**
	 * Sets the matrix location
	 * @param x
	 * @param y
	 * @param z
	 */
	public void placeAt(float x, float y, float z) {
		translation = Matrix4f.translate(x, y, z);
		matrix = translation.multiply(rotation).multiply(scale);
		matrix = Matrix4f.translate(x, y, z);
	}
	/**
	 * Returns the matrix
	 * @return
	 */
	public Matrix4f getMatrix() {
		return matrix;
	}
}
