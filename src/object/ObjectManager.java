package object;

import graphics.FrameBufferObject;

import java.util.ArrayList;

import world.Chunk;
import world.Skybox;
import world.Water;
import world.World;

public class ObjectManager {
	public GameObject target;
	public static GameObject ball;
	//private Object test;
	private Skybox box;
	public static Water water;
	FrameBufferObject WaterFBO;
	float c;
	public ArrayList<GameObject> objectList = new ArrayList<GameObject>();

	public ObjectManager() {
		/*
		 * create a list of all objects
		 */
		target = new GameObject("src/models/box.obj", "src/textures/wood.png");
		ball = new GameObject("src/models/torus.obj", "src/textures/wood.png");
		water = new Water("src/models/plane.obj");
		ball.scale(.25f, .25f, .25f);
		ball.translate(5, 2, 10);
		target.translate(2, 2, 10);
		objectList.add(target);
		objectList.add(ball);
		box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		box.rotate(90, 1, 0, 0);
		WaterFBO = new FrameBufferObject();
		c = 0;
	}

	public void update() {
		
		
		
		
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			object.translate(object.velocity);
			object.physic();
		}
		maths.BoundingBox.collide(ball, target, ball.velocity, target.velocity);
		float cZ = (float) Math.abs(World.noise.getValue(ball.position.x, ball.position.y, 0.1)) * Chunk.SIZE / 2 + .5f;
		float diff = ball.position.z - cZ;
		//System.out.println(diff);
		ball.velocity.z = -diff * .7f;
	}

	public void render() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			//object.render();
		}
	//	box.render();
		WaterFBO.bindReflectionFrameBuffer();
		water.render();
		WaterFBO.unbindCurrentFrameBuffer();
		
	}

	public void cleanUp() {
		WaterFBO.cleanUp();
	}
}
