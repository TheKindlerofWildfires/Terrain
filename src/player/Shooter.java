package player;

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
		Window.objectManager.add(new GameObject("resources/models/uvsphere2.obj", "resources/textures/uvlayout.png", true),Window.entityManager.player.position, new Vector3f(0,0.01f,0));
		//raycast
	}

}
