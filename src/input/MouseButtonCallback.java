package input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonCallback extends GLFWMouseButtonCallback {
	static int button;
	static int action;
	@Override
	public void invoke(long window, int button, int action, int mods) {
		MouseButtonCallback.button = button;
		MouseButtonCallback.action = action;
	}
	public static int[] getMouseButton(){
		int[] i = {button, action};
		return i;
	}
	

}
