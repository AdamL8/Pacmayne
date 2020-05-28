package rendering;

import static configs.GameConfig.GAME_WIDTH;
import static configs.GameConfig.HEIGTH_FOOTER;
import static configs.GameConfig.HEIGTH_HEADER;
import static configs.GameConfig.SIZE_IMG_LOGO;
import static configs.GameConfig.TILE_SIZE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Entity;
import entities.LivesImages;
import image.FontRepository;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;;

public class Board extends BorderPane implements IBoardRenderer{
	private static Logger logger = Logger.getAnonymousLogger();
	private boolean isRunning = true;
	private FontRepository fontRepository = new FontRepository();
	@FXML private ImageView imglogo ;
	private	int life;
	private Pane paneFooter= new Pane();
	private Pane paneHeader =new Pane();
	private Pane pane =new Pane();
	private Pane ScorePane= new Pane();
	private Pane livePane =new Pane();
	private char[] pause = {'p', 'a', 'u', 's', 'e'};
	private ArrayList<Sprite> spritesPause;
	private ArrayList<Sprite> spritesScore;
	private Entity pacman;
	private char[] textScore = {'s', 'c', 'o', 'r', 'e'};
	private char[] gameOver = {'g', 'a', 'm', 'e', ' ', 'o','v','e','r'};
	private char[] textFps = {'f', 'p', 's'};
	private ArrayList<Sprite> spritesfps;
	private char[] textLevel = {'l', 'e', 'v', 'e', 'l'};
	private ArrayList<Sprite> spritesLevel;
	private ArrayList<Sprite> spritesGameOver;
	private ArrayList<Sprite> spritesTextScore;
	private ArrayList<Sprite> spritesNumScore;
	private LivesImages imagelives;
	private boolean isPaused = false;
	private boolean  BonusIsAdded= false;
	private boolean LevelPassed = false;

	public Board()
	{	
		pane.setStyle("-fx-background-color: black;");	
		spritesfps = createWords(textFps, 10, 48, paneHeader);
		spritesLevel = createWords(textLevel,170, 0 ,  paneFooter );
	}	

	public void drawMaze(List<Sprite> sprites) 
	{
		pane.getChildren().clear();
	    if(pane.getChildren().isEmpty()) {
		this.setCenter(pane);
		spritesPause = createWords(pause, 11*TILE_SIZE + TILE_SIZE/2, 17*TILE_SIZE, pane);
		hideSprites(spritesPause);
		pane.getChildren().addAll(sprites);	
		}
		
	}
	public void drawHeaderAndFooter() {
		footer();
		header();	
	}
	
	public void dispose() {
		this.getChildren().clear();
	}
	public void Removesprites(List<Sprite> sprites) {
		pane.getChildren().removeAll(sprites);
	}
	
	public void RemovespritesMoving(List<Sprite> movingSprites){   
	  if(!pane.getChildren().isEmpty()){
       pane.getChildren().removeAll(movingSprites);
	}
	}
	@Override
	public void addSpritesMoving (List<Sprite> movingSprites){
		if(!pane.getChildren().isEmpty()){
		       pane.getChildren().addAll(movingSprites);
			}
	}
	
	private void header() {
		Image image = new Image("file:ressource/sprites/logo.png");
		imglogo = new ImageView();
		imglogo.setImage(image);
		imglogo.setFitHeight(SIZE_IMG_LOGO);
		imglogo.setFitWidth(GAME_WIDTH);
		paneHeader.getChildren().add(imglogo);
		paneHeader.setStyle("-fx-background-color: black;");
		paneHeader.setPrefSize(GAME_WIDTH,HEIGTH_HEADER);   
		this.setTop(paneHeader);
	}
	
	private void footer() {
		spritesTextScore= createWords(textScore, 350, 0, ScorePane);
		paneFooter.getChildren().add(ScorePane);
		paneFooter.getChildren().add(livePane);
		paneFooter.setStyle("-fx-background-color: black;");
		paneFooter.setPrefSize(GAME_WIDTH,HEIGTH_FOOTER);
		this.setBottom(paneFooter);
	}

