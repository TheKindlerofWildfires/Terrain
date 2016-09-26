package maths;

/**
 * This class represents a (x,y,z)-Vector. GLSL equivalent to vec3.
 *
 * @author Heiko Brumme
 */
public class Vector2f {

	public float x;
	public float y;

	/**
	 * Creates a default 3-tuple vector with all values set to 0.
	 */
	public Vector2f() {
		this.x = 0f;
		this.y = 0f;
	}

	/**
	 * Creates a 3-tuple vector with specified values.
	 *
	 * @param x x value
	 * @param y y value
	 * @param z z value
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates the squared length of the vector.
	 *
	 * @return Squared length of this vector
	 */
	public float lengthSquared() {
		return x * x + y * y;
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
	public Vector2f normalize() {
		float length = length();
		return divide(length);
	}

	/**
	 * Adds this vector to another vector.
	 *
	 * @param other The other vector
	 * @return Sum of this + other
	 */
	public Vector2f add(Vector2f other) {
		float x = this.x + other.x;
		float y = this.y + other.y;
		return new Vector2f(x, y);
	}

	/**
	 * Negates this vector.
	 *
	 * @return Negated vector
	 */
	public Vector2f negate() {
		return scale(-1f);
	}

	/**
	 * Subtracts this vector from another vector.
	 *
	 * @param other The other vector
	 * @return Difference of this - other
	 */
	public Vector2f subtract(Vector2f other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies a vector by a scalar.
	 *
	 * @param scalar Scalar to multiply
	 * @return Scalar product of this * scalar
	 */
	public Vector2f scale(float scalar) {
		float x = this.x * scalar;
		float y = this.y * scalar;
		return new Vector2f(x, y);
	}

	/**
	 * Divides a vector by a scalar.
	 *
	 * @param scalar Scalar to multiply
	 * @return Scalar quotient of this / scalar
	 */
	public Vector2f divide(float scalar) {
		return scale(1f / scalar);
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 *
	 * @param other The other vector
	 * @return Dot product of this * other
	 */
	public float dot(Vector2f other) {
		return this.x * other.x + this.y * other.y;
	}

	/**
	 * Calculates a linear interpolation between this vector with another
	 * vector.
	 *
	 * @param other The other vector
	 * @param alpha The alpha value, must be between 0.0 and 1.0
	 * @return Linear interpolated vector
	 */
	public Vector2f lerp(Vector2f other, float alpha) {
		return this.scale(1f - alpha).add(other.scale(alpha));
	}

	public static Vector2f midpoint(Vector2f a, Vector2f b) {
		return new Vector2f((a.x + b.x) / 2, (a.y + b.y) / 2);
	}

	public String toString() {
		return x + "," + y;
	}

	public float length2() {
		return (float) Math.pow(this.length(), 2);
	}
}