package models.md5;

import maths.Matrix4f;
import maths.Vector2f;
import maths.Vector3f;
import maths.Vector4f;
import models.VertexArrayObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MD5Loader {

	private MD5Loader() {
	}

	/**
	 * Loads the index'th mesh out of the md5 mesh file in fileName
	 * 
	 * @param fileName
	 *            the path to the md5mesh file
	 * @param index
	 *            the index of the mesh to be loaded. usually 0
	 * @return a VertexArrayObject with all the necessary info abt the mesh so
	 *         it can be rendered
	 */
	public static VertexArrayObject loadMesh(String fileName, int index) {
		MD5Model model = null;
		try {
			model = new MD5Model(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generateMesh(model, model.getMeshes().get(index));
	}

	/**
	 * Parses MD5Mesh into a VAO
	 * 
	 * @param model
	 *            the MD5Model
	 * @param mesh
	 *            the MD5Mesh
	 * @return the VAO corresponding to the MD5Mesh
	 */
	private static VertexArrayObject generateMesh(MD5Model model, MD5Mesh mesh) {
		List<Vector3f> positions = new ArrayList<Vector3f>();
		List<Vector2f> textCoords = new ArrayList<Vector2f>();

		List<MD5Mesh.MD5Vertex> vertices = mesh.getVertices();
		List<MD5Mesh.MD5Weight> weights = mesh.getWeights();
		List<MD5JointInfo.MD5JointData> joints = model.getJointInfo().getJoints();

		for (MD5Mesh.MD5Vertex vertex : vertices) {
			Vector3f vertexPos = new Vector3f();
			Vector2f vertexTextCoords = vertex.textCoords;
			textCoords.add(vertexTextCoords);

			int startWeight = vertex.startWeight;
			int numWeights = vertex.weightCount;

			for (int i = startWeight; i < startWeight + numWeights; i++) {
				MD5Mesh.MD5Weight weight = weights.get(i);
				MD5JointInfo.MD5JointData joint = joints.get(weight.jointIndex);
				Vector4f rot = joint.getOrientation();
				Vector3f rotatedPos = new Vector3f(
						Matrix4f.rotate(rot.x, rot.y, rot.z, rot.w).multiply(new Vector4f(weight.position, 1)));
				Vector3f acumPos = joint.position.add(rotatedPos);
				acumPos = acumPos.scale(weight.bias);
				vertexPos = vertexPos.add(acumPos);
			}
			positions.add(vertexPos);
			System.out.println(vertexPos);
		}

		ArrayList<Vector3f> vaoData = new ArrayList<Vector3f>();

		for (MD5Mesh.MD5Triangle tri : mesh.getTriangles()) {
			Vector3f pos0 = positions.get(tri.vertex0);
			Vector3f pos1 = positions.get(tri.vertex1);
			Vector3f pos2 = positions.get(tri.vertex2);

			Vector3f normal = (pos2.subtract(pos0)).cross(pos1.subtract(pos0));
			normal = normal.normalize();

			vaoData.add(pos0);
			vaoData.add(normal);
			vaoData.add(new Vector3f(textCoords.get(tri.vertex0), 0));

			vaoData.add(pos1);
			vaoData.add(normal);
			vaoData.add(new Vector3f(textCoords.get(tri.vertex1), 0));

			vaoData.add(pos2);
			vaoData.add(normal);
			vaoData.add(new Vector3f(textCoords.get(tri.vertex2), 0));
		}

		float[] vao = new float[vaoData.size() * 3];
		int c = 0;
		for (int i = 0; i < vaoData.size(); i++) {
			vao[c++] = vaoData.get(i).x;
			vao[c++] = vaoData.get(i).y;
			vao[c++] = vaoData.get(i).z;
		}
		return new VertexArrayObject(vao, 3);
	}
}
