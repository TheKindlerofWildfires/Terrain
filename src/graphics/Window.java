package graphics;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
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
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Random;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import entity.EntityManager;
import input.MouseInput;
import maths.Vector3f;
import maths.Vector4f;
import object.ObjectManager;
import world.Chunk;
import world.ChunkLoader;
import world.Water;
import world.World;

public class Window implements Runnable {
	public boolean running = true;

	static Long window;

	private GLFWKeyCallback keyCallback;
	public static GLFWCursorPosCallback cursorCallback;

	public static GraphicsManager graphicsManager;
	public static ObjectManager objectManager;
	public static EntityManager entityManager;

	public static World world;
	public static Water water;
	public static WaterFBO waterFBO;

	public static ChunkLoader chunkLoader;

	public static double deltaX, deltaY;

	public static Random worldRandom = new Random();
	public static Random mathRandom = new Random();

	private static Vector4f reflectionClipPlane;
	private static Vector4f refractionClipPlane;
	private static Vector4f renderClipPlane;

	public static void main(String args[]) {
		Window game = new Window();
		chunkLoader = new ChunkLoader();
		game.run();
	}

	/**
	 * Code called at the start of the gameloop
	 */
	public void init() {
		randomize();
		// Initialize glfw
		if (!glfwInit()) {
			System.err.println("GLFW init fail");
		}
		// make window resizable
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		// set opengl to version 3.2, core profile, forward compatable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		// create window
		window = glfwCreateWindow(1920, 1080, "WaterGame", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window");
		}
		// enable keyboard and mouse
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetKeyCallback(window, keyCallback = new input.KeyboardInput());
		glfwSetCursorPosCallback(window, cursorCallback = (GLFWCursorPosCallback) new input.MouseInput());
		// set window pos
		glfwSetWindowPos(window, 0, 20);
		// display window
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		// init gl
		GL.createCapabilities();
		// background colour
		glClearColor(75 / 255f, 10 / 255f, 130 / 255f, 1.0f);
		// enable depth testing and face culling
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CLIP_DISTANCE0);
		// Hm this looks wrong
		// glEnable(GL_CULL_FACE);

		// Create GraphicsManager and World
		graphicsManager = new GraphicsManager();

		world = new World();
		objectManager = new ObjectManager();
		entityManager = new EntityManager();

		water = new Water("src/models/plane.obj");
		waterFBO = new WaterFBO();

		chunkLoader.setPriority(Thread.MIN_PRIORITY);
		chunkLoader.start();

		reflectionClipPlane = new Vector4f(0, 0, 1, -Chunk.WATERLEVEL);
		refractionClipPlane = new Vector4f(0, 0, -1, Chunk.WATERLEVEL);
		renderClipPlane = new Vector4f(0, 0, 1, 100000);
	}

	/**
	 * Sets the seeds for everything
	 */
	private void randomize() {
		// setting seeds
		worldRandom.setSeed(mathRandom.nextLong());
		worldRandom.setSeed(120);
		mathRandom.setSeed(worldRandom.nextLong());
		World.perlinSeed = mathRandom.nextInt();

	}

	float speed = .001f;
	Vector3f vel = new Vector3f();

	/**
	 * The start of the update call
	 */
	public void update() {
		glfwPollEvents();
		graphicsManager.update();

		world.update();
		objectManager.update();
		entityManager.update();

	}

	/**
	 * The start of the render call
	 */
	public void render() {
		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//move camera to appropriate location and render reflection texture
		float camDist = GraphicsManager.camera.pos.z - Chunk.WATERLEVEL;
		float targetDist = GraphicsManager.camera.getTarget().z - Chunk.WATERLEVEL;
		GraphicsManager.camera.moveCamera(new Vector3f(0, 0, -camDist * 2));
		GraphicsManager.camera.moveTarget(new Vector3f(0, 0, -targetDist * 2));
		waterFBO.bindReflectionFrameBuffer();
		world.render(reflectionClipPlane);
		objectManager.render();

		//move camera back and render refraction texture
		GraphicsManager.camera.moveCamera(new Vector3f(0, 0, camDist * 2));
		GraphicsManager.camera.moveTarget(new Vector3f(0, 0, targetDist * 2));
		waterFBO.bindRefractionFrameBuffer();
		world.render(refractionClipPlane);
		objectManager.render();

		waterFBO.unbindCurrentFrameBuffer();
		world.render(renderClipPlane);
		objectManager.render();
		water.render();
	}

	/**
	 * The game loop
	 */
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
			deltaX = MouseInput.pos()[0] - 1920 / 2;
			deltaY = MouseInput.pos()[1] - 1080 / 2;
			glfwSetCursorPos(window, 1920 / 2, 1080 / 2);
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
				System.out.println(updates + " UPS, " + frames + " FPS");
				frames = 0;
				updates = 0;
			}

			while (chunkLoader.loadedChunks.size() > 0) {
				world.addChunk(chunkLoader.loadedChunks.poll());
			}

			if (glfwWindowShouldClose(window)) {
				running = false;
				chunkLoader.running = false;
			}
		}
		glfwDestroyWindow(window);
	}
}
