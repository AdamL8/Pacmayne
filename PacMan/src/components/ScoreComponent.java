package components;

public class ScoreComponent implements IComponent{
	
	private int score = 0;

	public int addScore(int score) {
		return this.score += score;
	}

	public int getScore() {
		return score;
	}
}
