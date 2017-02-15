package object;

import java.util.ArrayList;

import maths.Vector4f;
import world.Skybox;

public class ObjectManager {
	private Skybox box;
	float c;
	public ArrayList<GameObject> objectList = new ArrayList<GameObject>();

	public ObjectManager() {
		box = new Skybox("resources/models/uvsphere2.obj", "resources/textures/uvlayout.png");
		box.scale(5,5,5);
		right();
		box.rotate(-90, 1, 0, 0);

	}

	private void right() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			object.rotate(90, 1, 0, 0);
		}

	}

	public void update() {
	}

	public void render(Vector4f clipPlane) {
		for (int i = 0; i < objectList.size(); i++) {
			// GameObject object = objectList.get(i);
			// object.render(clipPlane);
		}
		// target.render(clipPlane);
		// ball.render(clipPlane);
		box.render(clipPlane);
	}
}
