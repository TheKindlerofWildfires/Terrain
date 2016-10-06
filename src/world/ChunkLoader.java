package world;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import maths.Vector2i;

public class ChunkLoader extends Thread {
	public boolean running = true;

	public Queue<Chunk> loadedChunks = new LinkedBlockingQueue<Chunk>();
	public Queue<Vector2i> chunksToLoad = new LinkedBlockingQueue<Vector2i>();

	private void loadChunk(int x, int y) {
		loadedChunks.add(new Chunk(World.noise, x, y));
	}

	private void loadChunk(Vector2i xy) {
		loadedChunks.add(new Chunk(World.noise, xy.x, xy.y));
	}

	public void run() {
		int x = 0;
		int y = 0;
		int dx = 0;
		int dy = 0;
		dy = -1;
		int t = 3;
		int X = t;
		int Y = t;
		int maxI = t * t;
		for (int i = 0; i < maxI & running; i++) {
			if ((-X / 2 <= x) && (x <= X / 2) && (-Y / 2 <= y) && (y <= Y / 2)) {
				loadChunk(x, y);
			}
			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}
		while (running) {
<<<<<<< HEAD
			if (chunksToLoad.size() > 0) {
				int[] in = chunksToLoad.poll();
				//loadChunk(in[0], in[1]);
=======
			if (!chunksToLoad.isEmpty()) {
				loadChunk(chunksToLoad.poll());
>>>>>>> infinite-terrain
			}
		}
	}
}
