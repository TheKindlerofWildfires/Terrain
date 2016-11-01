package world;

import java.util.Random;

import graphics.Window;

import maths.Vector3f;
import object.GameObject;

public class Foliage {
	Random rng = Window.worldRandom;
	public Foliage(){	
	}

	public void generate(Vector3f pos) {
		
		GameObject tree = new GameObject("src/models/tree.obj", "src/textures/wood.png");
		System.out.println(tree.position);
		
		tree.placeAt(pos.x, pos.y, pos.z);
		tree.rotate(90, 1, 0, 0);
		tree.scale((float)(.2+rng.nextDouble()/10), (float)(.2+rng.nextDouble()/10), (float)(.2+rng.nextDouble()/10));
		//probably should be some kinda linked list thing
		graphics.Window.objectManager.objectList.add(tree);	
		
	}
}
