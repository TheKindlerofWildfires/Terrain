package render;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import object.Mesh;
import static graphics.ShaderManager.landShader;
public class Render {
	public void render(Mesh mesh) {
		landShader.start();
		glBindVertexArray(mesh.getVaoId());
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		landShader.stop();
	}
	public void cleanup() {
		if ( landShader != null) {
			//landShader.cleanup();
		}
	}
}