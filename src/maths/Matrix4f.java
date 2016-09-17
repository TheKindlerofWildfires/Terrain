package maths;
/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Heiko Brumme
 */
public class Matrix4f {

	public float m00, m01, m02, m03;
	public float m10, m11, m12, m13;
	public float m20, m21, m22, m23;
	public float m30, m31, m32, m33;

	/**
	 * Creates a 4x4 identity matrix.
	 */
	public Matrix4f() {
		setIdentity();
	}

	/**
	 * Creates a 4x4 matrix with specified columns.
	 *
	 * @param col1 Vector with values of the first column
	 * @param col2 Vector with values of the second column
	 * @param col3 Vector with values of the third column
	 * @param col4 Vector with values of the fourth column
	 */

	public Matrix4f(float i00, float i01, float i02, float i03, float i10, float i11, float i12, float i13, float i20,
			float i21, float i22, float i23, float i30, float i31, float i32, float i33) {
		m00 = i00;
		m01 = i01;
		m02 = i02;
		m03 = i03;
		m10 = i10;
		m11 = i11;
		m12 = i12;
		m13 = i13;
		m20 = i20;
		m21 = i21;
		m22 = i22;
		m23 = i23;
		m30 = i30;
		m31 = i31;
		m32 = i32;
		m33 = i33;
	}

	public Matrix4f(Vector4f col1, Vector4f col2, Vector4f col3, Vector4f col4) {
		m00 = col1.x;
		m10 = col1.y;
		m20 = col1.z;
		m30 = col1.w;

		m01 = col2.x;
		m11 = col2.y;
		m21 = col2.z;
		m31 = col2.w;

		m02 = col3.x;
		m12 = col3.y;
		m22 = col3.z;
		m32 = col3.w;

		m03 = col4.x;
		m13 = col4.y;
		m23 = col4.z;
		m33 = col4.w;
	}

	/**
	 * Sets this matrix to the identity matrix.
	 */
	public final void setIdentity() {
		m00 = 1f;
		m11 = 1f;
		m22 = 1f;
		m33 = 1f;

		m01 = 0f;
		m02 = 0f;
		m03 = 0f;
		m10 = 0f;
		m12 = 0f;
		m13 = 0f;
		m20 = 0f;
		m21 = 0f;
		m23 = 0f;
		m30 = 0f;
		m31 = 0f;
		m32 = 0f;
	}

	/**
	 * Adds this matrix to another matrix.
	 *
	 * @param other The other matrix
	 * @return Sum of this + other
	 */
	public Matrix4f add(Matrix4f other) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 + other.m00;
		result.m10 = this.m10 + other.m10;
		result.m20 = this.m20 + other.m20;
		result.m30 = this.m30 + other.m30;

		result.m01 = this.m01 + other.m01;
		result.m11 = this.m11 + other.m11;
		result.m21 = this.m21 + other.m21;
		result.m31 = this.m31 + other.m31;

		result.m02 = this.m02 + other.m02;
		result.m12 = this.m12 + other.m12;
		result.m22 = this.m22 + other.m22;
		result.m32 = this.m32 + other.m32;

		result.m03 = this.m03 + other.m03;
		result.m13 = this.m13 + other.m13;
		result.m23 = this.m23 + other.m23;
		result.m33 = this.m33 + other.m33;

