package entity;

public abstract class Time {
	static int tickUpdate;
	static int tickSec;
	static int tickMin;
	static String timeState;

	public Time() {
		tickUpdate = 0;
		tickSec = 0;
		tickMin = 0;
	}

	public static void updateTick() {
		convertGameTime();
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

	public static void convertGameTime() {
		// each tick in game time is equal to ~~1 second~~
		// this means that each sec is a minute, each min is an hour,
		// 24 minute days, 12 minute day night cycles
		if (getMinTick() % 24 == 12) {
			if (timeState == "day") {
				timeState = "night";
			} else {
				timeState = "day";
			}
		}
	}
}
