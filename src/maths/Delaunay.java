package maths;

import java.util.ArrayList;
import java.util.Collections;

public class Delaunay {
	/**
	 * Performs Delanay Triangulation via Boyer-Watson Algorithm
	 */
	static float xOf = 3;
	static float yOf = 3;
	public static Vector3f startingTriPt0 = new Vector3f(-xOf, -yOf, 0);
	public static Vector3f startingTriPt1 = new Vector3f(xOf, -yOf, 0);
	public static Vector3f startingTriPt2 = new Vector3f(0, yOf, 0);

	public static Triangle startingTri = new Triangle(startingTriPt0, startingTriPt1, startingTriPt2);

	private ArrayList<Triangle> triangles;
	private boolean calculated = false;

	/**
	 * Adds a point and makes triangles with it
	 * 
	 * @param pt
	 *            The point added
	 */
	private void addPoint(Vector3f pt) {
		ArrayList<Triangle> badTris = new ArrayList<Triangle>();
		ArrayList<Triangle> goodTris = new ArrayList<Triangle>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Triangle tri : triangles) {
			if (tri.isInCircumsphere(pt)) {
				badTris.add(tri);
			} else {
				goodTris.add(tri);
			}
		}
		for (Triangle tri : badTris) {
			for (int i = 0; i < 3; i++) {
				Edge edge = tri.getEdge(i);
				boolean isIn = false;
				for (int j = 0; j < edges.size(); j++) {
					if (edge.equals(edges.get(j))) {
						edges.remove(j);
						isIn = true;
						break;
					}
				}
				if (!isIn) {
					edges.add(edge);
				}
			}
		}
		for (Edge edge : edges) {
			goodTris.add(new Triangle(edge, pt));
		}
		triangles = goodTris;
	}

	/**
	 * Creates a Delaunay Triangulation with the given points
	 * 
	 * @param points
	 *            The points used to make the Triangulation
	 * @param offset
	 */
	public Delaunay(ArrayList<Vector3f> points) {
		triangles = new ArrayList<Triangle>();
		triangles.add(startingTri);

		for (Vector3f point : points) {
			addPoint(point);
		}
		for (int i = 0; i < triangles.size(); i++) {
			Triangle tri = triangles.get(i);
			for (int j = 0; j < 3; j++) {
				if (tri.getPoint(j) == startingTriPt0 || tri.getPoint(j) == startingTriPt1
						|| tri.getPoint(j) == startingTriPt2) {
					triangles.set(i, startingTri);
					break;
				}
			}
		}
		triangles.removeAll(Collections.singleton(startingTri));
		calculated = true;
	}

	public ArrayList<Triangle> getTriangles() {
		assert calculated : "you need to math it before you can have any triangles!";
		return triangles;
	}
}