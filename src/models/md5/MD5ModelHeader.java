package models.md5;

import java.io.IOException;
import java.util.List;

public class MD5ModelHeader {

	private String version;
	private String commandLine;
	private int numJoints;
	private int numMeshes;

	public String getVersion() {
		return version;
	}

	public String getCommandLine() {
		return commandLine;
	}

	public int getNumJoints() {
		return numJoints;
	}

	public int getNumMeshes() {
		return numMeshes;
	}

	@Override
	public String toString() {
		return "[version: " + version + ", commandLine: " + commandLine + ", numJoints: " + numJoints + ", numMeshes: "
				+ numMeshes + "]";
	}

	/**
	 * Parses MD5ModelHeader from header block
	 * 
	 * @param lines
	 *            header block
	 * @throws IOException
	 *             if you try to parse an empty file
	 */
	public MD5ModelHeader(List<String> lines) throws IOException {
		int numLines = lines != null ? lines.size() : 0;
		if (numLines == 0) {
			throw new IOException("Cannot parse empty file");
		}

		boolean finishHeader = false;
		for (int i = 0; i < numLines && !finishHeader; i++) {
			String line = lines.get(i);
			String[] tokens = line.split("\\s+");
			int numTokens = tokens != null ? tokens.length : 0;
			if (numTokens > 1) {
				String paramName = tokens[0];
				String paramValue = tokens[1];

				switch (paramName) {
				case "MD5Version":
					this.version = paramValue;
					break;
				case "commandline":
					this.commandLine = paramValue;
					break;
				case "numJoints":
					this.numJoints = Integer.parseInt(paramValue);
					break;
				case "numMeshes":
					this.numMeshes = Integer.parseInt(paramValue);
					break;
				case "joints":
					finishHeader = true;
					break;
				}
			}
		}
	}
}
