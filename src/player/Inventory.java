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
			i.setLocation("I" + inside.size());
		}
		if(inside.size()>maxSize){
			for(int j = maxSize; j<inside.size();j++){
				inside.remove(j);
			}
			
		}
	}
	public void activeItem() {
		try{
		inside.get(active).use();
		}
		catch( IndexOutOfBoundsException e){
			System.err.println("Your inventory is not filled here");
		}
				
		
	}
	public void scroll(double s) {
		active = (int) Math.abs(((active +s)%maxSize));
		
	}
	public void setActive(int i) {
		active = i;
		
	}
}
