package particles;

import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import graphics.ShaderManager;
import maths.Transformation;
import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Particle extends GameObject {

	private long ttl; //time remaining to live (milliseconds)
	float angleX;
	float angleY;

	public Particle(String objectPath, String texturePath, Vector3f velocity, long ttl) {
		super(objectPath, texturePath, true);
		this.velocity = velocity;
		this.ttl = ttl;
		angleX = (float) Math.toDegrees(Math.atan(velocity.x / velocity.z));
		angleY = (float) Math.toDegrees(Math.atan(velocity.y / velocity.z));
		this.shader = ShaderManager.particleShader;
	}

	public Particle(Particle baseParticle) {
		super();
		this.ttl = baseParticle.ttl;
		this.velocity = new Vector3f(baseParticle.velocity);
		this.boundingBox = baseParticle.boundingBox;
		this.hasMaterial = baseParticle.hasMaterial;
		this.isGL = baseParticle.isGL;
		this.material = baseParticle.material;
		this.model = new Transformation(baseParticle.model);
		this.position = new Vector3f(baseParticle.position);
		this.shader = baseParticle.shader;
		this.texture = baseParticle.texture;
		this.textured = baseParticle.textured;
		this.vao = baseParticle.vao;
		this.angleX = baseParticle.angleX;
		this.angleY = baseParticle.angleY;
		this.shader = ShaderManager.particleShader;
		this.placeAt(0, 0, 0);
	}

	@Override
	public void physic() {
		//not effected by physics :P
	}

	public long updateTtl(long elapsedTime) {
		ttl -= elapsedTime;
		return ttl;
	}

	@Override
	public void render(Vector4f clipPlane) {
		start(shader);
		renderPrep(clipPlane);
		glBindVertexArray(vao.getVaoID());
		System.out.println();
		glDrawArraysInstanced(GL_TRIANGLES, 0, vao.getSize(), 5);
		stop();
	}
}
