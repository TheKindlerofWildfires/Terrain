package graphics;

import maths.Vector3f;

public class Material {
	public Vector3f colour;
	public int useColour;
	public float reflectance;

	public Material() {
		colour = new Vector3f(1, 1, 1);
		useColour = 0;
		reflectance = 0;
	}

	public Material(Material material) {
		colour = material.colour;
		useColour = material.useColour;
		reflectance = material.reflectance;
	}
};