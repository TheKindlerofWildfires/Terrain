package oldStuff;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import maths.Vector3f;

import org.lwjgl.BufferUtils;

/**
 * Up for deletion
 * @author KingInYellow
 *
 */
public class Mesh {
	private final int vaoId;
	private final int posVboId;
	private final int idxVboId;
	// private final int colourVboId;
	private final int vertexCount;
	private final int textureVboId;
	private final int normalVboId;
	private Vector3f colour;
	private float[] positions;
	private float[] textCoords;
	private float[] normals;
	private byte[] indices;

	public Mesh(float[] positions, float[] textCoords, 	float[]	normals,int[] indices) {
		this.positions = positions;
		this.textCoords = textCoords;
		this.normals = normals;
		this.indices = new byte[indices.length];
		for(int i = 0; i<indices.length;i++){
			this.indices[i]=(byte) indices[i];
		}
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
		/*
		 * colourVboId = glGenBuffers(); FloatBuffer colourBuffer = BufferUtils
		 * .createFloatBuffer(colours.length); colourBuffer.put(colours).flip();
		 * glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
		 * glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
		 * glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		 */
		textureVboId = glGenBuffers();
		FloatBuffer textCoordsBuffer = BufferUtils
				.createFloatBuffer(textCoords.length);
		textCoordsBuffer.put(textCoords).flip();
		glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
		glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		normalVboId	=	glGenBuffers();
		FloatBuffer	vecNormalsBuffer	=	BufferUtils.createFloatBuffer(normals.length); 
		vecNormalsBuffer.put(normals).flip(); glBindBuffer(GL_ARRAY_BUFFER,	normalVboId);
		glBufferData(GL_ARRAY_BUFFER,	vecNormalsBuffer,	GL_STATIC_DRAW);
		glVertexAttribPointer(2,	3,	GL_FLOAT,	false,	0,	0);

		colour = new Vector3f(0.5f,1,1);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, posVboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glActiveTexture(GL_TEXTURE0);
		//glBindTexture(GL_TEXTURE_2D, texture.getId());

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
		glDeleteBuffers(textureVboId);
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

	public Vector3f getColour() {
		return colour;
	}

	public boolean isTextured() {
		return true;
	}

	public float[] getPos() {
		return positions;
	}

	public float[] getTextCoords() {
		// TODO Auto-generated method stub
		return textCoords;
	}

	public float[] getNormals() {
		// TODO Auto-generated method stub
		return normals;
	}
	
	public byte[] getIndices() {
		// TODO Auto-generated method stub
		return indices;
	}
}
