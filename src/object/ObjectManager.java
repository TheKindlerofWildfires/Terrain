package object;

import maths.BoundingBox;
import maths.Vector3f;

public class ObjectManager {
	public Object target;
	public Object ball;
	//private Object test;
	//private Skybox box;
	float c;

	public ObjectManager() {
		target = new Object("src/models/box.obj", "src/textures/wood.png");
		//	test = new Object("src/models/box.obj", "src/textures/wood.png",
		//			new BoundingBox(new Vector3f(0, 0, 0), 1, 1, 1));
		ball = new Object("src/models/char.obj", "src/textures/wood.png");
		//	ball.model.scale(.5f, .5f, .5f);
		//	ball.model.translate(5, 0, 0);

		ball.scale(.5f, .5f, .5f);
		ball.translate(5, 0, 0);
		ball.rotate(-90, 0, 1, 0);
		//	box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		c = 0;
	}

	public void test() {
		//test.model = test.model.multiply(Matrix4f.rotate(1, 0, 1, 10));
		//	test.model = test.model.multiply(Matrix4f.translate(0,(float)(0.2*Math.sin(c)),0));
		//	c+= Math.PI/100;

	}

	public void render() {
		//target.render();
		//ball.render();
		//		test.render();
		//box.render();	
	}
}
