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
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
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
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Properties;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import entity.EntityManager;
import input.MouseInput;
import maths.Vector3f;
import maths.Vector4f;
import object.ObjectManager;
import particles.Particle;
import particles.ParticleEmitter;
import world.Chunk;
import world.ChunkLoader;
import world.Water;
import world.World;

public class Window implements Runnable {
	private static int REFLECTION_WIDTH;
	private static int REFLECTION_HEIGHT;

	private static int REFRACTION_WIDTH;
	private static int REFRACTION_HEIGHT;

	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;

	private Vector3f CLEAR_COLOUR;

	public boolean running = true;

	private static Long window;
	private static int windowWidth;
	private static int windowHeight;

	@SuppressWarnings("unused")
	private GLFWKeyCallback keyCallback;
	public static GLFWCursorPosCallback cursorCallback;

	public static GraphicsManager graphicsManager;
	public static ObjectManager objectManager;
	public static EntityManager entityManager;

	public static World world;
	public static Water water;

	public static ChunkLoader chunkLoader;

	public static double deltaX, deltaY;

	public static Random worldRandom = new Random();
	public static Random mathRandom = new Random();

	private static Vector4f reflectionClipPlane;
	private static Vector4f refractionClipPlane;
	public static Vector4f renderClipPlane;

	public static FrameBufferObject refraction;
	public static FrameBufferObject reflection;

	private static ParticleEmitter particles;
	private static Particle baseParticle;

	public static void main(String args[]) {
		Window game = new Window();
		game.run();
	}

	private void loadProperties() {
		System.out.println("Loading properties from resources/properties/window.properties");

		Properties props = new Properties();
		try {
			FileReader reader = new FileReader("resources/properties/window.properties");
			props.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		REFLECTION_WIDTH = Integer.parseInt(props.getProperty("reflectionWidth"));
		REFLECTION_HEIGHT = Integer.parseInt(props.getProperty("reflectionHeight"));

		REFRACTION_WIDTH = Integer.parseInt(props.getProperty("refractionWidth"));
		REFRACTION_HEIGHT = Integer.parseInt(props.getProperty("refractionHeight"));

		WINDOW_WIDTH = Integer.parseInt(props.getProperty("windowWidth"));
		WINDOW_HEIGHT = Integer.parseInt(props.getProperty("windowHeight"));

		CLEAR_COLOUR = Vector3f.parseVector(props.getProperty("clearColour"));
	}

	/**
	 * Code called at the start of the gameloop
	 */
	public void init() {
		loadProperties();
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
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "WaterGame", NULL, NULL);
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
		glClearColor(CLEAR_COLOUR.x, CLEAR_COLOUR.y, CLEAR_COLOUR.z, 1.0f);
		// enable depth testing and face culling
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CLIP_DISTANCE0);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		glfwGetFramebufferSize(Window.window, width, height);
		windowWidth = width.get(0);
		windowHeight = height.get(0);
		// Create GraphicsManager and World
		world = new World();

		graphicsManager = new GraphicsManager();
		objectManager = new ObjectManager();
		entityManager = new EntityManager();

		water = new Water();

		chunkLoader = new ChunkLoader();
		chunkLoader.setPriority(Thread.MIN_PRIORITY);
		chunkLoader.start();

		reflectionClipPlane = new Vector4f(0, 0, 1, -Chunk.WATERLEVEL + 0.01f);
		refractionClipPlane = new Vector4f(0, 0, -1, Chunk.WATERLEVEL + 0.01f);
		renderClipPlane = new Vector4f(0, 0, -1, 10000);

		reflection = new FrameBufferObject(REFLECTION_WIDTH, REFLECTION_HEIGHT, false);
		refraction = new FrameBufferObject(REFRACTION_WIDTH, REFRACTION_HEIGHT, true);

		baseParticle = new Particle("resources/models/box.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		baseParticle.scale(.01f, .01f, .01f);
		particles = new ParticleEmitter(baseParticle, 500, 10);
		particles.activate();

		GraphicsManager.toggleFog();
	}

