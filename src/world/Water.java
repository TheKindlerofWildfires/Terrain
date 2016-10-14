package world;

import static graphics.Shader.setMaterial;
import static graphics.Shader.setUniform1i;
import static graphics.Shader.setUniformMatrix4f;
import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import graphics.ShaderManager;
import graphics.Texture;
import graphics.Window;
import object.GameObject;

public class Water extends GameObject {

	public Water(String modelPath) {
		super(modelPath, "none");
		translate(0, 0, Chunk.WATERLEVEL);
		rotate(90, 1, 0, 0);
		scale(10, 10, 10);
		shader = ShaderManager.waterShader;
		hasMaterial = false;
		texture = new Texture("src/textures/wood.png");
	}

	@Override
	public void render() {
		start(shader);
		setUniform1i("reflectionTexture", 0);
		//setUniform1i("refractionTexture", 1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Window.textureColorbuffer);
		//glActiveTexture(GL_TEXTURE1);
		//glBindTexture(GL_TEXTURE_2D, Window.waterFBO.getRefractionTexture());
		setUniformMatrix4f("modelView", graphics.GraphicsManager.camera.view.multiply(model.getMatrix()));
		if (hasMaterial) {
			setMaterial("material", material);
		}
		if (textured) {
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());

		stop();
	}
}
