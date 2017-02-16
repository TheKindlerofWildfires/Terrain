package player;

import graphics.Window;
import object.GameObject;

public class Shooter extends Item{

	public Shooter(String tag, int cd, int charge, String location) {
		super(tag, cd, charge, location);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void activate(){
		Window.objectManager.add(new GameObject("resources/models/uvsphere2.obj", "resources/textures/uvlayout.png", true),Window.entityManager.player.position);
		//raycast
	}

}
