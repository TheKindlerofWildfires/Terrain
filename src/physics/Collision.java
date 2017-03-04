package physics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.*;

import input.KeyboardInput;
import maths.Vector4f;
import object.GameObject;

public class Collision {
	GameObject box1;
	GameObject box2; 
	Vector4f[] axis1;
	Vector4f[] axis2;
	/**
	 * Literally all this does is the math so I don't know what you want
	 * @param box1
	 * @param box2
	 */
	public Collision(GameObject box1, GameObject box2){
		axis1 = box1.getNorm();
		axis2 = box2.getNorm();
		this.box1 = box1;
		this.box2 = box2;
		box1.placeAt(-2,-2,-2);
		box2.placeAt(-1	,-1,-1);

	}
	protected boolean collide(){
		for (int i = 0; i < axis1.length; i++) {
			  Vector4f axis = axis1[i].normalize();
			  //System.out.println(axis);
			  float[] p1 = box1.project(axis);
			  float[] p2 = box2.project(axis);
			  // do the projections overlap?
			  if (!overlap2(p1, p2)) {
			    // then we can guarantee that the shapes do not overlap
			    return false;
			  }
			}
			// loop over the axes2
			for (int i = 0; i < axis2.length; i++) {
				Vector4f axis = axis2[i].normalize();
			  // project both shapes onto the axis
			  float[] p1 = box1.project(axis);
			  float[] p2 = box2.project(axis);
			  // do the projections overlap?
			  if (!overlap2(p1, p2)) {
			    // then we can guarantee that the shapes do not overlap
			    return false;
			  }
			}
			// if we get here then we know that every axis had overlap on it
			// so we can guarantee an intersection
			return true;
	}
	public void update() {
		if (KeyboardInput.isKeyPressed(GLFW_KEY_UP)||KeyboardInput.isKeyDown(GLFW_KEY_UP)) {
			box1.translate(0.05f, 0, 0);
			if(collide()){
				box1.translate(-0.05f, 0, 0);
			}
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_DOWN)||KeyboardInput.isKeyDown(GLFW_KEY_DOWN)) {
			box1.translate(-0.05f, 0, 0);
			if(collide()){
				box1.translate(0.05f, 0, 0);
			}
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_LEFT)||KeyboardInput.isKeyDown(GLFW_KEY_LEFT)) {
			box1.translate(0, 0.05f, 0);
			if(collide()){
				box1.translate(0, -0.05f, 0);
			}
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_RIGHT)||KeyboardInput.isKeyDown(GLFW_KEY_RIGHT)) {
			if(collide()){
				box1.translate(0, 0.05f, 0);
			}
			
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_Q)||KeyboardInput.isKeyDown(GLFW_KEY_Q)) {
			box1.translate(0, 0, 0.05f);
			if(collide()){
				box1.translate(0, 0, -0.05f);
			}
		}
		if (KeyboardInput.isKeyPressed(GLFW_KEY_E)||KeyboardInput.isKeyDown(GLFW_KEY_E)) {
			box1.translate(0, 0, -0.05f);
			if(collide()){
				box1.translate(0, 0, 0.05f);
			}
		}
		//if(Time.getUpdateTick()%60==0){System.out.println(collide());}
		
	}

	public void render(Vector4f renderClipPlane) {
		box1.render(renderClipPlane);
		box2.render(renderClipPlane);
		
	}
	public boolean overlap(float[] one, float[] two){
		if(one[0]<two[1] || two[0]<one[1]){ //I believe that this doesn't do so hot at all
			System.out.println(one[0] + " " +  one[1]);
			System.out.println(two[0] + " " +  two[1]);
			return true;
		}
		return false;
	}
	public boolean overlap2(float[] one, float[] two){
		if(KeyboardInput.isKeyDown(GLFW_KEY_R)){
			System.out.println(one[0] + " " +  one[1]);
			System.out.println(two[0] + " " +  two[1]);		
		}
		if(one[0]<two[1]&&one[0]>two[0]){
			return true;
		}
		if(one[1]>two[0]&&one[1]<two[1]){
			return true;
		}
		return false;
	}

}
