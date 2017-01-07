package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import graphics.Camera;
import graphics.GraphicsManager;
import input.KeyboardInput;
import input.MouseButtonCallback;
import input.ScrollCallback;
import maths.Vector4f;
import player.Player;
/**
 * @author TheKingInYellow
 */
public class EntityManager {
	public Player player;

	//public Wanderer m;
	Camera camera = GraphicsManager.camera;

	public EntityManager() {

		player = new Player(camera);
		//	m = new Wanderer("resources/models/box.obj");
	}

	public void render(Vector4f clipPlane) {
		//m.render(clipPlane);
		// player.render(clipPlane);

	}

	public void update(Long window) {
		//	m.update();
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
		if(m[1] ==  GLFW_PRESS){
			if(m[0] == GLFW_MOUSE_BUTTON_LEFT){
				player.activeItem();
			}
		}
		double s = ScrollCallback.getyoffset();
		player.scroll(s);
		/*
		int state = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT);
		if (state == GLFW_PRESS){
			player.activeItem();
		}*/
	}
}