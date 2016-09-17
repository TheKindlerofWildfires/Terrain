package maths;

import java.util.ArrayList;
import java.util.Random;

public class PoissonGenerator {

	public Random rand = new Random();

	public ArrayList<int[]> points = new ArrayList<int[]>();
	public double MIN_DISTANCE = 0;//adjusting this has weird effects, 
	//0 makes really perfect shapes, so i think we did this wrong
	//small and many small pockets 
	//medium and a several medium pockets 
	//large and few--> 1 large pocket 
	public int POINTS_PER_ITER = 40;//smaller number less clustering
	//larger and larger clustering
	public int width = 640;
	public int height = 480;
	public int remainingPoints = 1000;

	public PoissonGenerator() {
		/*System.out.println("CALLED MAIN!");
		PoissonGenerator gen = new PoissonGenerator();
		gen.generate();
		for(int[] pt : gen.points) {
			System.out.print(pt[0]);
			System.out.print(", ");
			System.out.println(pt[1]);
		}*/
	}

	public boolean iterate() {
		//System.out.println("CALLED ITERATE!");
		ArrayList<int[]> pts = new ArrayList<int[]>();
		for (int i = 0; i < POINTS_PER_ITER; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			pts.add(new int[] { x, y });
		}

		int bestPoint = -1;//null case
		double maxDist = 0;//largest dist recorded
		for (int i = 0; i < pts.size(); i++) {//for all the new points
			double minDist = Double.MAX_VALUE; //really large number for distance to nearest point
			for (int[] point : points) { //for the given point in all the other points
				double dist = dist(pts.get(i), point);//distance between the points
				if (dist < minDist && dist > MIN_DISTANCE) {//100% sure this line is broke
//if the distance between the points is less than huge number and greater then the min distance
					minDist = dist;//adjust large number to new smaller number 
					//sorts for closest other point
				}
			}
			if (minDist > maxDist) {//if now small number > highest number 
				maxDist = minDist;//update highest number
				bestPoint = i;//this is now the biggest point
			}
		}
		if (bestPoint == -1) {//if it all went bad try again
			return false;//exit
		}
		points.add(pts.get(bestPoint));
		return true;
	}
	
	public void generate() {
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		points.add(new int[] {x, y});
		while(remainingPoints > 0) {
			if (iterate()) {
				remainingPoints--;
			}
		}
	}

	public double dist(int[] p1, int[] p2) {
		int dx = p2[0] - p1[0];
		int dy = p2[1] - p1[1];
		return Math.sqrt(dx * dx + dy * dy);
	}
}
