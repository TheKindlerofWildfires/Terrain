package particles;

import graphics.ShaderManager;
import maths.Transformation;
import maths.Vector3f;
import object.GameObject;

public class Particle extends GameObject {


	private long ttl; //time remaining to live (milliseconds)
	public float angleX;
	public float angleY;

	/**
	 * Makes a new base particle
	 * @param objectPath path to obj file
	 * @param texturePath path to texture file
	 * @param velocity velocity of particle
	 * @param ttl lifetime of partilcle
	 */
	public Particle(String objectPath, String texturePath, Vector3f velocity, long ttl) {
		super(objectPath, texturePath, true);
		this.velocity = velocity;
		this.ttl = ttl;
		angleX = (float) Math.toDegrees(Math.atan(velocity.x / velocity.z));
		angleY = (float) Math.toDegrees(Math.atan(velocity.y / velocity.z));
		this.shader = ShaderManager.particleShader;
	}

	/**
	 * makes a new particle based on the base particle
	 * @param baseParticle particle on which new particle will be based
	 */
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
		//this.placeAt(0, 0, 0);
	}

	/**
	 * particles dont get physic'd
	 */
	@Override
	public void physic() {

	}

	/**
	 * updates time to live
	 * @param elapsedTime time since last update tick
	 * @return new time to live
	 */
	public long updateTtl(long elapsedTime) {
		ttl -= elapsedTime;
		return ttl;
	}
}
