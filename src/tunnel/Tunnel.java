package tunnel;

import java.util.ArrayList;

import graphics.Window;
import maths.Vector4f;
import object.GameObject;

public class Tunnel {
	public ArrayList<GameObject> solids = new ArrayList<GameObject>();
	// Each byte represents a piece of space. A one represents a closed side and
	// a zero represents an open side
	// The order is left, right, forward, backs,up, down, exists
	public ArrayList<ArrayList<ArrayList<Byte>>> tunnelArray = new ArrayList<ArrayList<ArrayList<Byte>>>();

	int x = 10;
	int y = 10;
	int z = 10;

	public Tunnel() {
		// this fills out the array
		for (int i = 0; i < x; i++) { //right left
			ArrayList<ArrayList<Byte>> outer = new ArrayList<ArrayList<Byte>>();
			for (int j = 0; j < y; j++) {//forward back
				ArrayList<Byte> inner = new ArrayList<Byte>();
				for (int k = 0; k < z; k++) { //up down
					inner.add((byte) 0);
				}
				outer.add(inner);
			}
			tunnelArray.add(outer);
		}

		tunnelArray.get(5).get(5).set(5, (byte) 1);
		genTunnel();
		System.out.println(tunnelArray.toString());

	}

	public void update() {

	}

	public void render(Vector4f clipPlane) {

	}

	public void genTunnel() { //well this currently simply makes every single block into a node
		// this needs to take the actives and deal with them.
		boolean breaker = true;
		while(breaker){
			breaker = false;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				for (int k = 0; k < z; k++) {
					if (getBit(0, tunnelArray.get(i).get(j).get(k)) == 1) {
						for (int l = 0; l < 6; l++) {
							/* 0: right
							 * 1: left
							 * 2: forward
							 * 3: back
							 * 4: up
							 * 5: down
							 */
							if (Window.worldRandom.nextInt(8) == 0) {
								tunnelArray.get(i).get(j).set(k,setBit(l, tunnelArray.get(i).get(j).get(k), false)); //says "I have an opening here"
								System.out.println(tunnelArray.get(i).get(j).get(k));
								switch(l){
								case 0:
									if(i!=x-1){
										tunnelArray.get(i+1).get(j).set(k,setBit(0, tunnelArray.get(i+1).get(j).get(k), false));
										breaker = true;
									}
									break;
								case 1:
									if(i!=0){
										tunnelArray.get(i-1).get(j).set(k, setBit(0, tunnelArray.get(i-1).get(j).get(k), false));
										breaker = true;
									}
									break;
								case 2:
									if(j!=y-1){
										tunnelArray.get(i).get(j+1).set(k, setBit(0, tunnelArray.get(i).get(j+1).get(k), false));
										breaker = true;
									}
									break;
								case 3:
									if(j!=0){
										tunnelArray.get(i).get(j-1).set(k,setBit(0, tunnelArray.get(i).get(j-1).get(k), false));
										breaker = true;
									}
									break;
								case 4:
									if(k!=z-1){
										tunnelArray.get(i).get(j).set(k,setBit(0, tunnelArray.get(i).get(j).get(k+1), false));
										breaker = true;
									}
									break;
								case 5:
									if(k!=0){
										tunnelArray.get(i).get(j).set(k,setBit(0, tunnelArray.get(i).get(j).get(k-1), false));
										breaker = true;
									}
									break;
								}
								
							}
							
						}
						setBit(0, tunnelArray.get(i).get(j).get(k), true); //Says I am no longer active
					}
				}
			}
		}
		}
	}

	public byte getBit(int position, byte ID) {
		return (byte) ((ID >> position) & 1);
	}

	public byte setBit(int position, byte ID, boolean clear) {
		if (clear) {
			return (ID &= ~(1 << position)); //set 0
		} else { 
			return (ID |= (1 << position)); //set 1
		}
	}

}
