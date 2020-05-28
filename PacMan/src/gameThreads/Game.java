package gameThreads;

import static configs.GameConfig.BLINKY_SPAWN_POINT_X;
import static configs.GameConfig.BLINKY_SPAWN_POINT_Y;
import static configs.GameConfig.CLYDE_SPAWN_POINT_X;
import static configs.GameConfig.CLYDE_SPAWN_POINT_Y;
import static configs.GameConfig.INKY_SPAWN_POINT_X;
import static configs.GameConfig.INKY_SPAWN_POINT_Y;
import static configs.GameConfig.PACMAN_SPAWN_POINT_X;
import static configs.GameConfig.PACMAN_SPAWN_POINT_Y;
import static configs.GameConfig.PINKY_SPAWN_POINT_X;
import static configs.GameConfig.PINKY_SPAWN_POINT_Y;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import components.AudioComponent;
import components.GraphicsComponent;
import components.LifeComponent;
import components.MoveComponent;
import components.PhysicsComponent;
import components.ScoreComponent;
import components.UserInputComponent;
import configs.HighScoreReposity;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import entities.Maze;
import entities.Score;
import factories.EntityFactory;
import factories.MazeFactory;
import javafx.animation.AnimationTimer;
import rendering.IBoardRenderer;
import rendering.Sprite;
import strategies.GhostAIAmbusher;
import strategies.GhostAIChaser;
import strategies.GhostAIRandom;
import strategies.GhostAIStupid;
import systemThreads.AISystem;
import systemThreads.GameAudioSystem;
import systemThreads.GraphicsSystem;
import systemThreads.InvincibleSystem;
import systemThreads.LifeSystem;
import systemThreads.MoveSystem;
import systemThreads.PhysicsSystem;
import systemThreads.ScoreSystem;
import systemThreads.UserInputSystem;

public class Game {

	private static final Logger LOGGER = Logger.getLogger( Game.class.getName() );
	private IBoardRenderer board;
	private EntityManager entityManager;
	private UserInputSystem userInputSystem;
	private MoveSystem moveSystem;
	private PhysicsSystem physicsSystem;
	private GraphicsSystem graphicsSystem;
	private GameAudioSystem audioSystem;
	private ScoreSystem scoreSystem;
	private AISystem aiSystem;
	private LifeSystem lifeSystem;
	private InvincibleSystem invincibleSystem;
	private Entity pacman;
	private Entity inky;
	private Entity blinky;
	private Entity pinky;
	private Entity clyde;
	private boolean isFocused = true;
	private boolean inView = true;
	private Thread physicsThread;
	private Thread audioThread;
	private Thread graphicThread;
	private int lives;
	private LifeComponent life;
	private int frameCounter = 0;
	int level=1;
	private volatile boolean isRunning = true;
	private Score finalScore;
	private HighScoreReposity highScore;
	private int topScore = 0;
	private boolean gameOver = false;
	Maze map;
	
	public Game(IBoardRenderer board)
	{
		this.board = board;
		this.init();
	}

