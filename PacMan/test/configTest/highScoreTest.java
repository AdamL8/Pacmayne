package configTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import configs.HighScoreReposity;
import entities.Score;

class highScoreTest {

	
	
	HighScoreReposity highScore = new HighScoreReposity();
	ArrayList<Score> scoresRead = new ArrayList<>();
	ArrayList<Score> scoresWrite = new ArrayList<>();
	Score score = new Score(100, "FLO");
	
	@BeforeEach
	void setUp() {
		scoresRead = new ArrayList<>(); 
		scoresWrite = new ArrayList<>(); 
	}
	
	@Test
	void readFileTest() {
		scoresRead = (ArrayList<Score>) highScore.getHighScores("ressource/text/highScoreRead.txt");
		assertEquals(scoresRead.size(), 5);
	}
	
	@Test
	void writeFileTest() {
		scoresWrite.add(score);
		highScore.writeHighScore("ressource/text/highScoreWrite.txt", scoresWrite);
		scoresWrite = (ArrayList<Score>) highScore.getHighScores("ressource/text/highScoreWrite.txt");
		assertEquals(scoresWrite.size(), 1);
	}
	
	@Test
	void replaceHighScoreTest() {
		Score score = new Score(500, "FLO");
		scoresRead = (ArrayList<Score>) highScore.getHighScores("ressource/text/highScoreRead.txt");
		assertEquals(scoresRead.size(), 5);
		highScore.replaceHighScore(score);
		scoresRead = (ArrayList<Score>) highScore.getHighScores("ressource/text/highScoreRead.txt");
		assertEquals(scoresRead.size(), 5);
	}
	

}
