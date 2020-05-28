package rendering;

import java.util.List;

import entities.Entity;

public interface IBoardRenderer {

	void drawMaze(List<Sprite> sprites);
	
	void refreshScore(int score);
	
	void refreshLives(int lives);
	
	void spawnAnimatables(List<Sprite> sprites);
	
	void setPacManEntity(Entity pacman);
	
	boolean isRunning();
	
	void setRunning(boolean isRunning);

	void initLives(int lives);
	
	void displayPause();
	
	void hidePause();
	
	void pauseGame();
	
	void dispose();
	
	void refreshFps(int fps);

	void drawHeaderAndFooter();

	void refreshlevel(int level);

	boolean getBonusIsAdded();

	boolean isLevelPassed();

	void setLevelPassed(boolean levelPassed);

	void addSpritesMoving(List<Sprite> movingSprites);

	void Removesprites(List<Sprite> sprites);

	void addBonusLife();
}

