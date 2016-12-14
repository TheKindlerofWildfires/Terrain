package entity;

import java.util.Random;

import graphics.Window;

public class Wander {
	Random random = Window.entityRandom;
	public Wander(){
		//gift wander mechanic for entity
	}
	public void move(){
		int r = random.nextInt(5);
		if(r==0){
			//stand still
		}else if(r==1){
			//move forward 
		}else if(r==2){
			//move back
		}else if(r==3){
			//move left
		}else if(r==4){
			//move right
		}else{
			//rotate!
		}
	}
	//bruh lets grab that danke collider code from player 
}
