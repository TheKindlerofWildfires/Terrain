package input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseInput extends GLFWCursorPosCallback{
	static double xpos;
	static double ypos;
	@Override
	public void invoke(long window, double xpos, double ypos){
		MouseInput.xpos = xpos;
		MouseInput.ypos = ypos;
		//print pos to console
		//System.out.println(xpos + "," +ypos);
	}
	public static double[] pos(){
		double[] pos = {xpos, ypos};
		return pos;
	}
}
