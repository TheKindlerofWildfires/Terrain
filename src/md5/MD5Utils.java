package md5;

import maths.Vector3f;
import maths.Vector4f;

public class MD5Utils {

	public static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";

	public static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*("
			+ FLOAT_REGEXP + ")\\s*\\)";

	private MD5Utils() {
	}

	public static Vector4f calculateQuaternion(Vector3f vec) {
		Vector4f orientation = new Vector4f(vec.x, vec.y, vec.z, 0);
		float temp = 1.0f - (orientation.x * orientation.x) - (orientation.y * orientation.y)
				- (orientation.z * orientation.z);
		if (temp < 0.0f) {
			orientation.w = 0.0f;
		} else {
			orientation.w = -(float) (Math.sqrt(temp));
		}
		return orientation;
	}
}