package maths;

import graphics.Window;

import java.util.ArrayList;

public class PoissonGenerator {

	public ArrayList<int[]> points = new ArrayList<int[]>();
	public int POINTS_PER_ITER = 20;
	public int width = 1000;
	public int height = 1000;
	public int remainingPoints = 100;

	int freq=0;
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;

	public PoissonGenerator() {
	}

	/**
	 * Finds the best place to put a point for best spread
	 */
	public void iterate() {
		ArrayList<int[]> pts = new ArrayList<int[]>();
		for (int i = 0; i < POINTS_PER_ITER; i++) {
			int x = Window.mathRandom.nextInt(width);
			int y = Window.mathRandom.nextInt(height);
			pts.add(new int[] { x,y });
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
		points.add(bestPoint);
	}

	/**
	 * Finds the distance from the nearest point
	 * @param point
	 * 			The point in question
	 * @return
	 * 			The distance fromt the nearest point
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
	 * This makes a 100 by 100 grid of Poisson points
	 */
	public void generate() {
		int x = Window.mathRandom.nextInt(width);
		int y = Window.mathRandom.nextInt(height);
		points.add(new int[] { x,y });
		while (remainingPoints > 0) {
			iterate();
			remainingPoints--;
		}

		/*//float spacing = width / freq;
		for (int i = 0; i < 4; i++) {
			float pX = 0;
			float pY = 0;
			switch (i) {
			case NORTH:
				pX = -width;
				pY = height;
				break;
			case SOUTH:
				pX = -width;
				pY = -height;
				break;
			case EAST:
				pX = width;
				pY = -height;
				break;
			case WEST:
				pX = -width;
				pY = -height;
				break;
			default:
				assert false : "too many sides to the square";
			}
			for (int j = 0; j < freq; j++) {
				switch (i) {
				case NORTH:
					points.add(new int[] { (int) pX,(int) pY });
					//pX += spacing;
					break;
				case SOUTH:
					points.add(new int[] { (int) pX,(int) pY });
				//	pX += spacing;
					break;
				case EAST:
					points.add(new int[] { (int) pX,(int) pY });
				//	pY += spacing;
					break;
				case WEST:
					points.add(new int[] { (int) pX,(int) pY });
				//	pY += spacing;
					break;
				default:
					System.err.println("you done fucked up");
				}
			}
		}*/
	}

	/**
	 * a**2 + b**2 = c**2
	 * @param p1
	 * 			a
	 * @param p2
	 * 			b
	 * @return
	 * 			c
	 */
	public double distance(int[] p1, int[] p2) {
		int dx = p2[0] - p1[0];
		int dy = p2[1] - p1[1];
		return Math.sqrt(dx * dx + dy * dy);
	}
}
