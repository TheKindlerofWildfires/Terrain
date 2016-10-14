package world;

import static graphics.Shader.setUniform1i;
import static graphics.Shader.setUniformMatrix4f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import graphics.ShaderManager;
import graphics.Texture;
import graphics.Window;
import maths.Vector4f;
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
	protected void renderPrep(Vector4f clipPlane) {
		setUniform1i("reflectionTexture", 0);
		setUniform1i("refractionTexture", 1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Window.reflection.getTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, Window.refraction.getTexture());
		setUniformMatrix4f("modelView", graphics.GraphicsManager.camera.view.multiply(model.getMatrix()));
	}
}
