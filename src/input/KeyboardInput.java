package input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * @author TheKingInYellow
 */
public class KeyboardInput extends GLFWKeyCallback {

	public static boolean[] keys = new boolean[65535];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
}
