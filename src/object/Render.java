package object;

import static graphics.ShaderManager.objectShader;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Render {
	public void render(Mesh mesh) {
		objectShader.start();
		objectShader.setUniform1i("texture_sampler",0);
		objectShader.setUniform3f("colour",	mesh.getColour());
		objectShader.setUniform1i("useColour",	mesh.isTextured()	?	0	:	1);

		glBindVertexArray(mesh.getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D,	0);
		objectShader.stop();
	}

	public void cleanup() {
		if (objectShader != null) {
			// landShader.cleanup();
		}
	}
}