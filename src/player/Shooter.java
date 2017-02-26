package player;

import graphics.Window;
import maths.Vector3f;
import object.GameObject;

public class Shooter extends Item{

	public Shooter(String tag, int cd, int charge, String location) {
		super(tag, cd, charge, location);
	}
	
	
	@Override
	public void activate(){
		//Window.entityManager.player.position
		Vector3f position =Window.entityManager.player.position;// GraphicsManager.camera.pos;
		GameObject shot = new GameObject("resources/models/box.obj", "none", true);
		shot.placeAt(position.x, position.y, position.z);
		Vector3f ray = Window.entityManager.player.target.subtract(position);
		shot.velocity = ray.normalize().scale(.8f);
		Window.objectManager.add(shot);
	}

}
