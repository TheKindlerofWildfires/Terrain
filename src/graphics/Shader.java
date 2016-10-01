package graphics;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

import java.nio.IntBuffer;

import maths.Matrix4f;
import maths.Utilities;
import maths.Vector3f;

public abstract class Shader {
	private static int currentProgramID;

	public static int makeShader(String vertexFile, String fragmentFile) {
		int vertexShaderID = Utilities.loadShader(vertexFile, GL_VERTEX_SHADER);
		int fragmentShaderID = Utilities.loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		int programID = glCreateProgram();
		if (vertexShaderID == -1 || fragmentShaderID == -1) {
			assert false : "shader compilation failed";
		}
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		glLinkProgram(programID);
		glValidateProgram(programID);
		return programID;
	}

	private static int getUniform(String name) {
		// for (StackTraceElement ste : Thread.currentThread().getStackTrace())
		// {
		// System.out.println(ste);
		// }
		int result = glGetUniformLocation(currentProgramID, name);
		if (result == -1) {
			System.err.println("Could not find uniform variable " + name);
		}
		return result;
	}

	private static int getUniformBlock(String name) {
		int result = glGetUniformBlockIndex(currentProgramID, name);
		if (result == -1) {
			System.err.println("Could not find uniform block " + name);
		}
		return result;
	}

	public static void setUniformBlockf(String name, int ubo, int location) {
		glUniformBlockBinding(currentProgramID, getUniformBlock(name), location);
		glBindBufferBase(GL_UNIFORM_BUFFER, location, ubo);
	}

	public static void setUniform1f(String name, float position) {
		glUniform1f(getUniform(name), position);
	}

	public static void setUniformMatrix4f(String name, Matrix4f mat) {
		glUniformMatrix4fv(getUniform(name), false, mat.getBuffer());
	}

	public static void setUniform1i(String name, int position) {
		glUniform1i(getUniform(name), position);
	}

	public static void setUniform3f(String name, Vector3f position) {
		glUniform3f(getUniform(name), position.x, position.y, position.z);
	}

	public static void start(int id) {
		currentProgramID = id;
		glUseProgram(currentProgramID);
	}

	public static void stop() {
		glUseProgram(0);
	}

	public static void setUniform1iv(String name, IntBuffer position) {
		glUniform1iv(getUniform(name), position);
	}

	public static void setMaterial(String name, Material material) {
		setUniform3f(name + ".colour", material.colour);
		setUniform1i(name + ".useColour", material.useColour);
		setUniform1f(name + ".reflectance", material.reflectance);
	}

	public static void setPointLight(String name, PointLight pointlight) {
		setUniform3f(name + ".colour", pointlight.colour);
		setUniform3f(name + ".position", pointlight.position);
		setUniform1f(name + ".intensity", pointlight.intensity);
		setUniform1f(name + ".att.constant", pointlight.att.constant);
		setUniform1f(name + ".att.linear", pointlight.att.linear);
		setUniform1f(name + ".att.exponent", pointlight.att.exponent);
	}

	public static void setDirectionalLight(String uniformName, DirectionalLight dirLight) {
		setUniform3f(uniformName + ".colour", dirLight.colour);
		setUniform3f(uniformName + ".direction", dirLight.direction);
		setUniform1f(uniformName + ".intensity", dirLight.intensity);
	}
}
