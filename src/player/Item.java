package player;

import physics.Time;

public class Item {
	public String tag;
	public int cd;
	public int lastUsed;
	public Item(String tag, int cd){
		this.tag = tag;
		this.cd = cd;
		lastUsed = Time.getUpdateTick();
	}
	public void use(){
		if(Time.getUpdateTick()-lastUsed>cd){
			lastUsed = Time.getUpdateTick();
			activate();
		}
	}
	private void activate() {
		System.out.println("hi");
		
	}
}
