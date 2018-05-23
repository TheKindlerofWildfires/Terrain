package object;

import maths.Vector4f;
import world.Skybox;

import java.util.ArrayList;

public class ObjectManager {
	private Skybox box;
	float c;
	public ArrayList<GameObject> objectList = new ArrayList<GameObject>();

	public ObjectManager() {
		box = new Skybox("resources/models/uvsphere2.obj", "resources/textures/uvlayout.png");
		box.scale(7,7,7);
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
		if(objectList.size()!=0){
			for (int i = 0; i < objectList.size(); i++) {
				GameObject object = objectList.get(i);
				object.translate(object.velocity);
			}
			}
	}

	public void render(Vector4f clipPlane) {
		if(objectList.size()!=0){
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			object.render(clipPlane);
		}
		}
		// target.render(clipPlane);
		// ball.render(clipPlane);
		box.render(clipPlane);
	}
	public void add(GameObject object){
		GameObject o = object;
		objectList.add(o);
		
	}
}
