package object;

import java.util.ArrayList;

import maths.Vector4f;
import world.Skybox;

public class ObjectManager {
	private Skybox box;
	float c;
	public ArrayList<GameObject> objectList = new ArrayList<GameObject>();

	public ObjectManager() {
		box = new Skybox("resources/models/uvsphere.obj", "resources/textures/uvlayout.png");
		right();
		box.rotate(90, 1, 0, 0);

	}

	private void right() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject object = objectList.get(i);
			object.rotate(90, 1, 0, 0);
		}

	}

	public void update() {
		/*
		 * //target.force = new Vector3f((float)(-0.1*Math.sin(c)),0,0);
		 * //target.force = target.position.subtract(new Vector3f(0, 5,
		 * 10)).scale(-0.1f); for (int i = 0; i < objectList.size(); i++) {
		 * GameObject object = objectList.get(i);
		 * object.translate(object.velocity);
		 * //object.placeAt(object.position.x, object.position.y,
		 * object.position.z); object.physic(); }
		 * maths.BoundingBox.collide(ball, target, ball.velocity,
		 * target.velocity); float cZ = (float)
		 * Math.abs(World.noise.getValue(ball.position.x, ball.position.y, 0.1))
		 * * Chunk.SIZE / 2 + .5f; float diff = ball.position.z - cZ;
		 * //System.out.println(diff); ball.velocity.z = -diff * .7f;
		 */
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
