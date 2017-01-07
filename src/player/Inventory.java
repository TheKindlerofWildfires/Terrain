package player;

import java.util.ArrayList;

public class Inventory {
	//I suspect this will be a hashmap
	private ArrayList<Item> inside = new ArrayList<Item>();
	int maxSize;
	
	/**
	 * @param size
	 */
	public Inventory(int size){
		maxSize = size;
	}
	public void add(Item i){
		if(inside.size()>=maxSize){
			System.err.println("Your inventory is full!");
		}else{
			inside.add(i);
		}
	}
}
