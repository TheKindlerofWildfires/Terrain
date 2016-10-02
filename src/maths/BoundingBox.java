package maths;

import object.Object;

public class BoundingBox {

	public Vector3f centre;
	public float x, y, z;

	/**
	 * @param centre centre of box
	 * @param x dist from centre to side along x
	 * @param y dist from centre to side along y
	 * @param z dist from centre to side along z
	 */

	public BoundingBox(Vector3f centre, float x, float y, float z) {
		this.centre = centre;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param ob0 first object
	 * @param ob1 second object
	 * @param vOb0 velocity object 1
	 * @param vOb1 velocity object 2
	 */

	public static void collide(Object ob0, Object ob1, Vector3f vOb0, Vector3f vOb1) {
		BoundingBox b0 = ob0.boundingBox;
		BoundingBox b1 = ob1.boundingBox;
		boolean xIntersect = Math.abs(b0.centre.x - b1.centre.x) < b0.x + b1.x;
		boolean yIntersect = Math.abs(b0.centre.y - b1.centre.y) < b0.y + b1.y;
		boolean zIntersect = Math.abs(b0.centre.z - b1.centre.z) < b0.z + b1.z;
		if (xIntersect && yIntersect && zIntersect) {
			boolean colliding = true;
			float x = .5f;
			while (colliding) {
				ob0.translate(vOb0.negate().scale(x));
				ob1.translate(vOb0.negate().scale(x));
				b0 = ob0.boundingBox;
				b1 = ob1.boundingBox;
				boolean xI = Math.abs(b0.centre.x - b1.centre.x) < b0.x + b1.x;
				boolean yI = Math.abs(b0.centre.y - b1.centre.y) < b0.y + b1.y;
				boolean zI = Math.abs(b0.centre.z - b1.centre.z) < b0.z + b1.z;
				colliding = xI && yI && zI;
			}
			//ob0+x*-vOb0 is at the boundary of ob1+x*vOb1 
		}
	}
}
