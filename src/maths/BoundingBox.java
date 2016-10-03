package maths;

import object.Object;

public class BoundingBox {

	public Vector3f centre;
	public float x, y, z;

	/**
	 * The maximum possible deviation from snug fit
	 */
	public static final float margin = .05f;

	/**
	 * aabb bounding box
	 * 
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
	 * rotates kinda
	 *
	 * @param x the number of 90 degree rotations to be performed in the x axis
	 * @param y the number of 90 degree rotations to be performed in the y axis
	 * @param z the number of 90 degree rotations to be performed in the z axis
	 */
	public void rotate(int x, int y, int z) {
		System.out.println("hello");
		if (x == 0 && y == 0 && z == 0) { //no rotation
			return;
		} else if (x == 0 && y == 0 && z == 1) { //rotate once in z
			float oldX = this.x;
			float oldY = this.y;
			this.x = oldY;
			this.y = oldX;
		} else if (x == 0 && y == 1 && z == 0) { //rotate once in y
			float oldX = this.x;
			float oldZ = this.z;
			this.x = oldZ;
			this.z = oldX;
		} else if (x == 1 && y == 0 && z == 0) { //rotate once in x
			float oldZ = this.z;
			float oldY = this.y;
			this.z = oldY;
			this.y = oldZ;
		} else {
			for (int i = 0; i < z; i++) {//do the right number of z rotations
				this.rotate(0, 0, 1);
			}
			for (int i = 0; i < y; i++) {//do the right number of y rotations
				this.rotate(0, 1, 0);
			}
			for (int i = 0; i < x; i++) {//do the right number of x rotations
				this.rotate(1, 0, 0);
			}
		}
	}

	/**
	 * Determines whether two aabb bounding boxes are colliding
	 * 
	 * @param b0 first box
	 * @param b1 second box
	 * @return whether or not they are colliding
	 */
	private static boolean aabbCollides(BoundingBox b0, BoundingBox b1) {
		boolean xIntersect = Math.abs(b0.centre.x - b1.centre.x) < b0.x + b1.x;
		boolean yIntersect = Math.abs(b0.centre.y - b1.centre.y) < b0.y + b1.y;
		boolean zIntersect = Math.abs(b0.centre.z - b1.centre.z) < b0.z + b1.z;
		return xIntersect && yIntersect && zIntersect;
	}

	/** 
	 * If two objects are colliding, it moves them back until they are not
	 * 
	 * @param ob0 first object
	 * @param ob1 second object
	 * @param vOb0 velocity vector of first object
	 * @param vOb1 velocity vector of second object
	 */
	public static void collide(Object ob0, Object ob1, Vector3f vOb0, Vector3f vOb1) {
		BoundingBox b0 = ob0.boundingBox;
		BoundingBox b1 = ob1.boundingBox;

		if (aabbCollides(b0, b1)) {
			Vector3f v0;
			Vector3f v1;
			if (vOb0.length() != 0) {
				v0 = vOb0.negate().normalize().scale(margin);
			} else {
				v0 = new Vector3f(0, 0, 0);
			}
			if (vOb1.length() != 0) {
				v1 = vOb1.negate().normalize().scale(margin);
			} else {
				v1 = new Vector3f(0, 0, 0);
			}
			boolean colliding = true;
			while (colliding) {
				ob0.translate(v0);
				ob1.translate(v1);
				b0 = ob0.boundingBox;
				b1 = ob1.boundingBox;
				colliding = aabbCollides(b0, b1);
			}
<<<<<<< HEAD
		 Vector3f momentum = ob0.velocity.scale(ob0.mass).add(ob1.velocity.scale(ob1.mass));
		 Float kin = (float) (0.5*(ob0.velocity.length2()*ob0.mass+ob1.velocity.length2()*ob1.mass));
		 ob0.velocity = ob0.velocity.negate();
		 ob1.velocity = ob1.velocity.negate();
		 
			//ob0+x*-vOb0 is at the boundary of ob1+x*vOb1 
=======
>>>>>>> colisions
		}
	}
}
