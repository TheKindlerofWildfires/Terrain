package maths;

import java.util.ArrayList;

public class Delaunay {

	/**
	 * Performs Delanay Triangulation via Boyer-Watson Algorithm
	 */

	public static final Vector3f startingTriPt0 = new Vector3f(-3f, -3f, 0);
	public static final Vector3f startingTriPt1 = new Vector3f(3f, -3f, 0);
	public static final Vector3f startingTriPt2 = new Vector3f(0, 3f, 0);

	public static final Triangle startingTri = new Triangle(startingTriPt0,
			startingTriPt1, startingTriPt2);

	private ArrayList<Triangle> triangles;
	private boolean calculated = false;
	/**
	 * Adds a point and makes triangles with it
	 * @param pt
	 * 			The point added
	 */
	private void addPoint(Vector3f pt) {
		// System.out.println("added a pt");
		ArrayList<Triangle> badTris = new ArrayList<Triangle>();// triangles
																// changing/going
																// away
		ArrayList<Triangle> goodTris = new ArrayList<Triangle>();
		ArrayList<Edge> edges = new ArrayList<Edge>();// edges of space opened
														// up by removed tris
		for (Triangle tri : triangles) { // discover which tris are effected
			if (tri.isInCircumsphere(pt)) {
				badTris.add(tri);
			} else {
				goodTris.add(tri);
			}
		}
		// System.out.println(badTris.size());
		for (Triangle tri : badTris) { // work out edges and remove bad
										// triangles
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
		// System.out.println(triangles.size());
		triangles = goodTris;
	}
	/**
	 * Creates a Delaunay Triangulation with the given points
	 * @param points
	 * 			The points used to make the Triangulation
	 */
	public Delaunay(ArrayList<Vector3f> points) {
		triangles = new ArrayList<Triangle>();
		triangles.add(startingTri);

		// ftriangles.remove(startingTri);
		for (Vector3f point : points) {
			addPoint(point);
		}
		boolean breaker = true;
		/**
		 * It should be noted that this micro-function could use to be optimised
		 * It scans known good triangles over and over to find the bad ones that hide
		 */
		while (breaker) {
			int b1 = triangles.size();
			for (int i = 0; i < triangles.size(); i++) {
				Triangle tri = triangles.get(i);
				for (int j = 0; j < 3; j++) {
					if (tri.getPoint(j) == startingTriPt0
							|| tri.getPoint(j) == startingTriPt1
							|| tri.getPoint(j) == startingTriPt2) {
						triangles.remove(tri);
						break;
					}
				}
			}
			int b2 = triangles.size();
			if(b2 ==b1){
				breaker = false;
			}
		}
	}

	/*
	 * Things i've learned A) All the triangles that pass the sieve are good B)
	 * If we do if(true) remove triangles triangles still remain C) However the
	 * bad triangles are still in the triangles list D) And if we clear it the
	 * triangles go away E) The iteration never finishes F) All this is
	 * occurring because the size of the triangle list is shrinking as we go
	 * G)And now very clearly I am not removing the triangles correctly
	 */
	public ArrayList<Triangle> getTriangles() {
		assert calculated : "you need to math it before you can have any triangles!";
		return triangles;
	}

	/*
	 * public static void main(String[] args) { Vector3f pt = new
	 * Vector3f(0,3,0);
	 * 
	 * if (pt.subtract(startingTriPt0).length() < 1 ||
	 * pt.subtract(startingTriPt1).length() < 1 ||
	 * pt.subtract(startingTriPt2).length() < 1) { System.out.println("YO");
	 * 
	 * } }
	 */
}