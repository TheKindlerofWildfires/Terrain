package tunnel;

import java.util.ArrayList;
import java.util.Random;

import maths.Vector4f;
import object.GameObject;

public class Tunnel {
	public ArrayList<GameObject> solids = new ArrayList<GameObject>();

	// The order is left, right, forward, backs,up, down, exists
	//1 is open
	int xSize = 30;
	int ySize = 30;
	int zSize = 30;

	Random rng = new Random();

	public byte[][][] tunnelArray = new byte[xSize][ySize][zSize];

	public static byte EXISTS = 0b00000001;

	public static byte DOWN = 0b00000010;
	public static byte UP = 0b00000100;
	public static byte BACK = 0b00001000;
	public static byte FRONT = 0b00010000;
	public static byte RIGHT = 0b00100000;
	public static byte LEFT = 0b01000000;

	public Tunnel() {
		// this fills out the array
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				for (int z = 0; z < zSize; z++) {
					tunnelArray[x][y][z] = 0;
				}
			}
		}
		tunnelArray[5][5][5] = 0b01111111;
		genTunnel();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				for (int z = 0; z < zSize; z++) {
					//System.out.print(tunnelArray[x][y][z] & EXISTS);
					if ((tunnelArray[x][y][z] & EXISTS) == EXISTS) {
						GameObject object = new GameObject("resources/models/box.obj", "none", true);
						object.placeAt(x*2, y*2, z*2);
						solids.add(object);
					}
				}
				//System.out.println();
			}
		//	System.out.println();
		}

	}

	public void update() {

	}

	public void render(Vector4f clipPlane) {
		solids.stream().forEach(c -> c.render(clipPlane));
	}

	public void genTunnel() {
		boolean breaker = true;
		int iterations = 0;
		while (breaker) {
			iterations++;
			for (int x = 1; x < xSize - 1; x++) {
				for (int y = 1; y < ySize - 1; y++) {
					for (int z = 1; z < zSize - 1; z++) {
						byte block = tunnelArray[x][y][z];
						if ((block & EXISTS) == EXISTS) {
							if ((block & DOWN) == DOWN) {
								byte newBlock = tunnelArray[x][y][z - 1];
								newBlock |= UP;
								newBlock = newBlock(newBlock);
								tunnelArray[x][y][z - 1] = newBlock;
							}
							if ((block & UP) == UP) {
								byte newBlock = tunnelArray[x][y][z + 1];
								newBlock |= DOWN;
								newBlock = newBlock(newBlock);
								tunnelArray[x][y][z + 1] = newBlock;
							}
							if ((block & RIGHT) == RIGHT) {
								byte newBlock = tunnelArray[x + 1][y][z];
								newBlock |= LEFT;
								newBlock = newBlock(newBlock);
								tunnelArray[x + 1][y][z] = newBlock;
							}
							if ((block & LEFT) == LEFT) {
								byte newBlock = tunnelArray[x - 1][y][z];
								newBlock |= RIGHT;
								newBlock = newBlock(newBlock);
								tunnelArray[x - 1][y][z] = newBlock;
							}
							if ((block & FRONT) == FRONT) {
								byte newBlock = tunnelArray[x][y + 1][z];
								newBlock |= BACK;
								newBlock = newBlock(newBlock);
								tunnelArray[x][y + 1][z] = newBlock;
							}
							if ((block & BACK) == BACK) {
								byte newBlock = tunnelArray[x][y - 1][z];
								newBlock |= FRONT;
								newBlock = newBlock(newBlock);
								tunnelArray[x][y - 1][z] = newBlock;
							}
						}
					}
				}
			}
			if (iterations > 10) {
				breaker = true;
				break;
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

	public byte newBlock(byte block) {
		byte newBlock = block;
		if ((newBlock & EXISTS) == EXISTS) {
			return newBlock;
		}
		newBlock |= EXISTS;
		if (rng.nextFloat() < .25) {
			newBlock |= UP;
		}
		if (rng.nextFloat() < .25) {
			newBlock |= DOWN;
		}
		if (rng.nextFloat() < .25) {
			newBlock |= RIGHT;
		}
		if (rng.nextFloat() < .25) {
			newBlock |= LEFT;
		}
		if (rng.nextFloat() < .25) {
			newBlock |= FRONT;
		}
		if (rng.nextFloat() < .25) {
			newBlock |= BACK;
		}
		return newBlock;
	}

}
