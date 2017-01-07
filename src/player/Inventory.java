package player;

import java.util.ArrayList;

public class Inventory {
	//I suspect this will be a hashmap
	private ArrayList<Item> inside = new ArrayList<Item>();
	int maxSize;
	int active;
	
	/**
	 * @param size
	 */
	public Inventory(int size){
		maxSize = size;
		active = 0;
	}
	public void add(Item i){
		if(inside.size()>=maxSize){
			System.err.println("Your inventory is full!");
		}else{
			inside.add(i);
		}
	}
	public void activeItem() {
		//inside.get(active).use();
		System.out.println(active);
		
	}
	public void scroll(double s) {
		active = (int) Math.abs(((active +s)%maxSize));
		
	}
}
