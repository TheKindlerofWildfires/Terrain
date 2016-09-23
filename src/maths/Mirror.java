package maths;

import java.util.ArrayList;

public class Mirror {
	private ArrayList<Vector3f> points;
	private ArrayList<Vector3f> xp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> xn = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> yp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> yn = new ArrayList<Vector3f>();

	private ArrayList<Vector3f> mxp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> mxn = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myn = new ArrayList<Vector3f>();
	float specificity = 0.85f;

	public Mirror(ArrayList<Vector3f> points) {
		this.points = points;
		corner();
		divide();
		spread();
		acc();
	}

	private void acc() {
		points.addAll(mxp);
		points.addAll(myp);
		points.addAll(mxn);
		points.addAll(myn);
		
	}
	private void corner() {
		//xp.add(new Vector3f(specificity, 1, 0));
		//xp.add(new Vector3f(specificity, -1, 0));
		yp.add(new Vector3f(1, specificity, 0));
		yp.add(new Vector3f(-1, specificity, 0));
		//xn.add(new Vector3f(-specificity, 1, 0));
		//xn.add(new Vector3f(-specificity, -1, 0));
		yn.add(new Vector3f(1, -specificity, 0));
		yn.add(new Vector3f(-1, -specificity, 0));
	}

	private void divide() {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x > specificity) {
				xp.add(points.get(i));
			}
			if (points.get(i).y > specificity) {
				yp.add(points.get(i));
			}
			if (points.get(i).x < -specificity) {
				xn.add(points.get(i));
			}
			if (points.get(i).y < -specificity) {
				yn.add(points.get(i));
			}

		}
	}

	public ArrayList<Vector3f> give() {
		return points;
	}

	private void spread() {
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
}
