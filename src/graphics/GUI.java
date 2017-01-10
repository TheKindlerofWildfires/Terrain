package graphics;

	import object.GameObject;

	public class GUI extends GameObject {

	    private static final float ZPOS = 0.0f;

	    private static final int VERTICES_PER_QUAD = 4;

	    private String text;

	    private int numCols;
	    private int numRows;

	    public GUI(String texturePath, String modelPath, int numCols, int numRows) throws Exception {
	        super(modelPath, texturePath, true);
	        this.numCols = numCols;
	        this.numRows = numRows;
	    }
}
