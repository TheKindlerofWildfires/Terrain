package md5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MD5Model {

	private MD5JointInfo jointInfo;
	private MD5ModelHeader header;
	private List<MD5Mesh> meshes;

	public MD5JointInfo getJointInfo() {
		return jointInfo;
	}

	public List<MD5Mesh> getMeshes() {
		return meshes;
	}

	public MD5ModelHeader getHeader() {
		return header;
	}

	public MD5Model(String fileName) throws IOException {
		meshes = new ArrayList<MD5Mesh>();

		Path path = Paths.get(fileName);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		int numLines = lines != null ? lines.size() : 0;

		if (numLines == 0) {
			throw new IOException("File is empty");
		}

		//parse header
		boolean headerEnd = false;
		int start = 0;
		for (int i = 0; i < numLines && !headerEnd; i++) {
			String line = lines.get(i);
			headerEnd = line.trim().endsWith("{");
			start = i;
		}
		if (!headerEnd) {
			throw new IOException("Cannot find header");
		}
		List<String> headerBlock = lines.subList(0, start);
		this.header = new MD5ModelHeader(headerBlock);

		//parse rest of file
		int blockStart = 0;
		boolean inBlock = false;
		String blockId = "";
		for (int i = start; i < numLines; i++) {
			String line = lines.get(i);
			if (line.endsWith("{")) { //we enter a block
				blockStart = i;
				blockId = line.substring(0, line.lastIndexOf(" "));
				inBlock = true;
			} else if (inBlock && line.endsWith("}")) { //we leave a block
				List<String> blockBody = lines.subList(blockStart + 1, i);
				parseBlock(blockId, blockBody);
				inBlock = false;
			}
		}
	}

	private void parseBlock(String blockId, List<String> blockBody) throws IOException {
		switch (blockId) {
		case "joints":
			this.jointInfo = new MD5JointInfo(blockBody);
			break;
		case "mesh":
			this.meshes.add(new MD5Mesh(blockBody));
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(new MD5Model("src/models/cube.md5mesh").meshes.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
