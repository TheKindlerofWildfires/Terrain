package object;

import static graphics.ShaderManager.objectShader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Render {
	public void render(Mesh mesh) {
		objectShader.start();
		objectShader.setUniform1i("texture_sampler",0);

		glBindVertexArray(mesh.getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		objectShader.stop();
	}

	public void cleanup() {
		if (objectShader != null) {
			// landShader.cleanup();
		}
	}
}