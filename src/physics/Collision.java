package physics;

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
		box2.placeAt(-1,-1,-1);

	}
	protected boolean collide(){
		for (int i = 0; i < axis1.length; i++) {
			  Vector4f axis = axis1[i].normalize();
			  //System.out.println(axis);
			  float[] p1 = box1.project(axis);
			  float[] p2 = box2.project(axis);
			  // do the projections overlap?
			  if (!overlap(p1, p2)) {
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
			  if (!overlap(p1, p2)) {
			    // then we can guarantee that the shapes do not overlap
			    return false;
			  }
			}
			// if we get here then we know that every axis had overlap on it
			// so we can guarantee an intersection
			return true;
	}
	public void update() {
		box1.translate(0.005f, 0, 0);
		if(Time.getUpdateTick()%60==0){System.out.println(collide());}
		//box2.physic();
		
	}

	public void render(Vector4f renderClipPlane) {
		box1.render(renderClipPlane);
		box2.render(renderClipPlane);
		
	}
	public boolean overlap(float[] one, float[] two){
		if(one[0]<two[1] || two[0]>one[1]){ 
			return true;
		}
		return false;
	}

}
