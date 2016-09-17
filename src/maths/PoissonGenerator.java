package maths;

import graphics.Window;

import java.util.ArrayList;

public class PoissonGenerator {

	public ArrayList<int[]> points = new ArrayList<int[]>();
	public int POINTS_PER_ITER = 100;//smaller number less clustering
	//larger and larger clustering

	public int width = 640;
	public int height = 480;
	public int remainingPoints = 100;

	public PoissonGenerator() {
		/*System.out.println("CALLED MAIN!");
		=======
		private boolean calculated = false;
		
		public static void main(String[] args) {
		>>>>>>> Stashed changes
		PoissonGenerator gen = new PoissonGenerator();
		gen.generate();
		for (int[] pt : gen.points) {
			System.out.print(pt[0]);
			System.out.print(", ");
			System.out.println(pt[1]);
		}*/
	}

	public void iterate() {
		//System.out.println("CALLED ITERATE!");
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

	public void generate() {
		int x = Window.mathRandom.nextInt(width);
		int y = Window.mathRandom.nextInt(height);
		points.add(new int[] { x,y });
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
