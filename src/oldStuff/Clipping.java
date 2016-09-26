package oldStuff;

import java.util.ArrayList;

import stuffnooneneeds.Direction;
import maths.Edge;
import maths.Triangle;
import maths.Vector3f;
import static maths.Utilities.mod;
/**
 * Up for deletion
 *
 */
public class Clipping {

	ArrayList<Triangle> badOnes = new ArrayList<Triangle>();
	ArrayList<Triangle> terrain = new ArrayList<Triangle>();

	//clips to from bound to -bound in x and y
	float bound = 1.7f;

	public Clipping(ArrayList<Triangle> input) {
		for (Triangle tri : input) {
			int numberOut = 0;
			for (int i = 0; i < 3; i++) {
				Vector3f point = tri.getPoint(i);
				if (point.x > bound || point.y > bound || point.x < -bound || point.y < -bound) {
					numberOut++;
				}
			}
			if (numberOut > 0) {
				badOnes.add(tri);
			} else {
				terrain.add(tri);
			}
		}

		for (Triangle tri : badOnes) {

			int numberOut = 0;
			for (int i = 0; i < 3; i++) {
				Vector3f point = tri.getPoint(i);
				if (point.x > bound || point.y > bound || point.x < -bound || point.y < -bound) {
					numberOut++;
				}
			}
			if (numberOut == 0) {
				assert false : "dont try to clip triangles that are in bounds";
				continue;
			} else if (numberOut == 1 || numberOut == 2) { //only one or two is outside, calculate clipping
				ArrayList<Vector3f> points = new ArrayList<Vector3f>();
				ArrayList<Vector3f> newPoints = new ArrayList<Vector3f>();
				newPoints.add(tri.getPoint(0));
				newPoints.add(tri.getPoint(1));
				newPoints.add(tri.getPoint(2));
				for (Direction dir : Direction.values()) {//for each clipEdge
					System.out.println(dir);
					points = (ArrayList<Vector3f>) newPoints.clone();
					newPoints.clear();
					System.out.println(points.size());

					for (int j = 0; j < points.size(); j++) {//for each point
						if (points.get(j).isInside(dir, bound)) {
							newPoints.add(points.get(j));
							System.out.println("valid point");
						} else {
							System.out.println("invalid point");
							if (points.get(Math.abs((j + 1) % points.size())).isInside(dir, bound)) {
								newPoints.add(Edge.intersect(new Edge(points.get(j), points.get(mod(j + 1, points.size()))), dir,bound));
							}
							if (points.get(Math.abs((j - 1) % points.size())).isInside(dir, bound)) {
								newPoints.add(Edge.intersect(new Edge(points.get(j), points.get(mod(j - 1, points.size()))), dir,bound));
							}
						}
					}
					System.out.println(newPoints.size());
					System.out.println("end " + dir);
				}
				System.out.println(points.size());
				points = (ArrayList<Vector3f>) newPoints.clone();
				System.out.println("points" + points.size());
				if(points.size()==3){
					terrain.add(new Triangle(points.get(0),points.get(1),points.get(2)));
				} else if(points.size()==4){
					terrain.add(new Triangle(points.get(0),points.get(1),points.get(2)));
					terrain.add(new Triangle(points.get(1),points.get(2),points.get(3)));
				} else{
					System.err.println("i dont think this is possible");
				}
			} else if (numberOut == 3) { //all three are out
				continue;
			} else {
				assert false : "triagles only get 3 pts";
			}
		}
		
	}
	
	public static void main(String[] args){
		System.out.println(-1%3);
		Vector3f p0 = new Vector3f(-3,.6f,0);
		Vector3f p1 = new Vector3f(.4f,.3f,0);
		Vector3f p2 = new Vector3f(.6f,.8f,0);
		Triangle tri = new Triangle(p0,p1,p2);
		ArrayList<Triangle> tris = new ArrayList<Triangle>();
		tris.add(tri);
		Clipping clip = new Clipping(tris);
		System.out.println(clip.terrain);
		System.out.println("hello world");
	}

}
