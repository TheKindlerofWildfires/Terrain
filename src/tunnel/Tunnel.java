package tunnel;

import java.util.ArrayList;

import maths.Vector4f;
import object.GameObject;

public class Tunnel {
	public ArrayList<GameObject> tunnelArray = new ArrayList<GameObject>();
	public Tunnel(){
		genTunnel();
	}
	public void update(){
	
	}
	public void right(){
		for (int i = 0; i < tunnelArray.size(); i++) {
			GameObject object = tunnelArray.get(i);
			object.rotate(90, 1, 0, 0);
		}
	}
	public void render(Vector4f clipPlane) {
		
	}
	public void genTunnel(){
		//this takes a piece that counts as an node
		/*
		 * Starts with a 8 noded piece, gens a piece of a random variety from the list(which should be squed towards smaller numbers)
		 * 8-1 (hive)
		 * 7-1 (hub2)
		 * 6-2 (hub)
		 * 5-3(junction2)
		 * 4-5(junction)
		 * 3-8 (forks)
		 * 2-13 (tunnels)
		 * 1-21 (ends)
		 * The things should stop a certain height above sea level (also someone is going to have to fix the water)
		 * Depends on how long it takes to come to a halt(if it reliably comes to a halt) I will set a certain probability of seed per chunk(small)
		 * Also need a way to git rid of the land covering the nodes
		 */

	}

}
