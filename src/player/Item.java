package player;

import physics.Time;

public class Item {
	public String tag;
	public String location; // I for inventory, B for bar, A-D for crafting. F
							// for crafting return
	public int cd;
	public int lastUsed;
	public int charge;

	public Item(String tag, int cd, int charge, String location) {
		this.location = location;
		this.tag = tag;
		this.cd = cd;
		lastUsed = Time.getUpdateTick();
	}

	public void use() {
		if (cd != 0) {
			if (Time.getUpdateTick() - lastUsed > cd) {
				lastUsed = Time.getUpdateTick();
				activate();
			}
		}
	}

	public void activate() {
		System.out.println("hi");

	}

	public void setLocation(String string) {
		this.location = string;
	}
}
