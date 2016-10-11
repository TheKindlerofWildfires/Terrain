package world;

import graphics.ShaderManager;
import maths.Vector4f;
import object.GameObject;

public class Water extends GameObject {

	public Water(String modelPath) {
		super(modelPath, "none");
		translate(0, 0, Chunk.WATERLEVEL);
		rotate(90, 1, 0, 0);
		scale(10, 10, 10);
		shader = ShaderManager.waterShader;
		hasMaterial = false;
	}
}
