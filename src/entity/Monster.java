package entity;

import static graphics.Shader.start;
import static graphics.Shader.stop;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;

import maths.Vector3f;
import maths.Vector4f;
import object.GameObject;
//Kindler your lighting is so terrible
public class Monster extends GameObject{
	
	//This is pretty stellar :P
	public ArrayList<GameObject> satellites = new ArrayList<GameObject>();
	
	public Monster(String body, Vector3f position){
		super(body, "none", true);
		this.material.colour = new Vector3f(1,1,0);
		rotate(90,1,0,0);
		construct();
		setPosition(position);
		resting = true;	
	}
	/**
	 * This builds the thing
	 */
	public void construct() {
		
	}
	public void update() {
		satellites.stream().forEach(o -> o.physic());
		physic(); 
		
	}
	@Override
	public void render(Vector4f clipPlane){
		satellites.stream().forEach(o -> o.render(clipPlane));
		start(shader);
		renderPrep(clipPlane);
		glBindVertexArray(vao.getVaoID());
		glDrawArrays(GL_TRIANGLES, 0, vao.getSize());
		stop();
	}
	public void setAcceleration(Vector3f acceleration){
		this.acceleration = acceleration;
		satellites.stream().forEach(o -> o.acceleration = acceleration);
	}
	public void addAcceleration(Vector3f acceleration){
		this.acceleration = this.acceleration.add(acceleration);
		satellites.stream().forEach(o -> o.acceleration = o.acceleration.add(acceleration));
	}
	public void setVelocity(Vector3f velocity){
		this.velocity = velocity;
		satellites.stream().forEach(o -> o.velocity = velocity);
	}
	public void addVelocity(Vector3f velocity){
		this.velocity = this.velocity.add(velocity);
		satellites.stream().forEach(o -> o.velocity = o.velocity.add(velocity));
	}
	public void setPosition(Vector3f position){
		placeAt(position);
		satellites.stream().forEach(o -> o.placeAt(position.add(o.position)));
	}
	public void setScale(Vector3f scale){
		scale(scale);
		satellites.stream().forEach(o -> o.scale(scale));
	}
}
