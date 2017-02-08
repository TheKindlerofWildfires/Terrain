package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import graphics.Camera;
import graphics.GraphicsManager;
import graphics.Window;
import input.KeyboardInput;
import input.MouseButtonCallback;
import input.ScrollCallback;
import maths.Vector4f;
import player.Player;

/**
 * @author TheKingInYellow
 * 
 *         This class contains all entities and entity collectors
 */
public class EntityManager {
	public Player player;

	// public Wanderer m;
	Camera camera = GraphicsManager.camera;
	int c = 0;

	public EntityManager() {

		player = new Player(camera);
		// m = new Wanderer("resources/models/box.obj");
	}

	/**
	 * Renders entities
	 * 
	 * @param clipPlane
	 */
	public void render(Vector4f clipPlane) {
		// m.render(clipPlane);
		// player.render(clipPlane);

	}

	/**
	 * Updates entities
	 * 
	 * @param window
	 */
	public void update(Long window) {
		// m.update();

		if (KeyboardInput.isKeyDown(GLFW_KEY_A)) {
			player.movePlayer("LEFT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_D)) {
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
		int[] m = MouseButtonCallback.getMouseButton();
		if (m[1] == GLFW_PRESS) {
			if (m[0] == GLFW_MOUSE_BUTTON_LEFT) {
				player.activeItem();
			}
		}
		double s = ScrollCallback.getyoffset();
		player.scroll(s);
		if (KeyboardInput.isKeyDown(GLFW_KEY_1)) {
			player.inventory.setActive(1);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_2)) {
			player.inventory.setActive(2);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_3)) {
			player.inventory.setActive(3);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_4)) {
			player.inventory.setActive(4);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_5)) {
			player.inventory.setActive(5);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_6)) {
			player.inventory.setActive(6);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_7)) {
			player.inventory.setActive(7);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_8)) {
			player.inventory.setActive(8);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_9)) {
			player.inventory.setActive(9);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_0)) {
			player.inventory.setActive(0);
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_P)) {
			Window.reload(c++ % 3);
		}
		// DO NOT DELETE THIS CODE
		/*
		 * int state = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT); if
		 * (state == GLFW_PRESS){ player.activeItem(); }
		 */
	}
}