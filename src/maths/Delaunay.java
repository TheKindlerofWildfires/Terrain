package maths;

import java.util.ArrayList;

public class Delaunay {

	/**
	 * Performs Delanay Triangulation via Boyer-Watson Algorithm
	 */

	public static final Vector3f startingTriPt0 = new Vector3f(-3f, -3f, 0);
	public static final Vector3f startingTriPt1 = new Vector3f(3f, -3f, 0);
	public static final Vector3f startingTriPt2 = new Vector3f(0, 3f, 0);

	public static final Triangle startingTri = new Triangle(startingTriPt0, startingTriPt1, startingTriPt2);

	private ArrayList<Triangle> triangles;
	private boolean calculated = false;

	private void addPoint(Vector3f pt) {
		System.out.println("added a pt");
		ArrayList<Triangle> badTris = new ArrayList<Triangle>();//triangles changing/going away
		ArrayList<Triangle> goodTris = new ArrayList<Triangle>();
		ArrayList<Edge> edges = new ArrayList<Edge>();//edges of space opened up by removed tris
		for (Triangle tri : triangles) { //discover which tris are effected
			if (tri.isInCircumsphere(pt)) {
				badTris.add(tri);
			} else {
				goodTris.add(tri);
			}
		}
		System.out.println(badTris.size());
		for (Triangle tri : badTris) { //work out edges and remove bad triangles
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
		System.out.println(triangles.size());
		triangles = goodTris;
	}

	public Delaunay(ArrayList<Vector3f> points) {
		triangles = new ArrayList<Triangle>();
		triangles.add(startingTri);
		for (Vector3f point : points) {
			addPoint(point);
		}
		System.out.println(triangles.size());
		for (int i = 0; i < triangles.size(); i++) {
			Triangle tri = triangles.get(i);
			for (int j = 0; j < 3; j++) {
				if (!tri.getPoint(j).onScreen()) {
					triangles.remove(tri);
					break;
				}
			}
		}
		System.out.println(triangles.size());
		calculated = true;

	}

	public ArrayList<Triangle> getTriangles() {
		assert calculated : "you need to math it before you can have any triangles!";
		return triangles;
	}
}