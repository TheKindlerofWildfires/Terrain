package models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import models.obj.OBJLoader;

public final class ModelManager {

	private static Map<String, Mesh> models = new HashMap<String, Mesh>();

	/**
	 * can't be called. static classes are fun :)
	 */
	private ModelManager() {
	}

	/**
	 * Loads a non-gl model
	 * Can be called from anywhere
	 * @param path to model file
	 * @return the mesh with ONLY float array or null if invalid filetype
	 * @throws IOException if you're bad at files
	 */
	public static Mesh loadModel(String path) throws IOException {
		if (models.containsKey(path)) {
			return models.get(path);
		} else {
			if (path.endsWith(".obj")) {
				Mesh mesh = new Mesh(OBJLoader.loadMesh(path));
				models.put(path, mesh);
				return mesh;
			} else if (path.endsWith(".md5")) {
				System.err.println("Probably should be able to load md5s :(");
				//float[] mesh = MD5Loader.loadMesh(path, 0);
				//nonGlModels.put(path, mesh);
				return null;
			} else {
				System.out.println("Sad");
				return null;
			}
		}
	}

	/**
	 * Loads a GL model
	 * Can only be called from graphics thread
	 * @param path to model file
	 * @return the mesh w/ vao and not guaranteed float array or null if invalid filetype
	 * @throws IOException if you're bad at files
	 */
	public static Mesh loadGlModel(String path) throws IOException {
		if (models.containsKey(path)) {
			if (models.get(path).isGL) {
				return models.get(path);

			} else {
				models.get(path).makeGL(3);
				return models.get(path);
			}
		} else {
			if (path.endsWith(".obj")) {
				Mesh mesh = new Mesh(OBJLoader.loadMesh(path));
				mesh.makeGL(3);
				models.put(path, mesh);
				return mesh;
			} else if (path.endsWith(".md5")) {
				System.err.println("Probably should be able to load md5s :(");
				//float[] mesh = MD5Loader.loadMesh(path, 0);
				//nonGlModels.put(path, mesh);
				return null;
			} else {
				System.out.println("Sadder");
				return null;
			}
		}
	}
}
