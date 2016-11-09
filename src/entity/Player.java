package entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import graphics.Camera;
import graphics.GraphicsManager;
import input.KeyboardInput;
import maths.Vector3f;

public class Player {
	Vector3f target = new Vector3f();
	Vector3f pos = new Vector3f();
	Float speed = Camera.speed/10;
	Vector3f upward = new Vector3f(0,0,1);
	Vector3f force;
	public void update(){
		this.target = GraphicsManager.camera.getTarget();
		this.pos = GraphicsManager.camera.getPos();
		force = new Vector3f();
		if (KeyboardInput.isKeyDown(GLFW_KEY_LEFT)) {
			movePlayer("RIGHT");//dont ask
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_RIGHT)) {
			movePlayer("LEFT");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_UP)) {
			movePlayer("FORWARD");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_DOWN)) {
			movePlayer("BACK");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_Q)) {
			movePlayer("UP");
		}
		if (KeyboardInput.isKeyDown(GLFW_KEY_E)) {
			movePlayer("DOWN");
		}
		//force = force.scale(0.9f);
			//ObjectManager.ball.setForce(force);
	}
	public void movePlayer(String dir){

		float vx = pos.x - target.x;
		float vy = pos.y - target.y;
		vx *= speed;
		vy *= speed;
		
		switch (dir) {
		case "UP":
			force = upward;
			break;
		case "DOWN":
			force = upward.negate();
			break;
		case "FORWARD":
			force = new Vector3f(-vx, -vy, 0);// backward.negate();
			break;
		case "BACK":
			force = new Vector3f(vx, vy, 0);// backward;
			break;
		case "LEFT":
			force = new Vector3f(-vy, vx, 0);// left;
			break;
		case "RIGHT":
			force = new Vector3f(vy, -vx, 0);// left.negate();
			break;
		default:
			System.err.println("wtf");
		}
		
		force = force.normalize().scale(speed);
	}
}