		return result;
	}

	/**
	 * Negates this matrix.
	 *
	 * @return Negated matrix
	 */
	public Matrix4f negate() {
		return multiply(-1f);
	}

	/**
	 * Subtracts this matrix from another matrix.
	 *
	 * @param other The other matrix
	 * @return Difference of this - other
	 */
	public Matrix4f subtract(Matrix4f other) {
		return this.add(other.negate());
	}

	/**
	 * Multiplies this matrix with a scalar.
	 *
	 * @param scalar The scalar
	 * @return Scalar product of this * scalar
	 */
	public Matrix4f multiply(float scalar) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 * scalar;
		result.m10 = this.m10 * scalar;
		result.m20 = this.m20 * scalar;
		result.m30 = this.m30 * scalar;

		result.m01 = this.m01 * scalar;
		result.m11 = this.m11 * scalar;
		result.m21 = this.m21 * scalar;
		result.m31 = this.m31 * scalar;

		result.m02 = this.m02 * scalar;
		result.m12 = this.m12 * scalar;
		result.m22 = this.m22 * scalar;
		result.m32 = this.m32 * scalar;

		result.m03 = this.m03 * scalar;
		result.m13 = this.m13 * scalar;
		result.m23 = this.m23 * scalar;
		result.m33 = this.m33 * scalar;

		return result;
	}

	/**
	 * Multiplies this matrix to a vector.
	 *
	 * @param vector The vector
	 * @return Vector product of this * other
	 */
	public Vector4f multiply(Vector4f vector) {
		float x = this.m00 * vector.x + this.m01 * vector.y + this.m02 * vector.z + this.m03 * vector.w;
		float y = this.m10 * vector.x + this.m11 * vector.y + this.m12 * vector.z + this.m13 * vector.w;
		float z = this.m20 * vector.x + this.m21 * vector.y + this.m22 * vector.z + this.m23 * vector.w;
		float w = this.m30 * vector.x + this.m31 * vector.y + this.m32 * vector.z + this.m33 * vector.w;
		return new Vector4f(x, y, z, w);
	}

	/**
	 * Multiplies this matrix to another matrix.
	 *
	 * @param other The other matrix
	 * @return Matrix product of this * other
	 */
	public Matrix4f multiply(Matrix4f other) {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20 + this.m03 * other.m30;
		result.m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20 + this.m13 * other.m30;
		result.m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20 + this.m23 * other.m30;
		result.m30 = this.m30 * other.m00 + this.m31 * other.m10 + this.m32 * other.m20 + this.m33 * other.m30;

		result.m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21 + this.m03 * other.m31;
		result.m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21 + this.m13 * other.m31;
		result.m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21 + this.m23 * other.m31;
		result.m31 = this.m30 * other.m01 + this.m31 * other.m11 + this.m32 * other.m21 + this.m33 * other.m31;

		result.m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22 + this.m03 * other.m32;
		result.m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22 + this.m13 * other.m32;
		result.m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22 + this.m23 * other.m32;
		result.m32 = this.m30 * other.m02 + this.m31 * other.m12 + this.m32 * other.m22 + this.m33 * other.m32;

		result.m03 = this.m00 * other.m03 + this.m01 * other.m13 + this.m02 * other.m23 + this.m03 * other.m33;
		result.m13 = this.m10 * other.m03 + this.m11 * other.m13 + this.m12 * other.m23 + this.m13 * other.m33;
		result.m23 = this.m20 * other.m03 + this.m21 * other.m13 + this.m22 * other.m23 + this.m23 * other.m33;
		result.m33 = this.m30 * other.m03 + this.m31 * other.m13 + this.m32 * other.m23 + this.m33 * other.m33;

		return result;
	}

	/**
	 * Transposes this matrix.
	 *
	 * @return Transposed matrix
	 */
	public Matrix4f transpose() {
		Matrix4f result = new Matrix4f();

		result.m00 = this.m00;
		result.m10 = this.m01;
		result.m20 = this.m02;
		result.m30 = this.m03;

		result.m01 = this.m10;
		result.m11 = this.m11;
		result.m21 = this.m12;
		result.m31 = this.m13;

		result.m02 = this.m20;
		result.m12 = this.m21;
		result.m22 = this.m22;
		result.m32 = this.m23;

		result.m03 = this.m30;
		result.m13 = this.m31;
		result.m23 = this.m32;
		result.m33 = this.m33;

		return result;
	}

	/**
	 * Returns the Buffer representation of this vector.
	 *
	 * @return Vector as FloatBuffer
	 */
	public FloatBuffer getBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(m00).put(m10).put(m20).put(m30);
		buffer.put(m01).put(m11).put(m21).put(m31);
		buffer.put(m02).put(m12).put(m22).put(m32);
		buffer.put(m03).put(m13).put(m23).put(m33);
		buffer.flip();
		return buffer;
	}

	/**
	 * Creates a orthographic projection matrix. Similar to
	 * <code>glOrtho(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left Coordinate for the left vertical clipping pane
	 * @param right Coordinate for the right vertical clipping pane
	 * @param bottom Coordinate for the bottom horizontal clipping pane
	 * @param top Coordinate for the bottom horizontal clipping pane
	 * @param near Coordinate for the near depth clipping pane
	 * @param far Coordinate for the far depth clipping pane
	 * @return Orthographic matrix
	 */
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f ortho = new Matrix4f();

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		ortho.m00 = 2f / (right - left);
		ortho.m11 = 2f / (top - bottom);
		ortho.m22 = -2f / (far - near);
		ortho.m03 = tx;
		ortho.m13 = ty;
		ortho.m23 = tz;

		return ortho;
	}

	/**
	 * Creates a perspective projection matrix. Similar to
	 * <code>glFrustum(left, right, bottom, top, near, far)</code>.
	 *
	 * @param left Coordinate for the left vertical clipping pane
	 * @param right Coordinate for the right vertical clipping pane
	 * @param bottom Coordinate for the bottom horizontal clipping pane
	 * @param top Coordinate for the bottom horizontal clipping pane
	 * @param near Coordinate for the near depth clipping pane, must be positive
	 * @param far Coordinate for the far depth clipping pane, must be positive
	 * @return Perspective matrix
	 */
	public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f frustum = new Matrix4f();

		float a = (right + left) / (right - left);
		float b = (top + bottom) / (top - bottom);
		float c = -(far + near) / (far - near);
		float d = -(2f * far * near) / (far - near);

		frustum.m00 = (2f * near) / (right - left);
		frustum.m11 = (2f * near) / (top - bottom);
		frustum.m02 = a;
		frustum.m12 = b;
		frustum.m22 = c;
		frustum.m32 = -1f;
		frustum.m23 = d;
		frustum.m33 = 0f;

		return frustum;
	}

	/**
	 * Creates a perspective projection matrix. Similar to
	 * <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
	 *
	 * @param fovy Field of view angle in degrees
	 * @param aspect The aspect ratio is the ratio of width to height
	 * @param near Distance from the viewer to the near clipping plane, must be
	 * positive
	 * @param far Distance from the viewer to the far clipping plane, must be
	 * positive
	 * @return Perspective matrix
	 */
	public static Matrix4f perspective(float fovy, float aspect, float near, float far) {
		Matrix4f perspective = new Matrix4f();

		float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));

		perspective.m00 = f / aspect;
		perspective.m11 = f;
		perspective.m22 = (far + near) / (near - far);
		perspective.m32 = -1f;
		perspective.m23 = (2f * far * near) / (near - far);
		perspective.m33 = 0f;

		return perspective;
	}

	/**
	 * Creates a translation matrix. Similar to
	 * <code>glTranslate(x, y, z)</code>.
	 *
	 * @param x x coordinate of translation vector
	 * @param y y coordinate of translation vector
	 * @param z z coordinate of translation vector
	 * @return Translation matrix
	 */
	public static Matrix4f translate(float x, float y, float z) {
		Matrix4f translation = new Matrix4f();

		translation.m03 = x;
		translation.m13 = y;
		translation.m23 = z;

		return translation;
	}

	public static Matrix4f inverse(Matrix4f mat) {
		float s = mat.determinant();
		if (s == 0.0) {
			return null;
		}

		s = 1 / s;

		Matrix4f result = new Matrix4f(
				mat.m11 * (mat.m22 * mat.m33 - mat.m23 * mat.m32) + mat.m12 * (mat.m23 * mat.m31 - mat.m21 * mat.m33) + mat.m13 * (mat.m21 * mat.m32 - mat.m22 * mat.m31),
				mat.m21 * (mat.m02 * mat.m33 - mat.m03 * mat.m32) + mat.m22 * (mat.m03 * mat.m31 - mat.m01 * mat.m33) + mat.m23 * (mat.m01 * mat.m32 - mat.m02 * mat.m31),
				mat.m31 * (mat.m02 * mat.m13 - mat.m03 * mat.m12) + mat.m32 * (mat.m03 * mat.m11 - mat.m01 * mat.m13) + mat.m33 * (mat.m01 * mat.m12 - mat.m02 * mat.m11),
				mat.m01 * (mat.m13 * mat.m22 - mat.m12 * mat.m23) + mat.m02 * (mat.m11 * mat.m23 - mat.m13 * mat.m21) + mat.m03 * (mat.m12 * mat.m21 - mat.m11 * mat.m22),

				mat.m12 * (mat.m20 * mat.m33 - mat.m23 * mat.m30) + mat.m13 * (mat.m22 * mat.m30 - mat.m20 * mat.m32) + mat.m10 * (mat.m23 * mat.m32 - mat.m22 * mat.m33),
				mat.m22 * (mat.m00 * mat.m33 - mat.m03 * mat.m30) + mat.m23 * (mat.m02 * mat.m30 - mat.m00 * mat.m32) + mat.m20 * (mat.m03 * mat.m32 - mat.m02 * mat.m33),
				mat.m32 * (mat.m00 * mat.m13 - mat.m03 * mat.m10) + mat.m33 * (mat.m02 * mat.m10 - mat.m00 * mat.m12) + mat.m30 * (mat.m03 * mat.m12 - mat.m02 * mat.m13),
				mat.m02 * (mat.m13 * mat.m20 - mat.m10 * mat.m23) + mat.m03 * (mat.m10 * mat.m22 - mat.m12 * mat.m20) + mat.m00 * (mat.m12 * mat.m23 - mat.m13 * mat.m22),

				mat.m13 * (mat.m20 * mat.m31 - mat.m21 * mat.m30) + mat.m10 * (mat.m21 * mat.m33 - mat.m23 * mat.m31) + mat.m11 * (mat.m23 * mat.m30 - mat.m20 * mat.m33),
				mat.m23 * (mat.m00 * mat.m31 - mat.m01 * mat.m30) + mat.m20 * (mat.m01 * mat.m33 - mat.m03 * mat.m31) + mat.m21 * (mat.m03 * mat.m30 - mat.m00 * mat.m33),
				mat.m33 * (mat.m00 * mat.m11 - mat.m01 * mat.m10) + mat.m30 * (mat.m01 * mat.m13 - mat.m03 * mat.m11) + mat.m31 * (mat.m03 * mat.m10 - mat.m00 * mat.m13),
				mat.m03 * (mat.m11 * mat.m20 - mat.m10 * mat.m21) + mat.m00 * (mat.m13 * mat.m21 - mat.m11 * mat.m23) + mat.m01 * (mat.m10 * mat.m23 - mat.m13 * mat.m20),

				mat.m10 * (mat.m22 * mat.m31 - mat.m21 * mat.m32) + mat.m11 * (mat.m20 * mat.m32 - mat.m22 * mat.m30) + mat.m12 * (mat.m21 * mat.m30 - mat.m20 * mat.m31),
				mat.m20 * (mat.m02 * mat.m31 - mat.m01 * mat.m32) + mat.m21 * (mat.m00 * mat.m32 - mat.m02 * mat.m30) + mat.m22 * (mat.m01 * mat.m30 - mat.m00 * mat.m31),
				mat.m30 * (mat.m02 * mat.m11 - mat.m01 * mat.m12) + mat.m31 * (mat.m00 * mat.m12 - mat.m02 * mat.m10) + mat.m32 * (mat.m01 * mat.m10 - mat.m00 * mat.m11),
				mat.m00 * (mat.m11 * mat.m22 - mat.m12 * mat.m21) + mat.m01 * (mat.m12 * mat.m20 - mat.m10 * mat.m22) + mat.m02 * (mat.m10 * mat.m21 - mat.m11 * mat.m20));

		result.multiply(s);
		return result;
	}

	public float determinant() {
		// less *,+,- calculation than expanded expression.
		return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32) - (m00 * m12 - m02 * m10) * (m21 * m33 - m23 * m31)
				+ (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31) + (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
				- (m01 * m13 - m03 * m11) * (m20 * m32 - m22 * m30) + (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
	}

	/**
	 * Creates a rotation matrix. Similar to
	 * <code>glRotate(angle, x, y, z)</code>.
	 *
	 * @param angle Angle of rotation in degrees
	 * @param x x coordinate of the rotation vector
	 * @param y y coordinate of the rotation vector
	 * @param z z coordinate of the rotation vector
	 * @return Rotation matrix
	 */
	public static Matrix4f rotate(float angle, float x, float y, float z) {
		Matrix4f rotation = new Matrix4f();

		float c = (float) Math.cos(Math.toRadians(angle));
		float s = (float) Math.sin(Math.toRadians(angle));
		Vector3f vec = new Vector3f(x, y, z);
		if (vec.length() != 1f) {
			vec = vec.normalize();
			x = vec.x;
			y = vec.y;
			z = vec.z;
		}

		rotation.m00 = x * x * (1f - c) + c;
		rotation.m10 = y * x * (1f - c) + z * s;
		rotation.m20 = x * z * (1f - c) - y * s;
		rotation.m01 = x * y * (1f - c) - z * s;
		rotation.m11 = y * y * (1f - c) + c;
		rotation.m21 = y * z * (1f - c) + x * s;
		rotation.m02 = x * z * (1f - c) + y * s;
		rotation.m12 = y * z * (1f - c) - x * s;
		rotation.m22 = z * z * (1f - c) + c;

		return rotation;
	}

	/**
	 * Creates a scaling matrix. Similar to <code>glScale(x, y, z)</code>.
	 *
	 * @param x Scale factor along the x coordinate
	 * @param y Scale factor along the y coordinate
	 * @param z Scale factor along the z coordinate
	 * @return Scaling matrix
	 */
	public static Matrix4f scale(float x, float y, float z) {
		Matrix4f scaling = new Matrix4f();

		scaling.m00 = x;
		scaling.m11 = y;
		scaling.m22 = z;

		return scaling;
	}

	public static Matrix4f gluLookAt(Vector3f eye, Vector3f center, Vector3f up) {
		Vector3f forward = center.subtract(eye).normalize();
		Vector3f side = forward.cross(up).normalize();
		up = side.cross(forward).normalize();
		Matrix4f result = new Matrix4f();
		result.m00 = side.x;
		result.m01 = side.y;
		result.m02 = side.z;
		result.m10 = up.x;
		result.m11 = up.y;
		result.m12 = up.z;
		result.m20 = -forward.x;
		result.m21 = -forward.y;
		result.m22 = -forward.z;
		return result.multiply(Matrix4f.translate(-eye.x, -eye.y, -eye.z));
	}
	
	public String toString(){
		return (m00 + "," + m01 + "," + m02 + "," + m03 + "\n" +
				m10 + "," + m11 + "," + m12 + "," + m13 + "\n" + 
				m20 + "," + m21 + "," + m22 + "," + m23 + "\n" +
				m30 + "," + m31 + "," + m32 + "," + m33 + "\n");
	}
}