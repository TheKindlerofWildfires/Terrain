package object;

import maths.Matrix4f;
import world.Skybox;

public class ObjectManager {
	private Object test;
	private Skybox box;
	float c;
	public ObjectManager(){
		test = new Object("src/models/torus.obj", "src/textures/wood.png");
		box = new Skybox("src/models/skybox.obj", "src/textures/skybox.png");
		c = 0;
	}
	public void test(){
		test.model = test.model.multiply(Matrix4f.rotate(1, 0, 1, 10));
		test.model = test.model.multiply(Matrix4f.translate(0,(float)(0.2*Math.sin(c)),0));
		c+= Math.PI/100;
	}
	public void render() {
		test.render();
		box.render();	
	}
}
