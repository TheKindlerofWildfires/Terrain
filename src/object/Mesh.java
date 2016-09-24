package object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * This class takes a series of points (float[])and turns them into a "mesh"
 * This mesh can be passed to render to show it
 * 
 * @author KingInYellow
 *
 */
public class Mesh {
	private final int vaoId;
	private final int posVboId;
	private final int idxVboId;
	private final int vertexCount;

	public Mesh(float[] positions, int[] indices) {
		vertexCount = indices.length;
		FloatBuffer verticesBuffer = BufferUtils
				.createFloatBuffer(positions.length);
		verticesBuffer.put(positions).flip();
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		posVboId = glGenBuffers();
		
		idxVboId = glGenBuffers();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, posVboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(posVboId);
		glDeleteBuffers(idxVboId);
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
