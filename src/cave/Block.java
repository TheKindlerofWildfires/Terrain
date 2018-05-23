package cave;

import models.ModelManager;
import object.GameObject;

import java.io.IOException;

public class Block extends GameObject{
	byte sides;
	public Block(String modelPath, String texturePath, boolean isGL, byte sides){
		super(modelPath,texturePath,isGL);
		this.sides = sides;
		
	}
	public void setModel(){
		//Steal simon's code for deciding which block to use from the byte
		String modelPath = "none";
		try {
			vao = ModelManager.loadGlModel(modelPath).vao;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
