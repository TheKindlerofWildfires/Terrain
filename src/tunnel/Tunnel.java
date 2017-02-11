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

	public short[][][] tunnelArray = new short[xSize][ySize][zSize];

	public static short EXISTS = 0b00000001;

	public static short DOWN = 0b00000010;
	public static short UP = 0b00000100;
	public static short BACK = 0b00001000;
	public static short FRONT = 0b00010000;
	public static short RIGHT = 0b00100000;
	public static short LEFT = 0b01000000;
	public static short SPREAD = 0b10000000;

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
						GameObject object = getModel(tunnelArray[x][y][z]);
						object.placeAt(x * 2, y * 2, z * 2);
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
			short[][][] newTunnelArray = tunnelArray;
			iterations++;
			for (int x = 1; x < xSize - 1; x++) {
				for (int y = 1; y < ySize - 1; y++) {
					for (int z = 1; z < zSize - 1; z++) {
						short block = tunnelArray[x][y][z];
						if ((block & SPREAD) == SPREAD) {
							continue;
						}
						if ((block & EXISTS) == EXISTS) {
							if ((block & DOWN) == DOWN) {
								short newBlock = tunnelArray[x][y][z - 1];
								newBlock |= UP;
								newBlock = newBlock(newBlock);
								newTunnelArray[x][y][z - 1] = newBlock;
							}
							if ((block & UP) == UP) {
								short newBlock = tunnelArray[x][y][z + 1];
								newBlock |= DOWN;
								newBlock = newBlock(newBlock);
								newTunnelArray[x][y][z + 1] = newBlock;
							}
							if ((block & RIGHT) == RIGHT) {
								short newBlock = tunnelArray[x + 1][y][z];
								newBlock |= LEFT;
								newBlock = newBlock(newBlock);
								newTunnelArray[x + 1][y][z] = newBlock;
							}
							if ((block & LEFT) == LEFT) {
								short newBlock = tunnelArray[x - 1][y][z];
								newBlock |= RIGHT;
								newBlock = newBlock(newBlock);
								newTunnelArray[x - 1][y][z] = newBlock;
							}
							if ((block & FRONT) == FRONT) {
								short newBlock = tunnelArray[x][y + 1][z];
								newBlock |= BACK;
								newBlock = newBlock(newBlock);
								newTunnelArray[x][y + 1][z] = newBlock;
							}
							if ((block & BACK) == BACK) {
								short newBlock = tunnelArray[x][y - 1][z];
								newBlock |= FRONT;
								newBlock = newBlock(newBlock);
								newTunnelArray[x][y - 1][z] = newBlock;
							}
						}
					}
				}
			}
			tunnelArray = newTunnelArray;
			if (iterations > 400) {
				breaker = true;
				break;
			}
		}
	}

	public short getBit(int position, short ID) {
		return (short) ((ID >> position) & 1);
	}

	public short setBit(int position, short ID, boolean clear) {
		if (clear) {
			return (ID &= ~(1 << position)); //set 0
		} else {
			return (ID |= (1 << position)); //set 1
		}
	}

	public short newBlock(short block) {
		short newBlock = block;
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

	private GameObject getModel(short block) {
		int numberOfSides = 0;
		if ((block & 0b00000001) != 0b00000001) {
			System.err.println("this box is fake");
			return null;
		}
		if ((block & 0b00000010) == 0b00000010) {
			numberOfSides++;
		}
		if ((block & 0b00000100) == 0b00000100) {
			numberOfSides++;
		}
		if ((block & 0b00001000) == 0b00001000) {
			numberOfSides++;
		}
		if ((block & 0b00010000) == 0b00010000) {
			numberOfSides++;
		}
		if ((block & 0b00100000) == 0b00100000) {
			numberOfSides++;
		}
		if ((block & 0b01000000) == 0b01000000) {
			numberOfSides++;
		}
		GameObject result = new GameObject("none", "none", true);
		switch (numberOfSides) {
		case 0:
			return result;
		case 1:
			result = new GameObject("resources/models/boxOpenFace1.obj", "none", true);
			//DO ROTATION
			return result;
		case 2:
			return result;
		case 3:
			return result;
		case 4:
			return result;
		case 5:
			result = new GameObject("resources/models/boxOpenFace5.obj", "none", true);
			return result;
		case 6:
			return result;
		default:
			return result;
		}
	}

}
