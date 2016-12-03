package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

<<<<<<< HEAD
import graphics.GraphicsManager;
=======
>>>>>>> refs/remotes/origin/BiomeBack
import input.KeyboardInput;
import maths.Vector4f;

public class EntityManager {
<<<<<<< HEAD
	Player player;

	public EntityManager() {
		player = new Player(GraphicsManager.camera);
	}

	public void render(Vector4f clipPlane) {
	//	player.render(clipPlane);
=======
	Player player = new Player();

	public void render(Vector4f clipPlane) {
		player.render(clipPlane);
>>>>>>> refs/remotes/origin/BiomeBack
	}

	public void update() {
		if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT)) {
			player.movePlayer("LEFT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_RIGHT)) {
			player.movePlayer("RIGHT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_UP)) {
			player.movePlayer("FORWARD");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_DOWN)) {
			player.movePlayer("BACK");
		}
		player.update();
	}
}