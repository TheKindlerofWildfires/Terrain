package graphics;

import static org.lwjgl.opengl.GL20.*;
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
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

import java.nio.IntBuffer;

import maths.Matrix4f;
import maths.Utilities;
import maths.Vector3f;

class Attenuation {
	float constant;
	float linear;
	float exponent;
}

class PointLight {
	Vector3f colour;
	Vector3f position; // Light position is assumed to be in view coordinates
	float intensity;
	Attenuation att;
}

class Material {
	Vector3f colour;
	int useColour;
	float reflectance;
};

public class Shader {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private boolean initialized = false;

	public Shader(String vertexFile, String fragmentFile) {
		initialized = true;
		vertexShaderID = Utilities.loadShader(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderID = Utilities.loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		if (vertexShaderID == -1 || fragmentShaderID == -1) {
			initialized = false;
		}
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		glLinkProgram(programID);
		glValidateProgram(programID);
	}

	public int getID() {
		return this.programID;
	}

	public int getUniform(String name) {
		// for (StackTraceElement ste : Thread.currentThread().getStackTrace())
		// {
		// System.out.println(ste);
		// }
		int result = glGetUniformLocation(programID, name);
		if (result == -1) {
			System.err.println("Could not find uniform variable " + name);
		}
		return result;
	}

	public int getUniformBlock(String name) {
		int result = glGetUniformBlockIndex(programID, name);
		if (result == -1) {
			System.err.println("Could not find uniform block " + name);
		}
		return result;
	}

	public void setUniformBlockf(String name, int ubo, int location) {
		glUniformBlockBinding(programID, getUniformBlock(name), location);
		glBindBufferBase(GL_UNIFORM_BUFFER, location, ubo);
	}

	public void setUniform1f(String name, float position) {
		glUniform1f(getUniform(name), position);
	}

	public void setUniformMatrix4f(String name, Matrix4f mat) {
		glUniformMatrix4fv(getUniform(name), false, mat.getBuffer());
	}

	public void setUniform1i(String name, int position) {
		glUniform1i(getUniform(name), position);
	}

	public void setUniform3f(String name, Vector3f position) {
		glUniform3f(getUniform(name), position.x, position.y, position.z);
	}

	public void start() {
		assert initialized : "can't use an uninitialized shader!";
		glUseProgram(programID);
	}

	public void stop() {
		glUseProgram(0);
	}

	public void setUniform1iv(String name, IntBuffer position) {
		glUniform1iv(getUniform(name), position);
	}

	public void setMaterial(String name, Material material) {
		setUniform3f(name + ".colour", material.colour);
		setUniform1i(name + ".useColour", material.useColour);
		setUniform1f(name + ".reflectance", material.reflectance);
	}

	public void setPointLight(String name, PointLight pointlight) {
		setUniform3f(name + ".colour", pointlight.colour);
		setUniform3f(name + ".position", pointlight.position);
		setUniform1f(name + ".intensity", pointlight.intensity);
		setUniform1f(name + ".att.constant", pointlight.att.constant);
		setUniform1f(name + ".att.linear", pointlight.att.linear);
		setUniform1f(name + ".att.exponent", pointlight.att.exponent);
	}
}
