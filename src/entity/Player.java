package entity;

import graphics.Camera;
import graphics.Shader;
import graphics.ShaderManager;
import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;
import world.Biome;

/**
 * @author TheKingInYellow
 */
public class Player extends GameObject {
	private static final float CLIMABLE = 1.5f;
	private static final float SPEEDSCALER = 20;
	private Vector3f target;
	private float speed;
	private Vector3f upward;
	Camera camera;
	Vector3f displacement = new Vector3f(0, 0, 0);
	Vector3f[] destination = new Vector3f[4];

	public Player(Camera camera) {
		super("resources/models/box.obj", "none", true);
		this.speed = camera.getSpeed() * SPEEDSCALER;
		upward = new Vector3f(0, 0, speed);
		this.camera = camera;
		this.target = camera.getTarget();
		this.position = new Vector3f(1, 1, 10);
	}

	public void update() {
		camera.pos = position;
		
	}

	private void effect() {
		/*
		 	Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);*/
		int biome = (int)Biome.getValue(position, position, false)[4];
		switch (biome) {
		case Biome.RAINFOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 2);
			Shader.setUniform4f("waterColour", new Vector4f(0.2f, 0.3f, 0.4f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",0.1f);
			break;
		case Biome.SEASONALFOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 20);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.001f);
			break;
		case Biome.FOREST:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.SWAMP:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0f);
			Shader.setUniform1f("waveStrength", 0.0002f);
			Shader.setUniform1f("waterClarity", 3);
			Shader.setUniform4f("waterColour", new Vector4f(0.1f, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",0.001f);
			break;
		case Biome.OCEAN:
			Shader.start(ShaderManager.waterShader);
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.02f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.1f, 0.25f,1));
			//Shader.setUniform1f("waveSpeed", 0.00001f);
			Shader.setUniform1f("maxDistortion",1f);
			break;
		case Biome.DESERT:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.5f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0, 0.2f, 0.5f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.MOUNTAIN:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 1f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 1);
			Shader.setUniform4f("waterColour", new Vector4f(1, .1f, .1f,1));
			//Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.01f);
			break;
		case Biome.SAVANNA:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0.1f);
			Shader.setUniform1f("waveStrength", 0.02f);
			Shader.setUniform1f("waterClarity", 10);
			Shader.setUniform4f("waterColour", new Vector4f(0.1f, 0.1f, 0.5f,1));
		//	Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0.1f);
			break;
		case Biome.TAIGA:
			Shader.start(ShaderManager.waterShader);
			Shader.setUniform1f("fresnelPower", 0f);
			Shader.setUniform1f("waveStrength", 0.002f);
			Shader.setUniform1f("waterClarity", 3);
			Shader.setUniform4f("waterColour", new Vector4f(.9f, 0.9f, 0.9f,1));
		//	Shader.setUniform1f("waveSpeed", 0.0001f);
			Shader.setUniform1f("maxDistortion",0f);
			break;
		}

		
	}

	public void movePlayer(String dir) {
		this.target = camera.target;

		float vx = position.x - target.x;
		float vy = position.y - target.y;
		vx *= speed;
		vy *= speed;
		switch (dir) {
		case "UP":
			displacement = upward;
			break;
		case "DOWN":
			displacement = upward.negate();
			break;
		case "FORWARD":
			displacement = new Vector3f(-vx, -vy, 0);// backward.negate();
			break;
		case "BACK":
			displacement = new Vector3f(vx, vy, 0);// backward;
			break;
		case "LEFT":
			displacement = new Vector3f(vy, -vx, 0);// left;
			break;
		case "RIGHT":
			displacement = new Vector3f(-vy, vx, 0);// left.negate();
			break;
		default:
			System.err.println("wtf");
		}
		displacement = displacement.normalize().scale(speed);
		move();

	}

	private void move() {
		boolean canMove = true;
		boolean noClip = true;
		if (!noClip) {

			this.destination[0] = position.add(displacement.scale(25 / SPEEDSCALER));
			this.destination[1] = position.add(displacement.scale(5 / SPEEDSCALER).negate());
			this.destination[2] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.normalize()));
			this.destination[3] = position.add(displacement.scale(5 / SPEEDSCALER).cross(upward.negate().normalize()));

			destination[0].z = Biome.getValue(destination[0], destination[0], false)[3] + 1.5f;
			destination[1].z = Biome.getValue(destination[1], destination[1], false)[3] + 1.5f;
			destination[2].z = Biome.getValue(destination[2], destination[2], false)[3] + 1.5f;
			destination[3].z = Biome.getValue(destination[3], destination[3], false)[3] + 1.5f;
			float rise = Math.max(Math.max(destination[0].z, destination[1].z),
					Math.max(destination[2].z, destination[3].z));
			rise = rise - position.z;

			if (rise > CLIMABLE) {
				canMove = false;// this part works on at least one side
			} else if (rise < -CLIMABLE) {
				displacement = displacement.add(upward.negate());
			} else {
				float[] diff = { position.z - destination[0].z, position.z - destination[1].z };
				float difference = Math.min(diff[0], diff[1]) * 0.1f;
				displacement = displacement.add(new Vector3f(0, 0, -difference));
			}
		}
		if (canMove) {
			this.translate(displacement);
			camera.move(displacement);
		}
		effect();
	}

}
