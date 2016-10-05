package graphics;

import maths.Vector3f;

public class Material {
	public Vector3f colour;
	public int useColour;
	public float reflectance;
	/**
	 * I am 99% sure my error is coming from here
	 */
	public Material() {
		colour = new Vector3f(1, 1, 1);
		useColour = 0;
		reflectance = 0;
	}
};