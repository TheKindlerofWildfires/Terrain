package object;

import java.util.ArrayList;

import maths.Vector4f;
import world.Skybox;
/**
 * The manager which deals with objects and object collections
 * @author TheKingInYellow
 *
 */
public class ObjectManager {
	private Skybox box;
	float c;
	public ArrayList<GameObject> objectList = new ArrayList<GameObject>();
	/**
	 * Initialises the manager
	 */
	public ObjectManager() {
		box = new Skybox("resources/models/skybox.obj", "resources/textures/skybox.png");
		right();
		box.rotate(90, 1, 0, 0);

	}
	/**
	 * Makes sure that the objs are rendered in the way they are seen in blender
	 */
	private void right() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			object.rotate(90, 1, 0, 0);
		}

	}
	/**
	 * Updates the object, should be used to call the update method on the game objects
	 */
	public void update() {
		
	}
	/**
	 * Renders the objects
	 * @param clipPlane
	 */
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
