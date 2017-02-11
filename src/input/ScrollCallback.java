package input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * @author TheKingInYellow
 */
public class ScrollCallback extends GLFWScrollCallback {

	public static double yoffset;

	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		ScrollCallback.yoffset = yoffset;
		
	}
	public static double getyoffset(){
		return yoffset;
	}



}
