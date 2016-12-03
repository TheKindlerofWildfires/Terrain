package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import graphics.GraphicsManager;
import input.KeyboardInput;
import maths.Vector4f;

public class EntityManager {
	Player player;

	public EntityManager() {
		player = new Player(GraphicsManager.camera);
	}

	public void render(Vector4f clipPlane) {
	//	player.render(clipPlane);
	}

	public void update() {
		if (KeyboardInput.isKeyDown(GLFW_KEY_D)) {
			player.movePlayer("LEFT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_A)) {
			player.movePlayer("RIGHT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_W)) {
			player.movePlayer("FORWARD");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_S)) {
			player.movePlayer("BACK");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_SPACE)) {
			player.movePlayer("UP");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			player.movePlayer("DOWN");
		}
		player.update();
	}
}