package maths;

public class Triangle {

	private Vector3f[] points = new Vector3f[3];
	private float circumradius;
	private Vector3f circumcenter;

	public Triangle(Vector3f... pts) {
		if (pts.length != 3) {
			System.err.println("Triangles need 3 points!");
			System.exit(-1);
		}
		points = pts;
		calculateCircumcircle();
	}

	public Triangle(Edge edge, Vector3f pt) {
		points[0] = pt;
		points[1] = edge.points[0];
		points[2] = edge.points[1];
		calculateCircumcircle();
	}

	public Vector3f getPoint(int i) {
		return points[i];
	}

	public Edge getEdge(int i) {
		if (i < 2) {
			return new Edge(points[i], points[i + 1]);
		} else if (i == 2) {
			return new Edge(points[2], points[0]);
		} else {
			System.err.println("you done fucked up");
			return null;
		}
	}

	private void calculateCircumcircle() {
		Vector3f ac = points[2].subtract(points[0]);
		Vector3f ab = points[1].subtract(points[0]);
		Vector3f abXac = ab.cross(ac);

		// this is the vector from a TO the circumsphere center
		Vector3f toCircumsphereCenter = (abXac.cross(ab).scale(ac.length2()).add(ac.cross(abXac).scale(ab.length2())))
				.divide(2.f * abXac.length2());
		circumradius = toCircumsphereCenter.length();

		// The 3 space coords of the circumsphere center then:
		circumcenter = points[0].add(toCircumsphereCenter); // now this is the actual 3space location
	}

	public boolean isInCircumsphere(Vector3f pt) {
		return (pt.subtract(circumcenter).length() <= circumradius);
	}

	public Vector3f getCircumcenter() {
		return circumcenter;
	}

	public float getCircumradius() {
		return circumradius;
	}
	public void translate(Vector3f displacement){
		for(int j = 0; j<3;j++){
			points[j].x += displacement.x;
			points[j].y += displacement.y;
			points[j].z += displacement.z;
			
		}
	}
}