package world;

import java.util.LinkedList;
import java.util.Queue;

public class ChunkLoader extends Thread {

	public boolean running = true;

	public Queue<Chunk> loadedChunks = new LinkedList<Chunk>();

	public void run() {
		int x = 0;
		int y = 0;
		int dx = 0;
		int dy = 0;
		dy = -1;
		int t = 20;
		int X = 20;
		int Y = 20;
		int maxI = t * t;
		for (int i = 0; i < maxI & running; i++) {
			if ((-X / 2 <= x) && (x <= X / 2) && (-Y / 2 <= y) && (y <= Y / 2)) {
				loadedChunks.add(new Chunk(World.noise, x, y, false));
			}
			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}

	}
}
