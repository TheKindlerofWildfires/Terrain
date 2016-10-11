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

	public void render() {
		super.render(new Vector4f(0, 0, -1, 100));
	}
}
