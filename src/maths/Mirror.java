package maths;

import graphics.Window;

import java.util.ArrayList;
import java.util.Random;
/**
 * @author TheKingInYellow
 */
public class Mirror {
	private ArrayList<Vector3f> points;
	private ArrayList<Vector3f> mxp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> mxn = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myp = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> myn = new ArrayList<Vector3f>();
	//float specificity = 0.6f;
	static int spec = PoissonGenerator.width-500; 
	static int iterator = 1250;
	Random rng = Window.mathRandom;
	public Mirror(ArrayList<Vector3f> points) {
		this.points = points;
		standard();
	}

	public Mirror() {
		standard();
	}

	/**
	 * I can't remember what this does, but when you take it away the wholes come back
	 * 
	 * @return
	 */
	public ArrayList<int[]> fish() {
		
		ArrayList<int[]> fish = new ArrayList<int[]>();
		for (int i = 0; i <= PoissonGenerator.width; i += iterator) {
			fish.add(new int[] { i, 1 });
			fish.add(new int[] { 1, i });
			fish.add(new int[] { i, -1 });
			fish.add(new int[] { -1, i });
		}
		return fish;
	}

	/**
	 * Makes a point shell for poisson
	 */
	private void standard() {
		for (float i = -1; i <= 1; i += .25f) {
			mxp.add(new Vector3f(1, i, 0));
			myp.add(new Vector3f(i, 1, 0));
			mxn.add(new Vector3f(-1, i, 0));
			myn.add(new Vector3f(i, -1, 0));
		}

	}
	public void acc() {
		points.addAll(mxp);
		points.addAll(myp);
		points.addAll(mxn);
		points.addAll(myn);

	}
	
	public ArrayList<Vector3f> points() {
		return points;
	}
	public ArrayList<Vector3f> getSide(String side) {
		switch (side) {
		case "yn":
			return myn;
		case "yp":
			return myp;
		case "xp":
			return mxp;
		case "xn":
			return mxn;
		default:
			System.err.println("invalid type");
			return null;
		}
	}

}