	public void refreshScore(int score) {
		int[] number = Integer.toString(score).chars().map(c -> c-'0').toArray();
		spritesScore = CreateScore(number,475,0, ScorePane);
	}

	public void spawnAnimatables(List<Sprite> movingSprites)
	{		
		pane.getChildren().addAll(movingSprites);
	}

	public void setPacManEntity(Entity pacman) {
		this.pacman = pacman;
	}
	
	@Override
	public void pauseGame() {
		if(imagelives.getNblives() > 0) {
			isRunning = !isRunning;
			if(isRunning) {
				hideSprites(spritesPause);
			} else {
				displaySprites(spritesPause);
			}
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}	

	private ArrayList<Sprite> CreateScore(int[] Number,int x, int y, Pane myPane) {

		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		for(int myScore: Number) { 
			try {
				Sprite num = new Sprite(fontRepository.getFont(myScore), x , y, false); //12*TILE_SIZE, 17*TILE_SIZE
				x += TILE_SIZE;
				sprites.add(num);
				myPane.getChildren().add(num);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return sprites;
	}

	private ArrayList<Sprite> createWords(char[] letters, int x, int y, Pane myPane) {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		for(char myLetter: letters) {
			try {
				if(myLetter != ' ') {
					Sprite letter = new Sprite(fontRepository.getFont(myLetter), x , y, false); //12*TILE_SIZE, 17*TILE_SIZE
					x += TILE_SIZE;
					sprites.add(letter);
					myPane.getChildren().add(letter);
				} else {
					x += TILE_SIZE;
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return sprites;
	}

	private void hideSprites(ArrayList<Sprite> sprites) {
		for(Sprite sprite : sprites) {
			sprite.setVisible(false);
		}
	}

	private void displaySprites(ArrayList<Sprite> sprites) {
		for(Sprite sprite : sprites) {
			sprite.setVisible(true);
		}
	}

	@Override
	public void initLives(int lives) {
		this.life = lives;
		imagelives = new LivesImages(livePane, this.life);
		System.out.println("lives :"+lives);
	}

	@Override
	public void refreshLives(int lives) {
	if (lives == 0 && isRunning == true  ) {
					this.setRunning(false);
					imagelives.removeLife();
					spritesGameOver = createWords(gameOver, 9 * TILE_SIZE + TILE_SIZE / 2, 17 * TILE_SIZE, pane);
					displaySprites(spritesGameOver);
			 }
		   else {
				imagelives.removeLife();
				 System.out.println("remove");
			}
	
	}
	@Override
	public void addBonusLife() {
		if(!BonusIsAdded)
		imagelives.addLife();
        setBonusIsAdded(true);
	}
	
	@Override
	public void displayPause() {
		if(!isPaused) {
			displaySprites(spritesPause);
			isPaused = true;
		}
	}
	@Override
	public void hidePause() {
		if(isPaused) {
			hideSprites(spritesPause);
			isPaused = false;
		}
	}

	@Override
	public void refreshFps(int fps) {
		int []  numFPS =Integer.toString(fps).chars().map(c -> c-'0').toArray();
		spritesNumScore = CreateScore(numFPS,100,48,paneHeader);
		
	}
	@Override
	public void refreshlevel(int level) {
		int []  numLevel =Integer.toString(level).chars().map(c -> c-'0').toArray();
		spritesNumScore=CreateScore(numLevel,280,0, ScorePane);
	}
	@Override
	public boolean getBonusIsAdded() {
		return BonusIsAdded;
	}
	public void setBonusIsAdded(boolean bonusIsAdded) {
		BonusIsAdded = bonusIsAdded;
	}
		
	@Override 
	public boolean isLevelPassed() {
		return LevelPassed;
	}
   @Override
	public void setLevelPassed(boolean levelPassed) {
		LevelPassed = levelPassed;
	}
}
