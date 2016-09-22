package maths;

public class Mtest {
	/*
	 * for all points
	 * if the point in questions x> then all the other points with same y+- some or<all 
	 * then extend in the x direction
	 * else if the point in questions y repeat process
	 * 
	 * 
	 * The extending code: 
	 * create a x+, x-, y+, y- list based on the points we have chosen
	 * for all in list create new point that has same list(x,y) cord as given point, but has the maximum/min cross cord(based on list
	 * add all triangles that are composed of 2 adj list points and 1 new point with same maxC
	 * remove 1 of the two triangles that will appear with same 2 adj points
	 * add all triangles that are composed of 2 new points and one side point
	 * remove 1 of the two triangles that will appear with the same 2 new points 
	 */
}
