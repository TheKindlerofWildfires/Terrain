package maths;

import graphics.Window;

import java.util.ArrayList;

/**
 * @author xWiffirat
 */
public class PoissonGenerator {
	public ArrayList<int[]> points = new ArrayList<int[]>();
	public int POINTS_PER_ITER = 20;
	public static int width = 10000;
	public int remainingPoints = 200;
	int freq = 0;
	// public ArrayList<Vector3f> xp = new ArrayList<Vector3f>();
	// public ArrayList<Vector3f> xn = new ArrayList<Vector3f>();
	// public ArrayList<Vector3f> yp = new ArrayList<Vector3f>();
	// public ArrayList<Vector3f> yn = new ArrayList<Vector3f>();

	public PoissonGenerator() {
	}

	/**
	 * Finds the best place to put a point for best spread
	 */
	public void iterate() {

		ArrayList<int[]> pts = new ArrayList<int[]>();
		for (int i = 0; i < POINTS_PER_ITER; i++) {
			int x = Window.mathRandom.nextInt(width);
			int y = Window.mathRandom.nextInt(width);
			pts.add(new int[] { x, y });
		}

		int[] bestPoint = new int[] {};
		double maxDist = 0;
		for (int[] point : pts) {
			double dist = getDistFromOthers(point);
			if (dist > maxDist) {
				maxDist = dist;
				bestPoint = point;
			}
		}
		if (check(bestPoint)) {
			points.add(bestPoint);
		} else {
			remainingPoints += 1;
		}
	}

	/**
	 * Makes sure that point is not about to mess up the sides
	 */
	private boolean check(int[] bestPoint) {
		if (bestPoint[0] > Mirror.spec || bestPoint[1] > Mirror.spec || bestPoint[0] < width - Mirror.spec
				|| bestPoint[1] < width - Mirror.spec) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Finds the distance from the nearest point
	 * 
	 * @param point
	 *            The point in question
	 * @return The distance from the nearest point
	 */
	public double getDistFromOthers(int[] point) {
		double minDist = Double.MAX_VALUE;
		for (int[] current : points) {
			double dist = distance(point, current);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	/**
	 * This makes a width by width grid of Poisson points
	 */
	public void generate() {
		Mirror m = new Mirror();
		points.addAll(m.fish());
		while (remainingPoints > 0) {
			iterate();
			remainingPoints--;
		}

	}

	public double distance(int[] p1, int[] p2) {
		int dx = p2[0] - p1[0];
		int dy = p2[1] - p1[1];
		return Math.sqrt(dx * dx + dy * dy);
	}
}
