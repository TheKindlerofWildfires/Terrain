package graphics;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Random;

import org.lwjgl.opengl.GL;

import maths.Vector3f;
import world.World;

public class Window implements Runnable {
	private Thread thread;
	public boolean running = true;

	private Long window;

	private GraphicsManager graphicsManager;
	private World world;
	public static Random worldRandom = new Random();
	public static Random mathRandom = new Random();
	
	public static void main(String args[]) {
		Window game = new Window();
		game.run();
	}

	public void start() {
		running = true;
		thread = new Thread(this, "SpaceGame");
		thread.start();
	}

	public void init() {
		randomize();
		//Initialize glfw
		if (!glfwInit()) {
			System.err.println("GLFW init fail");
		}
		//make window resizable
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		//set opengl to version 3.2, core profile, forward compatable 
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		//create window
		window = glfwCreateWindow(1920, 1080, "SpaceGame", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window");
		}

		//set window pos
		glfwSetWindowPos(window, 0, 20);

		//display window
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);

		//init gl
		GL.createCapabilities();

		//background colour
		glClearColor(0f, 0f, 0f, 1.0f);

		//enable depth testing and face culling
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_CULL_FACE);

		//Create GraphicsManager and World
		graphicsManager = new GraphicsManager();
		world = new World();
	}

	private void randomize() {
		worldRandom.setSeed(1);
		mathRandom.setSeed(1);
		World.perlinSeed = 120;
		
	}

	public void update() {
		glfwPollEvents();
	}

	public void render() {
		glfwSwapBuffers(window);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//		ShaderManager.landShader.start();
		//		glBindVertexArray(VAO.getVaoID());
		//		glDrawArrays(GL_TRIANGLES,0,3);
		//		ShaderManager.landShader.stop();
		//		
		world.render();

		//graphicsManager.moveCamera(new Vector3f(.01f,0,0));

	}

	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			//	System.out.println(updates + " UPS, " + frames + " FPS");
				frames = 0;
				updates = 0;
			}

			if (glfwWindowShouldClose(window)) {
				running = false;
			}
		}
		glfwDestroyWindow(window);
	}
}
