package object;

<<<<<<< HEAD
import java.util.ArrayList;

import maths.BoundingBox;
import maths.Vector3f;
=======
import world.Skybox;
>>>>>>> colisions

public class ObjectManager {
	public Object target;
	public Object ball;
	//private Object test;
	private Skybox box;
	float c;
	public ArrayList<Object> objectList = new ArrayList<Object>();
	public ObjectManager() {
<<<<<<< HEAD
		/*
		 * create a list of all objects
		 */
		
		target = new Object("src/models/box.obj", "src/textures/wood.png",
				new BoundingBox(new Vector3f(0, 0, 0), 1, 1, 1));
		ball = new Object("src/models/box.obj", "src/textures/wood.png",
				new BoundingBox(new Vector3f(0, 0, 0), 1f, 1f, 1f));


		ball.scale(.25f,.25f,.25f);
		ball.translate(5,2,10);
		//System.out.println(target.position);
		target.translate(0,2,10);
		//System.out.println(target.position);
		objectList.add(target);
		objectList.add(ball);
		//ball.rotate(30, 1,0,0);
		//	box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		c = 0;
	}
	public void update(){
		//target.force = new Vector3f((float)(-0.1*Math.sin(c)),0,0);
		target.force = target.position.subtract(new Vector3f(0,5,10)).scale(-0.1f);
		System.out.println(target.position);
		for(int i = 0; i<objectList.size();i++){
			Object object = objectList.get(i);
			object.translate(object.velocity);
			//object.placeAt(object.position.x, object.position.y, object.position.z);
			object.physic();
		}
		maths.BoundingBox.collide(ball, target, ball.velocity, target.velocity);
	
		c+= Math.PI/100;
=======
		target = new Object("src/models/box.obj", "src/textures/wood.png");
		//	test = new Object("src/models/box.obj", "src/textures/wood.png",
		//			new BoundingBox(new Vector3f(0, 0, 0), 1, 1, 1));
		ball = new Object("src/models/char.obj", "none");
		//	ball.model.scale(.5f, .5f, .5f);
		//	ball.model.translate(5, 0, 0);

		ball.scale(.5f, .5f, .5f);
		ball.translate(5, 0, 0);
		ball.rotate(-90, 0, 1, 0);
		//box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		c = 0;
	}

	public void test() {
		//test.model = test.model.multiply(Matrix4f.rotate(1, 0, 1, 10));
		//	test.model = test.model.multiply(Matrix4f.translate(0,(float)(0.2*Math.sin(c)),0));
		//	c+= Math.PI/100;
>>>>>>> colisions
	}
	public void render() {
<<<<<<< HEAD
		for(int i = 0; i<objectList.size();i++){
			Object object = objectList.get(i);
			object.render();
		}
=======
		target.render();
		ball.render();
		//		test.render();
		//box.render();
>>>>>>> colisions
	}
}
