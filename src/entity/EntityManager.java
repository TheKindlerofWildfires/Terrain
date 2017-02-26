package entity;

import graphics.Camera;
import graphics.GraphicsManager;
import maths.Vector4f;
import player.Player;

/**
 * @author TheKingInYellow
 */
public class EntityManager {
	public Player player;

	// public Wanderer m;
	Camera camera = GraphicsManager.camera;
	

	public EntityManager() {
		player = new Player(camera);
		// m = new Wanderer("resources/models/box.obj");
	}

	public void render(Vector4f clipPlane) {
		// m.render(clipPlane);
	}

	public void update(Long window) {
		// m.update();
		player.update();
	}
}