package entity;

import graphics.Camera;
import graphics.GraphicsManager;
import maths.Vector4f;
import player.Player;

/**
 * @author TheKingInYellow && RadientRoss
 */
public class EntityManager {
	public Player player;

	public Monster kindler; //Funny!
	Camera camera = GraphicsManager.camera;
	

	public EntityManager() {
		player = new Player(camera);
		kindler = new Monster("basic"); //Amazing!
	}

	public void render(Vector4f clipPlane) {
		kindler.render(clipPlane);
	}

	public void update(Long window) {
		kindler.update();
		player.update();
	}
}