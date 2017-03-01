package physics;

import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;

public class Collision {
	GameObject box1 = new GameObject("resources/models/box.obj", "resources/textures/uvlayout.png", true);
	GameObject box2 = new GameObject("resources/models/box.obj", "resources/textures/uvlayout.png", true);
	Vector3f[] axis1 = box1.getNormals();
	Vector3f[] axis2 = box2.getNormals();
	public Collision(){
		box1.placeAt(2,2, 2);
		box2.placeAt(1, 1, 1);
		box1.resting = true;
		box2.resting = false;
		System.out.println(collide());
	}
	protected boolean collide(){
		for (int i = 0; i < axis1.length; i++) {
			  Vector3f axis = axis1[i].normalize();
			  System.out.println(axis);
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
				Vector3f axis = axis2[i].normalize();
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
		//box1.physic();
		//box2.physic();
		
	}

	public void render(Vector4f renderClipPlane) {
		box1.render(renderClipPlane);
		box2.render(renderClipPlane);
		
	}
	public boolean overlap(float[] one, float[] two){
		if(one[0]<two[1] || two[0]<one[1]){ //deals with like 99% of cases
			return true;
		}
		return false;
	}

}
