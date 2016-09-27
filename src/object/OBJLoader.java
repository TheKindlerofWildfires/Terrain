package object;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import graphics.VertexArrayObject;
import maths.Vector2f;
import maths.Vector3f;

public class OBJLoader {

	public static VertexArrayObject loadMesh(String fileName) throws Exception {
		Path path = Paths.get(fileName);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Face> faces = new ArrayList<>();
		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
			case "v":
				// Geometric vertex
				Vector3f vec3f = new Vector3f(Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				vertices.add(vec3f);
				break;
			case "vt":
				// Texture coordinate
				Vector2f vec2f = new Vector2f(Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]));
				textures.add(vec2f);
				break;
			case "vn":
				// Vertex normal
				Vector3f vec3fNorm = new Vector3f(Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				normals.add(vec3fNorm);
				break;
			case "f":
				Face face = new Face(tokens[1], tokens[2], tokens[3]);
				faces.add(face);
				break;
			default:
				// Ignore other lines
				break;
			}
		}
		return reorderLists(vertices, textures, normals, faces);
	}

	private static VertexArrayObject reorderLists(List<Vector3f> posList,
			List<Vector2f> textCoordList, List<Vector3f> normList,
			List<Face> facesList) {
		List<Integer> indices = new ArrayList<Integer>();
		float[] posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}

		List<Vector3f> vertices = new ArrayList<Vector3f>();

		//System.out.println(facesList.size());
		//int x = 1;
		for (Face face : facesList) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			//System.out.println(x++);
		//	int y = 0;
			for (IdxGroup indValue : faceVertexIndices) {
				//System.out.println(y++);
				int pos = indValue.idxPos;
				int norm = indValue.idxVecNormal;
				int textCoord = indValue.idxTextCoord;
				vertices.add(posList.get(pos));
				vertices.add(normList.get(norm));
				if (textCoordList.size() > 0) {
					vertices.add(new Vector3f(textCoordList.get(textCoord), 0));
				} else {
					vertices.add(new Vector3f(0, 0, 0));
				}
			}
		}
		//System.out.println(vertices.size());
		int[] indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		float[] verts = new float[vertices.size() * 3];
		int c = 0;
		for (i = 0; i < vertices.size(); i++) {
			verts[c++] = vertices.get(i).x;
			verts[c++] = vertices.get(i).y;
			verts[c++] = vertices.get(i).z;
		}
		return new VertexArrayObject(verts, 3);
	}
	@Deprecated
	private static void processFaceVertex(IdxGroup indices,
			List<Vector2f> textCoordList, List<Vector3f> normList,
			List<Integer> indicesList, float[] texCoordArr, float[] normArr) {
		int posIndex = indices.idxPos;
		indicesList.add(posIndex);
		if (indices.idxTextCoord >= 0) {
			Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
	}
}

class IdxGroup {
	public static final int NO_VALUE = -1;
	public int idxPos;
	public int idxTextCoord;
	public int idxVecNormal;

	public IdxGroup() {
		idxPos = NO_VALUE;
		idxTextCoord = NO_VALUE;
		idxVecNormal = NO_VALUE;
	}
}

class Face {
	/** * List of idxGroup groups for a face triangle (3 vertices per face). */
	private IdxGroup[] idxGroups = new IdxGroup[3];

	public Face(String v1, String v2, String v3) {
		idxGroups = new IdxGroup[3];
		// Parse the lines
		idxGroups[0] = parseLine(v1);
		idxGroups[1] = parseLine(v2);
		idxGroups[2] = parseLine(v3);
	}

	private IdxGroup parseLine(String line) {
		IdxGroup idxGroup = new IdxGroup();
		String[] lineTokens = line.split("/");
		int length = lineTokens.length;
		idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
		if (length > 1) {
			// It can be empty if the obj does not define text coords
			String textCoord = lineTokens[1];
			idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer
					.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
			if (length > 2) {
				idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
			}
		}
		return idxGroup;
	}

	public IdxGroup[] getFaceVertexIndices() {
		return idxGroups;
	}

}
