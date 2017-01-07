package entity;

public class Life {
	public boolean isLiving = false;
	public float hp;
	/** 
	 * @param hp
	 */
	public Life(float hp){
		this.hp = hp;
		isLiving = true;
	}
	/**
	 * @param add, the amount of health to add, negative for damage.
	 */
	public void add(float add){
		this.hp += add;
		if(hp<= 0){
			isLiving = false;
		}
	}
	/**
	* And then there was death
	* @param cleanHp decides if the hp should be wiped or not
	*/
	public void kill(boolean cleanHp){
		isLiving = false;
		if(cleanHp){
			hp = 0;
		}
	}
}
