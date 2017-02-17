package player;

import graphics.GraphicsManager;
import graphics.Window;
import maths.Vector3f;
import object.GameObject;

public class Shooter extends Item{

	public Shooter(String tag, int cd, int charge, String location) {
		super(tag, cd, charge, location);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void activate(){
		Vector3f position = GraphicsManager.camera.pos;
		GameObject shot = new GameObject("resources/models/box.obj", "none", true);
		shot.placeAt(position.x, position.y, position.z);
		Vector3f ray = GraphicsManager.camera.target.subtract(position);
		shot.velocity = ray.scale(.2f);
		Window.objectManager.add(shot);
		//raycast
	}

}
