package entity;

public abstract class Time {
	static int tickUpdate;
	static int tickSec;
	static int tickMin;
	static String timeState;
	static int days;

	public Time() {
		tickUpdate = 0;
		tickSec = 0;
		tickMin = 0;
		days = 0;
		timeState = "day";
	}

	public static void updateTick() {
		convertGameTime();
		days = (int)(getSecTick()+4)/16;
		tickUpdate++;
		if (tickUpdate % 60 == 0) {
			tickSec++; // 1 second
			if (tickSec % 60 == 0) {
				tickMin++; // 1 minute
			}
		}
	}

	public static int getUpdateTick() {
		return tickUpdate;
	}

	public static int getSecTick() {
		return tickSec;
	}

	public static int getMinTick() {
		return tickMin;
	}

	public static String getTimeState() {
		return timeState;
	}
	public static int getDay(){
		return days;
	}

	public static void convertGameTime() {
		// each tick in game time is equal to ~~1 second~~
		// this means that each sec is a minute, each min is an hour,
		// 24 minute days, 12 minute day night cycles
		// 20 sec days, 10 sec day night cycles
		if (getSecTick() % 17 == 9) {
			if (timeState == "day") {
				timeState = "night";
				
			} else {
				timeState = "day";
			}
		}
	}
}
