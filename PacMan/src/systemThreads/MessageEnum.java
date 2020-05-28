package systemThreads;

public enum MessageEnum {
	COLLISION,
	DOWN,
	RIGHT,
	LEFT,
	UP,
	EATEN,
	INVINCIBLE_START,
	INVINCIBLE_END,
	BLINKING,
	KILLED,
	GUMPOINTS(10),
	SUPERGUMPOINTS(50),
	VOLUME_UP,
	VOLUME_DOWN,
	MUTE,
	GHOST200(200),
	GHOST400(400),
	GHOST800(800),
	GHOST1600(1600),
	HIT_WALL,
	PACMAN_PARALLELE,
	PACMAN_SAW;
	
	private int points = 0;
	
	MessageEnum() {}
	
	MessageEnum(int value) {
		this.points = value;
	}
	
	public int getPoints() {
		return points;
	}
}
