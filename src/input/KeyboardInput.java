package input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * @author TheKingInYellow
 */
public class KeyboardInput extends GLFWKeyCallback {

	public static int[] keys = new int[65535];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action;// != GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		if(keys[keycode] == GLFW_REPEAT){
			return true;
		}
		return false;
	}
	public static boolean isKeyPressed(int keycode) {
		if(keys[keycode] == GLFW_PRESS){
			return true;
		}
		return false;
	}
	public static boolean isKeyReleased(int keycode) {
		if(keys[keycode] == GLFW_RELEASE){
			return true;
		}
		return false;
	}
}
