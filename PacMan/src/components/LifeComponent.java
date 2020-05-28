package components;

public class LifeComponent implements IComponent {
	private int lives;
	public LifeComponent(int startingLives) {
		this.lives = startingLives;
	}
	
	public void removeLife(){
		--lives;
	}
	public void addLife(){
	    ++lives;
	 
	    
	}
	public int getLives() {
		return lives;
	}
	
	public boolean isDead() {
		return lives <= 0;
	}

}
