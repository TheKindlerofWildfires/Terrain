package world;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import maths.Vector2i;

public class ChunkLoader extends Thread {
	public Object lock = new Object();

	public boolean running = true;

	public Queue<Chunk> loadedChunks = new LinkedBlockingQueue<Chunk>();
	public Queue<Vector2i> chunksToLoad = new LinkedBlockingQueue<Vector2i>();

	private void loadChunk(Vector2i xy) {
		loadedChunks.add(new Chunk(World.noise, xy.x, xy.y));
	}

	public void run() {
		this.setName("Chunk Loader");
		synchronized (lock) {
			while (running) {
				if (!chunksToLoad.isEmpty()) {
					loadChunk(chunksToLoad.poll());
				}
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
