package maths;

import graphics.Window;

import java.util.ArrayList;
import java.util.Collections;

public class PoissonGenerator {

	public ArrayList<int[]> points = new ArrayList<int[]>();
	public int POINTS_PER_ITER = 10;
	public int width = 1000;
	public int height = 1000;
	public int remainingPoints = 250;
	public float density = (float)remainingPoints/(float)(height);

	int freq=0;
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	public ArrayList<Vector3f> xp = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> xn = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> yp = new ArrayList<Vector3f>();
	public ArrayList<Vector3f> yn = new ArrayList<Vector3f>();
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
		if(check(bestPoint)){
		points.add(bestPoint);
		}else{
			remainingPoints+=1;
		}
	}

	private boolean check(int[] bestPoint) {
		if(bestPoint[0]>Mirror.spec || bestPoint[1]>Mirror.spec||bestPoint[0]<1000-Mirror.spec||bestPoint[1]<1000-Mirror.spec){
			return false;
		}else{
			return true;
		}
		
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
		/*
		int x = Window.mathRandom.nextInt(width);
		int y = Window.mathRandom.nextInt(height);
		points.add(new int[] { x,y });
		*/
		Mirror m = new Mirror();
		points.addAll(m.fish());
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

	private void cleanup() {
		Vector3f nVec = new Vector3f(0,0,1);
		for(int i = 0; i<1000;i++){
			xp.add(nVec);
			yp.add(nVec);
			xn.add(nVec);
			yn.add(nVec);
		}
		
		for(int i =0; i<points.size();i++){
			int[] p = points.get(i);
			//System.out.println(xp.size());
			if (p[0]>xp.get(p[1]).y){
				xp.set(p[1],new Vector3f(p[0],p[1],0));
			}
			if (p[1]>yp.get(p[0]).x){
				yp.set(p[0],new Vector3f(p[0],p[1],0));
			}
			if (p[0]<xn.get(p[1]).y){
				xn.set(p[1],new Vector3f(p[0],p[1],0));
			}
			if (p[1]>yn.get(p[0]).x){
				yn.set(p[0],new Vector3f(p[0],p[1],0));
			}
		}
		xp.removeAll(Collections.singleton(nVec));
		yp.removeAll(Collections.singleton(nVec));
		xn.removeAll(Collections.singleton(nVec));
		yn.removeAll(Collections.singleton(nVec));
		//at the end of this we have 4 lists of vec3s each organized by opposite cord taking the maxiumum for that row
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
