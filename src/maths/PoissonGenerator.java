package maths;

import java.util.ArrayList;
import java.util.Random;

public class PoissonGenerator {

	public Random rand = new Random();

	public ArrayList<int[]> points = new ArrayList<int[]>();
	public double minDist = 10;
	public int POINTS_PER_ITER = 50;
	public int width = 640;
	public int height = 480;
	public int remainingPoints = 100;

	public static void main(String[] args) {
		PoissonGenerator gen = new PoissonGenerator();
		gen.generate();
		for(int[] pt : gen.points) {
			System.out.print(pt[0]);
			System.out.print(", ");
			System.out.println(pt[1]);
		}
	}

	public void iterate() {
		ArrayList<int[]> pts = new ArrayList<int[]>();
		for (int i = 0; i < POINTS_PER_ITER; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			pts.add(new int[] { x, y });
		}

		int bestPoint = -1;
		double minDist = Double.MAX_VALUE;
		for (int i = 0; i < pts.size(); i++) {
			for (int[] point : points) {
				if (dist(pts.get(i), point) < minDist) {
					minDist = dist(pts.get(i), point);
					bestPoint = i;
				}
			}
		}
		points.add(pts.get(bestPoint));
	}
	
	public void generate() {
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		points.add(new int[] {x, y});
		while(remainingPoints > 0) {
			iterate();
			remainingPoints--;
		}
	}

	public double dist(int[] p1, int[] p2) {
		int dx = p2[0] - p1[0];
		int dy = p2[1] - p1[1];
		return Math.sqrt(dx * dx + dy * dy);
	}
}