	/**
	 * Sets the seeds for everything
	 */
	private void randomize() {
		// setting seeds
		worldRandom.setSeed(mathRandom.nextLong());
		//worldRandom.setSeed(120);
		mathRandom.setSeed(worldRandom.nextLong());
		World.perlinSeed = mathRandom.nextInt();

	}

	/**
	 * The start of the update call
	 */
	public void update() {
		glfwPollEvents();
		graphicsManager.update();
		world.update();
		objectManager.update();
		entityManager.update();
		now = System.currentTimeMillis();
		if (particles.active) {
			particles.update(now - then);
		}
		then = now;
	}

	private long then = System.currentTimeMillis();
	private long now = System.currentTimeMillis();

	float x = 0;

	public void testUpdate() {
		glfwPollEvents();
		graphicsManager.update();
	}

	/**
	 * The start of the render call
	 */
	public void render() {
		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//waterFBO.bindReflectionFrameBuffer();
		//	world.render(reflectionClipPlane);
		//	objectManager.render();

		//	waterFBO.bindRefractionFrameBuffer();
		//	world.render(refractionClipPlane);
		//	objectManager.render();

		//move camera to appropriate location and render reflection texture
		float camDist = GraphicsManager.camera.pos.z - Chunk.WATERLEVEL;
		float targetDist = GraphicsManager.camera.getTarget().z - Chunk.WATERLEVEL;

		//	GraphicsManager.camera.flipCamera();
		GraphicsManager.camera.moveCamera(new Vector3f(0, 0, -camDist * 2));
		GraphicsManager.camera.moveTarget(new Vector3f(0, 0, -targetDist * 2));
		ShaderManager.setCamera(GraphicsManager.camera, GraphicsManager.dirLight);
		//bind reflection buffer and render to it
		reflection.activate();
		glClearColor(CLEAR_COLOUR.x, CLEAR_COLOUR.y, CLEAR_COLOUR.z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		world.renderLand(reflectionClipPlane);
		objectManager.render(reflectionClipPlane);
		entityManager.render(reflectionClipPlane);

		//	move camera back and render refraction texture
		//GraphicsManager.camera.flipCamera();
		GraphicsManager.camera.moveCamera(new Vector3f(0, 0, camDist * 2));
		GraphicsManager.camera.moveTarget(new Vector3f(0, 0, targetDist * 2));
		ShaderManager.setCamera(GraphicsManager.camera, GraphicsManager.dirLight);
		if (particles.active) {
			particles.render(renderClipPlane);
		}

		//bind refraction buffer and render to it
		refraction.activate();
		glClearColor(CLEAR_COLOUR.x, CLEAR_COLOUR.y, CLEAR_COLOUR.z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		world.renderLand(refractionClipPlane);
		objectManager.render(refractionClipPlane);
		entityManager.render(refractionClipPlane);
		if (particles.active) {
			particles.render(renderClipPlane);
		}

		//render to screen
		glBindFramebuffer(GL_FRAMEBUFFER, 0); // back to default
		glViewport(0, 0, windowWidth, windowHeight);
		glClearColor(CLEAR_COLOUR.x, CLEAR_COLOUR.y, CLEAR_COLOUR.z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		world.renderLand(renderClipPlane);
		objectManager.render(renderClipPlane);
		water.render(renderClipPlane); //do NOT attempt to render water anywhere other than to screen
		entityManager.render(renderClipPlane);
		if (particles.active) {
			particles.render(renderClipPlane);
		}
	}

	public void testRender() {
		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		objectManager.render(renderClipPlane);

	}

	/**
	 * The game loop
	 */
	@Override
	public void run() {
		init();
<<<<<<< HEAD
		GraphicsManager.toggleFog();
=======
		//	GraphicsManager.toggleFog();
		entityManager.player.slaveCamera(GraphicsManager.camera);
>>>>>>> i-have-a-branch-now
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			deltaX = MouseInput.pos()[0] - WINDOW_WIDTH / 2;
			deltaY = MouseInput.pos()[1] - WINDOW_HEIGHT / 2;
			glfwSetCursorPos(window, 1920 / 2, 1080 / 2);
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			//testRender();
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
				chunkLoader.interrupt();
			}
		}
		glfwDestroyWindow(window);
	}
}