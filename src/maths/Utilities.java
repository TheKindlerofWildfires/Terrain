package maths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.BufferUtils;

public class Utilities {

	private static final Pattern INCLUDE_PATTERN = Pattern.compile("\\s*#include\\s*(\\S+)");
	private static final String SHADER_LOCATION = "src/shaders/";

	public static float[][][] unflatten(int[] input, int xSize, int ySize, int zSize) {
		float[][][] output = new float[xSize][ySize][zSize];
		for (int i = 0; i < input.length; i++) {
			int xPos = i % xSize;
			int yPos = (i / xSize) % ySize;
			int zPos = i / (xSize * ySize);
			output[xPos][yPos][zPos] = input[i];
		}
		if (output[0][0].length != zSize) {
			System.err.println("i feel hurt and betrayed");
		}
		return output;
	}

	public static float[] flatten(int[][][] properties) {
		float[] output = new float[properties.length * properties[0].length * properties[0][0].length];
		int counter = 0;
		for (int z = 0; z < properties[0][0].length; z++) {
			for (int y = 0; y < properties[0].length; y++) {
				for (int x = 0; x < properties.length; x++) {
					output[counter++] = properties[x][y][z];
				}
			}
		}
		return output;
	}

	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static ByteBuffer createByteBuffer(byte[] data) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/** pads to the correct shit for ubo */
	public static FloatBuffer createUniformFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 4);
		for (int i = 0; i < data.length; i++) {
			buffer.put(data[i]);
			buffer.put(0);
			buffer.put(0);
			buffer.put(0);
		}
		buffer.flip();
		return buffer;
	}

	public static int loadShader(String filepath, int type) {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				if (buffer.contains("#include")) {
					Matcher matcher = INCLUDE_PATTERN.matcher(buffer);
					matcher.matches();
					String libpath = SHADER_LOCATION + matcher.group(1);
					BufferedReader libreader = new BufferedReader(new FileReader(libpath));
					while ((buffer = libreader.readLine()) != null) {
						result.append(buffer);
						result.append("\n");
					}
					libreader.close();
				} else {
					result.append(buffer);
				}
				result.append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, result.toString());
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.err.println(-1);
			return -1;
		}
		return shaderID;
	}

	/**
	 * creates uniform buffer object in gpu memory
	 */
	public static int createUniformBuffer(FloatBuffer data) {
		int ubo = glGenBuffers();
		glBindBuffer(GL_UNIFORM_BUFFER, ubo);
		glBufferData(GL_UNIFORM_BUFFER, data, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_UNIFORM_BUFFER, 0);
		return ubo;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return a mod b
	 */
	public static int mod(int a, int b) {
		if (a > 0) {
			return a % b;
		} else if (a < 0) {
			return b + a % b;
		} else {
			return 0;
		}
	}
<<<<<<< HEAD
=======

>>>>>>> master
}