	private void init()
	{
		entityManager = new EntityManager();
		
		try {
			map = MazeFactory.BuildMaze(entityManager);	
		
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		
		buildMaze();
		createMovableEntities();
		board.drawHeaderAndFooter();
		initSystems();
		initLives();
	    initLevel();
		highScore = new HighScoreReposity();
	}
	
	
	private void initLives() {
		life = (LifeComponent) entityManager.getComponentOfClass(LifeComponent.class.getName(), pacman);
		lives = life.getLives();
		board.initLives(lives);
	}
	private void initLevel() {
		board.refreshlevel(level);
	}
	
	private void buildMaze() {
		List<Sprite> sprites = new ArrayList<Sprite>();
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(GraphicsComponent.class.getName());
		
		for(Entity entity: entities) {	
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			sprites.add(graphic.getSprite());
		}	
		
		board.drawMaze(sprites);
	}
	private void removeallsprites() {
		List<Sprite> sprites = new ArrayList<Sprite>();
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(GraphicsComponent.class.getName());
		for (Entity entity : entities) {
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			sprites.add(graphic.getSprite());	
		}
		board.Removesprites(sprites);
		sprites.clear();
	}
	private void removeallspritesMoving() {
		List<Sprite> sprites = new ArrayList<Sprite>();
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class.getName());
		for (Entity entity : entities) {
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			sprites.add(graphic.getSprite());	
		}
		board.Removesprites(sprites);
	    sprites.clear();
	}
	private void AddAllspritesMoving() {
		List<Sprite> sprites = new ArrayList<Sprite>();
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class.getName());
		for (Entity entity : entities) {
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			sprites.add(graphic.getSprite());	
		}
		board.addSpritesMoving(sprites);
	    
	}

	public void NextLevel() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(GraphicsComponent.class.getName());
		
		for (Entity entity : entities) {
			PhysicsComponent physic = (PhysicsComponent) entityManager
					.getComponentOfClass(PhysicsComponent.class.getName(), entity);
			GraphicsComponent graphic = (GraphicsComponent) entityManager
					.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(),
					entity);
			AudioComponent audio = (AudioComponent) entityManager.getComponentOfClass(AudioComponent.class.getName(),
					entity);
			ScoreComponent scores = (ScoreComponent) entityManager.getComponentOfClass(ScoreComponent.class.getName(),
					entity);
			LifeComponent life = (LifeComponent) entityManager.getComponentOfClass(LifeComponent.class.getName(),
					entity);
			UserInputComponent user = (UserInputComponent) entityManager
					.getComponentOfClass(UserInputComponent.class.getName(), entity);
			if (graphic != null && physic != null && move == null && audio == null && scores == null && life == null && user == null) {
				return;
			}
		}
		
      if(!board.isLevelPassed()) {
		board.refreshlevel(++level);
		board.setLevelPassed(false);
		removeallsprites();
		try {
			map = MazeFactory.BuildMaze(entityManager);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		buildMaze();
		removeallspritesMoving();
		AddAllspritesMoving();
	    moveSystem.respawn();		
      }
	}
	
	private void initSystems() {
		userInputSystem = new UserInputSystem(entityManager);
		moveSystem = new MoveSystem(entityManager, map, pacman);
		physicsSystem = new PhysicsSystem(entityManager, pacman, inky, blinky, pinky, clyde);
		graphicsSystem = new GraphicsSystem(entityManager, pacman);
		scoreSystem = new ScoreSystem(entityManager);
		aiSystem = new AISystem(entityManager, pacman, map);
		lifeSystem = new LifeSystem(entityManager);
		invincibleSystem = new InvincibleSystem(entityManager, pacman, inky, blinky, pinky, clyde);
		audioSystem = new GameAudioSystem(entityManager);
	}
	
	private void createMovableEntities() {
		List<Sprite> sprites = new ArrayList<Sprite>();
		EntityFactory factory = new EntityFactory(entityManager);
		pacman = factory.createPacMan(PACMAN_SPAWN_POINT_X, PACMAN_SPAWN_POINT_Y, Direction.RIGHT);
		clyde = factory.createGhost(CLYDE_SPAWN_POINT_X, CLYDE_SPAWN_POINT_Y, Direction.UP, "clyde", new GhostAIStupid());
		blinky = factory.createGhost(BLINKY_SPAWN_POINT_X, BLINKY_SPAWN_POINT_Y, Direction.UP, "blinky", new GhostAIChaser());
		inky = factory.createGhost(INKY_SPAWN_POINT_X, INKY_SPAWN_POINT_Y, Direction.UP, "inky", new GhostAIRandom());
		pinky = factory.createGhost(PINKY_SPAWN_POINT_X, PINKY_SPAWN_POINT_Y, Direction.UP, "pinky", new GhostAIAmbusher());
		board.setPacManEntity(pacman);
		
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class.getName());
		
		for(Entity entity: entities) {
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			
			if (graphic == null) {
				continue;
			}
			
			sprites.add(graphic.getSprite());			
		}
		
		board.spawnAnimatables(sprites);
	}

	public void run()
	{
		physicsThread = new Thread(physicsSystem);
		physicsThread.start();
		audioThread = new Thread(audioSystem);
		audioThread.start();
		graphicThread = new Thread(graphicsSystem);
		graphicThread.start();
		
		new AnimationTimer()
        {
			long lastUpdate = System.nanoTime();
			long firstTime = lastUpdate;
            public void handle(long now)
            { 
    			if(!isRunning) {
    				this.stop();
    				return;
    			}
            	if (now - lastUpdate >= 30_000_000) {
            		lastUpdate = System.nanoTime();
            		++frameCounter;
            		update();  
            		render();
            	}
            	if((now - firstTime) >= 1000000000) {
            		board.refreshFps(frameCounter);
            		frameCounter = 0;
            		firstTime = now;
            	}
            }
        }.start();
	}
	
	private void update() {
		if(board.isRunning() && isFocused && inView) {
			userInputSystem.update();
			moveSystem.update();
			aiSystem.update();
			lifeSystem.update();
			scoreSystem.update();
			invincibleSystem.update();
		}			
	}
	
	private void render() {
		ScoreComponent score = (ScoreComponent) entityManager.getComponentOfClass(ScoreComponent.class.getName(), pacman);
		renderLives();
		NextLevel();
		if (score != null) {
			board.refreshScore(score.getScore());
			if (score.getScore() >= 10000 && !board.getBonusIsAdded()) {
				life.addLife();
				lives = life.getLives();
				board.addBonusLife();
			}
			}
		if(!isFocused||!inView ) { 
		}
		if(life.getLives() == 0) {
			gameOver = true;
			topScore = score != null ? score.getScore() : 0;
		}
		if(!isFocused || !inView) { 
			board.displayPause();
		} else {
			board.hidePause();
		}
	}
	
	private void renderLives() {
		if(life.getLives() != lives) {
			lives = life.getLives();
			board.refreshLives(life.getLives());
			if (life.getLives() > 0) {
				moveSystem.respawn();
			} else {
			   stopThreads();
		   }
		}
	}
	
	public void stopThreads() {
		physicsSystem.stopThread();
		audioSystem.stopThread();
		graphicsSystem.stopThread();
		try {
			physicsThread.join(33);
			audioThread.join(33);
			graphicThread.join(33);
			if (physicsThread.isAlive()){
				physicsThread.interrupt();
			}
			if (audioThread.isAlive()){
				audioThread.interrupt();
			}
			if (graphicThread.isAlive()){
				graphicThread.interrupt();
			}
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			physicsThread.interrupt();
			audioThread.interrupt();
			graphicThread.interrupt();
		}
		
	}
	
	public void setFocus(boolean focus) {
		isFocused = focus;
	}
	
	public void setInView(boolean inView) {
		this.inView = inView;
	}
	
	public IBoardRenderer getBoard() {
		return board;
	}

	public Entity getPacman() {
		return pacman;
	}
	
	public void stopGame() {
		isRunning = false;
		gameOver = true;
		stopThreads();
		entityManager.dispose();
		board.dispose();
		
	}
	
	public HighScoreReposity getHighScore() {
		return highScore;
	}

	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setTopScore(String name) {
		finalScore = new Score(topScore, name); 
		highScore.replaceHighScore(finalScore);
	}
}
