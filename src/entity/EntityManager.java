package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import graphics.GraphicsManager;
import input.KeyboardInput;
import maths.Vector4f;

public class EntityManager {
	Player player;

	public EntityManager() {
		player = new Player();
		//player.slaveCamera(GraphicsManager.camera);
	}

	public void render(Vector4f clipPlane) {
		//	player.render(clipPlane);
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