package maths;

import java.util.ArrayList;

/**
 * What is wrong? Its pretty simple actually If my specificity is high then the
 * triangles are thin and look wrong If my specificity is low then there is
 * overlap with other points essentially I think this idea is bust without
 * smarter coder
 * 
 * @author Mitchell
 *
 */
public class Shear {
	private ArrayList<Triangle> triangles = new ArrayList<Triangle>();

	private ArrayList<Triangle> triangles2 = new ArrayList<Triangle>();

	private ArrayList<Vector3f> xp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> xn = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> yp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> yn = new ArrayList<Vector3f>();

	private ArrayList<Vector3f> mxp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> mxn = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myn = new ArrayList<Vector3f>();

	ArrayList<Triangle> txp = new ArrayList<Triangle>();
	ArrayList<Triangle> typ = new ArrayList<Triangle>();
	ArrayList<Triangle> txn = new ArrayList<Triangle>();
	ArrayList<Triangle> tyn = new ArrayList<Triangle>();
	PoissonGenerator fish;
	float specificity = 0.7f;
	int iter = 0;

	public Shear(ArrayList<Triangle> terrain, PoissonGenerator fish) {
		this.fish = fish;
		triangles = terrain;
		for (int i = 0; i < terrain.size(); i++) {
			//clarify(triangles.get(i));
		}
		corner();
		translate();
		correct();
		triggle();

		triangles.addAll(txp);
		triangles.addAll(typ);
		triangles.addAll(txn);
		triangles.addAll(tyn);
		// System.out.println(triangles2.get(1).getPoint(0)+"|"+triangles2.get(1).getPoint(1)+"|"+triangles2.get(1).getPoint(2));

		/*
		 * samplecode for(int i=-1;i<xp.size();i++){//iterates through the xp
		 * list to find out if the point should be chosen
		 * if((xp.get(i).x-xp.get(i+1).x)<spacing ){ xp. do some removeing from
		 * delaunay } }
		 */
	}

	private void translate() {
		this.xp = fish.xp;
		this.xn = fish.xn;
		this.yp = fish.yp;
		this.yn = fish.yn;
		for(int i = 0; i<fish.xp.size();i++){
			xp.get(i).x = xp.get(i).x/ 500f - 1;
			xp.get(i).y = xp.get(i).y/ 500f - 1;
			
		}
		for(int i = 0; i<fish.yp.size();i++){
			yp.get(i).x = yp.get(i).x/ 500f - 1;
			yp.get(i).y = yp.get(i).y/ 500f - 1;
		}
		for(int i = 0; i<fish.xn.size();i++){
			xn.get(i).x = xn.get(i).x/ 500f - 1;
			xn.get(i).y = xn.get(i).y/ 500f - 1;
		}
		for(int i = 0; i<fish.yn.size();i++){
			yn.get(i).x = yn.get(i).x/ 500f - 1;
			yn.get(i).y = yn.get(i).y/ 500f - 1;
		}
	}

	private void corner() {
		xp.add(new Vector3f(specificity, 1, 0));
		xp.add(new Vector3f(specificity, -1, 0));
		yp.add(new Vector3f(1, specificity, 0));
		yp.add(new Vector3f(-1, specificity, 0));
		xn.add(new Vector3f(-specificity, 1, 0));
		xn.add(new Vector3f(-specificity, -1, 0));
		yn.add(new Vector3f(1, -specificity, 0));
		yn.add(new Vector3f(-1, -specificity, 0));
	}

	private void triggle() {
		for (int i = 1; i < xp.size(); i++) {
			int pt1 = i / 2;
			int pt2 = (i - 1) / 2;
			if (pt1 == pt2) {
				txp.add(new Triangle(xp.get(pt1), mxp.get(pt2), xp.get(pt2 + 1)));
			} else {
				txp.add(new Triangle(xp.get(pt1), mxp.get(pt2), mxp
						.get(pt2 + 1)));
			}

		}
		for (int i = 1; i < yp.size(); i++) {
			int pt1 = i / 2;
			int pt2 = (i - 1) / 2;
			if (pt1 == pt2) {
				typ.add(new Triangle(yp.get(pt1), myp.get(pt2), yp.get(pt2 + 1)));
			} else {
				typ.add(new Triangle(yp.get(pt1), myp.get(pt2), myp
						.get(pt2 + 1)));
			}

		}
		for (int i = 1; i < xn.size(); i++) {
			int pt1 = i / 2;
			int pt2 = (i - 1) / 2;
			if (pt1 == pt2) {
				txn.add(new Triangle(xn.get(pt1), mxn.get(pt2), xn.get(pt2 + 1)));
			} else {
				txn.add(new Triangle(xn.get(pt1), mxn.get(pt2), mxn
						.get(pt2 + 1)));
			}

		}
		for (int i = 1; i < yn.size(); i++) {
			int pt1 = i / 2;
			int pt2 = (i - 1) / 2;
			if (pt1 == pt2) {
				tyn.add(new Triangle(yn.get(pt1), myn.get(pt2), yn.get(pt2 + 1)));
			} else {
				tyn.add(new Triangle(yn.get(pt1), myn.get(pt2), myn
						.get(pt2 + 1)));
			}

		}

	}

	/**
	 * returns true if two of the points match else returns false
	 * 
	 * @param tri
	 * @param other
	 * @return
	 */
	private boolean matchTwoPoints(Triangle tri, Triangle other) {
		int matchedPoints = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tri.getPoint(i).x == other.getPoint(j).x
						&& tri.getPoint(i).y == other.getPoint(j).y) {
					matchedPoints++;
				}
			}

		}
		// System.out.println(matchedPoints);
		if (matchedPoints > 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This function changes the lists to now contain the mirror points
	 */
	private void correct() {
		for (int i = 0; i < xp.size(); i++) {
			mxp.add(new Vector3f(1, xp.get(i).y, 0));
		}
		for (int i = 0; i < yp.size(); i++) {
			myp.add(new Vector3f(yp.get(i).x, 1, 0));
		}
		for (int i = 0; i < xn.size(); i++) {
			mxn.add(new Vector3f(-1, xn.get(i).y, 0));
		}
		for (int i = 0; i < yn.size(); i++) {
			myn.add(new Vector3f(yn.get(i).x, -1, 0));
		}

	}

	/**
	 * This function puts the points that are close to the edges into
	 * appropriate lists
	 * 
	 * @param triangle
	 */
	private void clarify(Triangle triangle) {
		for (int j = 0; j < 3; j++) {
			if (triangle.getPoint(j).x > specificity) {
				xp.add(triangle.getPoint(j));
			}
			if (triangle.getPoint(j).y > specificity) {
				yp.add(triangle.getPoint(j));
			}
			if (triangle.getPoint(j).x < -specificity) {
				xn.add(triangle.getPoint(j));
			}
			if (triangle.getPoint(j).y < -specificity) {
				yn.add(triangle.getPoint(j));
			}
		}

	}

	public ArrayList<Triangle> fix() {
		if (triangles.size() == 0) {
			return null;
		}
		return triangles;
	}

}
