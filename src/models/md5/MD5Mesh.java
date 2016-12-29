package models.md5;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import maths.Vector2f;
import maths.Vector3f;

public class MD5Mesh {
	private static final Pattern PATTERN_SHADER = Pattern.compile("\\s*shader\\s*\\\"([^\\\"]+)\\\"");

	private static final Pattern PATTERN_VERTEX = Pattern.compile("\\s*vert\\s*(\\d+)\\s*\\(\\s*("
			+ MD5Utils.FLOAT_REGEXP + ")\\s*(" + MD5Utils.FLOAT_REGEXP + ")\\s*\\)\\s*(\\d+)\\s*(\\d+)");

	private static final Pattern PATTERN_TRI = Pattern.compile("\\s*tri\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)");

	private static final Pattern PATTERN_WEIGHT = Pattern.compile(
			"\\s*weight\\s*(\\d+)\\s*(\\d+)\\s*" + "(" + MD5Utils.FLOAT_REGEXP + ")\\s*" + MD5Utils.VECTOR3_REGEXP);

	private String texture;
	private List<MD5Vertex> vertices;
	private List<MD5Triangle> triangles;
	private List<MD5Weight> weights;

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("mesh [" + System.lineSeparator());
		str.append("texture: ").append(texture).append(System.lineSeparator());

		str.append("vertices [").append(System.lineSeparator());
		for (MD5Vertex vertex : vertices) {
			str.append(vertex).append(System.lineSeparator());
		}
		str.append("]").append(System.lineSeparator());

		str.append("triangles [").append(System.lineSeparator());
		for (MD5Triangle triangle : triangles) {
			str.append(triangle).append(System.lineSeparator());
		}
		str.append("]").append(System.lineSeparator());

		str.append("weights [").append(System.lineSeparator());
		for (MD5Weight weight : weights) {
			str.append(weight).append(System.lineSeparator());
		}
		str.append("]").append(System.lineSeparator());

		return str.toString();
	}

	public MD5Mesh(List<String> meshBlock) {
		vertices = new ArrayList<MD5Vertex>();
		triangles = new ArrayList<MD5Triangle>();
		weights = new ArrayList<MD5Weight>();

		for (String line : meshBlock) {
			if (line.contains("shader")) {
				Matcher textureMatcher = PATTERN_SHADER.matcher(line);
				if (textureMatcher.matches()) {
					this.texture = textureMatcher.group(1);
				}
			} else if (line.contains("vert")) {
				Matcher vertexMatcher = PATTERN_VERTEX.matcher(line);
				if (vertexMatcher.matches()) {
					MD5Vertex vertex = new MD5Vertex();
					vertex.index = Integer.parseInt(vertexMatcher.group(1));
					float x = Float.parseFloat(vertexMatcher.group(2));
					float y = Float.parseFloat(vertexMatcher.group(3));
					vertex.textCoords = new Vector2f(x, y);
					vertex.startWeight = Integer.parseInt(vertexMatcher.group(4));
					vertex.weightCount = Integer.parseInt(vertexMatcher.group(5));
					this.vertices.add(vertex);
				}
			} else if (line.contains("tri")) {
				Matcher triMatcher = PATTERN_TRI.matcher(line);
				if (triMatcher.matches()) {
					MD5Triangle triangle = new MD5Triangle();
					triangle.index = Integer.parseInt(triMatcher.group(1));
					triangle.vertex0 = Integer.parseInt(triMatcher.group(2));
					triangle.vertex1 = Integer.parseInt(triMatcher.group(3));
					triangle.vertex2 = Integer.parseInt(triMatcher.group(4));
					this.triangles.add(triangle);
				}
			} else if (line.contains("weight")) {
				Matcher weightMatcher = PATTERN_WEIGHT.matcher(line);
				if (weightMatcher.matches()) {
					MD5Weight weight = new MD5Weight();
					weight.index = Integer.parseInt(weightMatcher.group(1));
					weight.jointIndex = Integer.parseInt(weightMatcher.group(2));
					weight.bias = Float.parseFloat(weightMatcher.group(3));
					float x = Float.parseFloat(weightMatcher.group(4));
					float y = Float.parseFloat(weightMatcher.group(5));
					float z = Float.parseFloat(weightMatcher.group(6));
					weight.position = new Vector3f(x, y, z);
					this.weights.add(weight);
				}
			}
		}
	}

	public String getTexture() {
		return texture;
	}

	public List<MD5Vertex> getVertices() {
		return vertices;
	}

	public List<MD5Triangle> getTriangles() {
		return triangles;
	}

	public List<MD5Weight> getWeights() {
		return weights;
	}

	public static class MD5Vertex {

		int index;
		Vector2f textCoords;
		int startWeight;
		int weightCount;

		@Override
		public String toString() {
			return "[index: " + index + ", textCoods: " + textCoords + ", startWeight: " + startWeight
					+ ", weightCount: " + weightCount + "]";
		}
	}

	public static class MD5Triangle {

		int index;
		int vertex0;
		int vertex1;
		int vertex2;

		@Override
		public String toString() {
			return "[index: " + index + ", vertex0: " + vertex0 + ", vertex1: " + vertex1 + ", vertex2: " + vertex2
					+ "]";
		}
	}

	public static class MD5Weight {

		int index;
		int jointIndex;
		float bias;
		Vector3f position;

		@Override
		public String toString() {
			return "[index: " + index + ", jointIndex: " + jointIndex + ", bias: " + bias + ", position: " + position
					+ "]";
		}
	}

}
