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

	public boolean isInside(Direction dir, float bound) {
		switch (dir) {
		case NORTH:
			return y < bound;
		case SOUTH:
			return y > -bound;
		case EAST:
			return x < bound;
		case WEST:
			return x > -bound;
		default:
			assert false : "wtf";
			return false;
		}
	}

	/**
	 * Creates a default 3-tuple vector with all values set to 0.
	 */
	public Vector3f() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
	}

	/**
	 * Creates a 3-tuple vector with specified values.
	 *
	 * @param x x value
	 * @param y y value
	 * @param z z value
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 *
	 * @return Normalized vector
	 */
	public Vector3f normalize() {
		float length = length();
		return divide(length);
	}

	/**
	 * Adds this vector to another vector.
	 *
	 * @param other The other vector
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
	 * @param other The other vector
	 * @return Difference of this - other
	 */
	public Vector3f subtract(Vector3f other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies a vector by a scalar.
	 *
	 * @param scalar Scalar to multiply
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
	 * @param scalar Scalar to multiply
	 * @return Scalar quotient of this / scalar
	 */
	public Vector3f divide(float scalar) {
		return scale(1f / scalar);
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 *
	 * @param other The other vector
	 * @return Dot product of this * other
	 */
	public float dot(Vector3f other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 *
	 * @param other The other vector
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
	 * @param other The other vector
	 * @param alpha The alpha value, must be between 0.0 and 1.0
	 * @return Linear interpolated vector
	 */
	public Vector3f lerp(Vector3f other, float alpha) {
		return this.scale(1f - alpha).add(other.scale(alpha));
	}

	public static Vector3f midpoint(Vector3f a, Vector3f b) {
		return new Vector3f((a.x + b.x) / 2, (a.y + b.y) / 2, (a.z + b.z) / 2);
	}

	public String toString() {
		return x + "," + y + "," + z;
	}

	public float length2() {
		return (float) Math.pow(this.length(), 2);
	}
}