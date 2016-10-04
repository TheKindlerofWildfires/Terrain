package object;

import java.util.ArrayList;

import maths.Vector3f;
import world.Skybox;

public class ObjectManager {
	public Object target;
	public Object ball;
	//private Object test;
	private Skybox box;
	float c;
	public ArrayList<Object> objectList = new ArrayList<Object>();

	public ObjectManager() {
		/*
		 * create a list of all objects
		 */

		target = new Object("src/models/box.obj", "src/textures/wood.png");
		ball = new Object("src/models/box.obj", "src/textures/wood.png");

		ball.scale(.25f, .25f, .25f);
		ball.translate(5, 2, 10);
		//System.out.println(target.position);
		target.translate(0, 2, 10);
		//System.out.println(target.position);
		objectList.add(target);
		objectList.add(ball);
		//ball.rotate(30, 1,0,0);
		box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		box.rotate(90, 1, 0, 0);
		c = 0;
	}

	public void update() {
		//target.force = new Vector3f((float)(-0.1*Math.sin(c)),0,0);
		target.force = target.position.subtract(new Vector3f(0, 5, 10)).scale(-0.1f);
		for (int i = 0; i < objectList.size(); i++) {
			Object object = objectList.get(i);
			object.translate(object.velocity);
			//object.placeAt(object.position.x, object.position.y, object.position.z);
			object.physic();
		}
		maths.BoundingBox.collide(ball, target, ball.velocity, target.velocity);

		c += Math.PI / 100;
	}

	public void render() {
		for (int i = 0; i < objectList.size(); i++) {
			Object object = objectList.get(i);
			object.render();
		}
		box.render();
	}
}
