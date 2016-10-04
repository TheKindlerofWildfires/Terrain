package world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ChunkLoader extends Thread {

	public boolean running = true;

	public Map<int[], Boolean> chunks = new HashMap<int[], Boolean>();

	public Queue<Chunk> loadedChunks = new LinkedList<Chunk>();
	public Queue<int[]> chunksToLoad = new LinkedList<int[]>();

	private void loadChunk(int x, int y) {
		//this  line doesn't work at all
		/*
		 * Which leads to it just reloading the same chunks over and over again, hogging the pipline from this side
		 */
		/*
		 * HEY THIS IS THE ERROR I NEEDED TO TELL YOU ABOUT, THIS IS HOGGING THE PIPELINE WITH ITS BADNESS
		 * To fix the pipeline problem:
		 * a) Make this line actually detect something
		 * b) Check if it is fixed
		 * c) If fixed celebrate 
		 * d) If not fixed cry
		 * e) Investigate the queing methodss
		 */
		if (chunks.get(new int[] { x,y }) == null || !chunks.get(new int[] { x,y })) {
			loadedChunks.add(new Chunk(World.noise, x, y));
			chunks.put(new int[] { x,y }, true);
		}
	}

	public void run() {
		int x = 0;
		int y = 0;
		int dx = 0;
		int dy = 0;
		dy = -1;
		int t = 4;
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
			if (chunksToLoad.size() > 0) {
				int[] in = chunksToLoad.poll();
				//loadChunk(in[0], in[1]);
			}
		}
	}
}
