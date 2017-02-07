package maths;

/**
 * This class represents a (x,y,z)-Vector. GLSL equivalent to vec3.
 *
 * @author Heiko Brumme
 */
public class Vector3f {

	public float x;
	public float y;
	public float z;

	/**
	 * Creates a default 3-tuple vector with all values set to 0.
	 */
	public Vector3f() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
	}

	public Vector3f(Vector2f vec, float z) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = z;
	}

	/**
	 * Creates a 3-tuple vector with specified values.
	 *
	 * @param x
	 *            x value
	 * @param y
	 *            y value
	 * @param z
	 *            z value
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector4f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	public Vector3f(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	/**
	 * Calculates the squared length of the vector.
	 *
	 * @return Squared length of this vector
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Calculates the length of the vector.
	 *
	 * @return Length of this vector
	 */
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * Normalizes the vector.
	 * if the vector is zero it returns zero
	 * @return Normalized vector
	 */
	public Vector3f normalize() {
		float length = length();
		if(length == 0){
			return new Vector3f(0,0,0);
		}
		return divide(length);
	}

	/**
	 * Adds this vector to another vector.
	 *
	 * @param other
	 *            The other vector
	 * @return Sum of this + other
	 */
	public Vector3f add(Vector3f other) {
		float x = this.x + other.x;
		float y = this.y + other.y;
		float z = this.z + other.z;
		return new Vector3f(x, y, z);
	}

	/**
	 * Negates this vector.
	 *
	 * @return Negated vector
	 */
	public Vector3f negate() {
		return scale(-1f);
	}

	/**
	 * Subtracts this vector from another vector.
	 *
	 * @param other
	 *            The other vector
	 * @return Difference of this - other
	 */
	public Vector3f subtract(Vector3f other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies a vector by a scalar.
	 *
	 * @param scalar
	 *            Scalar to multiply
	 * @return Scalar product of this * scalar
	 */
	public Vector3f scale(float scalar) {
		float x = this.x * scalar;
		float y = this.y * scalar;
		float z = this.z * scalar;
		return new Vector3f(x, y, z);
	}

	/**
	 * Divides a vector by a scalar.
	 *
	 * @param scalar
	 *            Scalar to multiply
	 * @return Scalar quotient of this / scalar
	 */
	public Vector3f divide(float scalar) {
		return scale(1f / scalar);
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 *
	 * @param other
	 *            The other vector
	 * @return Dot product of this * other
	 */
	public float dot(Vector3f other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 *
	 * @param other
	 *            The other vector
	 * @return Cross product of this x other
	 */
	public Vector3f cross(Vector3f other) {
		float x = this.y * other.z - this.z * other.y;
		float y = this.z * other.x - this.x * other.z;
		float z = this.x * other.y - this.y * other.x;
		return new Vector3f(x, y, z);
	}

	/**
	 * Calculates a linear interpolation between this vector with another
	 * vector.
	 *
	 * @param other
	 *            The other vector
	 * @param alpha
	 *            The alpha value, must be between 0.0 and 1.0
	 * @return Linear interpolated vector
	 */
	public Vector3f lerp(Vector3f other, float alpha) {
		return this.scale(1f - alpha).add(other.scale(alpha));
	}

	public String toString() {
		return x + "," + y + "," + z;
	}

	/**
	 * @return length of this vector squared
	 */
	public float length2() {
		return (float) Math.pow(this.length(), 2);
	}

	public static Vector3f parseVector(String input) {
		String[] data = input.split(",");
		if (data.length >= 3) {
			return new Vector3f(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
		} else if (data.length == 2) {
			return new Vector3f(Float.parseFloat(data[0]), Float.parseFloat(data[1]), 0);
		} else if (data.length == 1) {
			return new Vector3f(Float.parseFloat(data[0]), 0, 0);
		} else {
			return new Vector3f(0, 0, 0);
		}
	}
}