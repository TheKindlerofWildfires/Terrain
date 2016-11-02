package physics;

import maths.Edge;
import maths.Vector3f;

public class SAT {

	private SAT() {
	}

	public static boolean test2D(Vector3f[] box, Vector3f[] other) {
		Vector3f[] boxEdges = new Vector3f[box.length];
		for (int i = 0; i < box.length - 1; i++) {
			boxEdges[i] = box[i].subtract(box[i + 1]);
		}
		boxEdges[box.length - 1] = box[box.length - 1].subtract(box[0]);

		Vector3f[] otherEdges = new Vector3f[other.length];
		for (int i = 0; i < other.length - 1; i++) {
			otherEdges[i] = other[i].subtract(other[i + 1]);
		}
		otherEdges[other.length - 1] = other[other.length - 1].subtract(other[0]);

		return false;
	}

	public static void main(String[] args) {

	}
}
