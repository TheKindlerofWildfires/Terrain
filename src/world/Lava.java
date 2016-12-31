package world;

import maths.Vector3f;
import maths.Vector4f;
import particles.Geyser;
import particles.Particle;
import particles.ParticleEmitter;

public class Lava {
	private static ParticleEmitter particles;
	private static ParticleEmitter part2;
	private static Particle baseParticle;
	private static Particle baseParticle2;
	public Lava(){
		baseParticle = new Particle("resources/models/box.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		baseParticle.scale(.01f, .01f, .01f);
		baseParticle.translate(10, 0, 1);
		
		particles = new Geyser(baseParticle, 1000, 10, new Vector4f(1, .1f, .1f,1));
		particles.activate();
		baseParticle2 = new Particle("resources/models/box2.obj", "none", new Vector3f(0, 0, 1f), 100000l);
		baseParticle2.scale(.1f, .1f, .1f);
		baseParticle2.translate(0, 0, 10);
		part2 = new Geyser(baseParticle2, 1000, 10, new Vector4f(1, .1f, .1f,1));
		part2.activate();
	}
	public void update(long l) {
		particles.update(l);
		part2.update(l);		
	}
	public void render(Vector4f renderClipPlane) {
		particles.render(renderClipPlane);
		part2.render(renderClipPlane);
		
	}
	
}
