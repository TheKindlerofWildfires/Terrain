package entity;

import graphics.Camera;
import graphics.GraphicsManager;
import maths.Vector3f;
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
		kindler = new Slasher("resources/models/box.obj", new Vector3f(0,0,6)); //Amazing!
	}

	public void render(Vector4f clipPlane) {
		kindler.render(clipPlane);
	}

	public void update(Long window) {
		kindler.update();
		player.update();
	}
}