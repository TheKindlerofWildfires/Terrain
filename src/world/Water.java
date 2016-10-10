package world;

import maths.Triangle;
import maths.Vector3f;
import noiseLibrary.module.source.Perlin;
import object.GameObject;

public class Water extends GameObject {

	public Water(String modelPath) {
		super(modelPath, "none");
		this.translate(0, 0, Chunk.WATERLEVEL);
	}
}
