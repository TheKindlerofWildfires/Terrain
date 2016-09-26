package maths;

import stuffnooneneeds.Direction;

public class Edge {

	public Vector3f[] points = new Vector3f[2];

	public Edge(Vector3f p0, Vector3f p1) {
		points[0] = p0;
		points[1] = p1;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Edge)) {
			return false;
		}
		if (o == this) {
			return true;
		}
		Edge e = (Edge) o;
		if ((e.points[0] == this.points[0] && e.points[1] == this.points[1])
				|| (e.points[0] == this.points[1] && e.points[1] == this.points[0])) {
			return true;
		} else {
			return false;
		}
	}

	public static Vector3f intersect(Edge edge, Direction dir, float bound) {
		System.out.println(dir);
		System.out.println(edge);
		Vector3f p0 = edge.points[0];
		Vector3f p1 = edge.points[1];
		float slope = 0;
		if (p0.y != p1.y) {
			slope = (p0.y - p1.y) / (p0.x - p1.x);
		} else {
			System.err.println("ill deal with that later");
		}
		float intercept = -slope * p0.x + p0.y;
		switch (dir) {
		case NORTH:
			return new Vector3f((bound - intercept) / slope, bound, 0);
		case SOUTH:
			return new Vector3f((-bound - intercept) / slope, -bound, 0);
		case EAST:
			return new Vector3f(bound,slope*bound+intercept,0);
		case WEST:
			return new Vector3f(-bound,slope*-bound+intercept,0);
		default:
			assert false : "you done fucked up";
			return null;
		}
	}
	
	public String toString(){
		return points[0] + " ; " + points[1];
	}
}
