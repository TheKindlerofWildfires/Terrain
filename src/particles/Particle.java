package particles;

import maths.Vector3f;
import object.GameObject;

public class Particle extends GameObject {

	private long ttl; //time remaining to live (milliseconds)
	
	public Particle(String objectPath, String texturePath, Vector3f velocity, long ttl) {
		super(objectPath,texturePath,true);
		this.velocity = velocity;
		this.ttl = ttl;
	}
	
	public Particle(Particle baseParticle){
		super();
		this.boundingBox = baseParticle.boundingBox;
		this.hasMaterial = baseParticle.hasMaterial;
		this.isGL = baseParticle.isGL;
		this.material = baseParticle.material;
		this.model = baseParticle.model;
		this.position = baseParticle.position;
		this.shader = baseParticle.shader;
		this.texture = baseParticle.texture;
		this.textured = baseParticle.textured;
		this.vao = baseParticle.vao;
	}
	
	@Override
	public void physic(){
		//not effected by physics :P
	}
	
	public long updateTtl(long elapsedTime){
		ttl-=elapsedTime;
		return ttl;
	}
}
