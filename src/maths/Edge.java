package maths;


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
	
	public String toString(){
		return points[0] + " ; " + points[1];
	}
}
