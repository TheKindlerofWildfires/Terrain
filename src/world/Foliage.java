package world;

import java.util.Random;

import graphics.Window;

import maths.Vector3f;
import object.GameObject;

public class Foliage {
	private static Random rng = Window.worldRandom;

	private Foliage() {
	}

	public static void makeTree(Vector3f pos) {
		GameObject tree = new GameObject("resources/models/tree.obj", "resources/textures/wood.png",false);
		tree.placeAt(pos.x, pos.y, pos.z/Chunk.SIZE);
		tree.rotate(90, 1, 0, 0);
		tree.scale((float) (.2 + rng.nextDouble() / 10), (float) (.2 + rng.nextDouble() / 10),
				(float) (.2 + rng.nextDouble() / 10));
		
	}
}
