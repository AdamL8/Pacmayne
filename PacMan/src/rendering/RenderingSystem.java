package rendering;

import static configs.GameConfig.GAME_WIDTH;
import static configs.GameConfig.HEIGTH_WINDOW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import configs.HighScoreReposity;
import entities.Score;
import gameThreads.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scenes.NameMenu;
import scenes.VerticalMenu;
import states.ControlMenuState;
import states.HighScoreNameState;
import states.HighScoresMenuState;
import states.InPlayGameState;
import states.MainMenuGameState;
import states.QuitMenuGameState;
import states.StateManager;

public class RenderingSystem extends Application {
	
	private static final Logger LOGGER = Logger.getLogger(RenderingSystem.class.getName());
	private Map<String, Parent> sceneRoots = new HashMap<>();
	private Stage primaryStage;

	public Game gameInstance = null;
	private Board board = null;
	private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private int firstIndex = 0;

	private HighScoreReposity highScoreReposity = new HighScoreReposity();
	private ArrayList<Score> highScores = new ArrayList<>();

	
	private Stage initStage(Stage stage, int width, int height) {
		try {
			primaryStage.setTitle("PacMan");

//			Scene scene = new Scene(root, width, height, Color.BLACK);
			Scene scene = new Scene(sceneRoots.get(MainMenuGameState.class.getName()), width, height, Color.BLACK);
			
			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> handleOnKeyPressed(event.getCode()));
			
			primaryStage.setScene(scene);
			primaryStage.setFullScreenExitHint("");
			primaryStage.setResizable(false);
			primaryStage.show();
			
			letterbox(scene, (Pane) sceneRoots.get(MainMenuGameState.class.getName()));
			
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		
		return primaryStage;
	}	
	
	private Game startGame(Stage stage) {
		board = new Board();
		sceneRoots.put(InPlayGameState.class.getName(), board);
		gameInstance = new Game((IBoardRenderer) sceneRoots.get(InPlayGameState.class.getName()));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

	         @Override
	         public void handle(WindowEvent event) {
	             Platform.runLater(new Runnable() {

	                 @Override
	                 public void run() {
	                	 gameInstance.stopThreads();
	                     System.exit(0);
	                 }
	             });
	         }
	     });
		
		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean > ov, Boolean t, Boolean t1) {
				gameInstance.setFocus(t1);
			}
		});
		
		primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean > ov, Boolean t, Boolean t1) {
				gameInstance.setInView(t);
			}
		});
		
		gameInstance.run();
		
		return gameInstance;
	}
	

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
		highScores = (ArrayList<Score>) highScoreReposity.getHighScores("ressource/text/highScoreRead.txt");
		createAllGameMenus();
		initStage(stage, GAME_WIDTH, HEIGTH_WINDOW);
	}
	
	private void handleOnKeyPressed(KeyCode key) {
		if (key == KeyCode.F) {			
			primaryStage.setFullScreen(!primaryStage.isFullScreen());
		} else {
			StateManager.handleUserInputs(key);
		}
	}
	
	private void createAllGameMenus() {
		VerticalMenu controlMenu = new VerticalMenu(500, 500);
		controlMenu.addMenuItem("P: PAUSE/UNPAUSE THE GAME", () -> {});
		controlMenu.addMenuItem("M: MUTE/UNMUTE THE GAME", () -> {});
		controlMenu.addMenuItem("F: TOGGLE FULLSCREEN", () -> {});
		controlMenu.addMenuItem("ESC: GAME MENU", () -> {});
		controlMenu.addMenuItem("GO BACK", () -> {StateManager.rollBackToLastState();
			primaryStage.getScene().setRoot(sceneRoots.get(StateManager.getCurrentState().getClass().getName()));});
		
		VerticalMenu highScoreMenu = new VerticalMenu(500, 500);
		
		VerticalMenu mainMenu = new VerticalMenu(500, 500);
		
		NameMenu nameMenu = new NameMenu(500, 500);
		
		nameMenu.addMenuItem("A", () -> {});
		nameMenu.addMenuItem("A", () -> {});
		nameMenu.addMenuItem("A", () -> {});
		nameMenu.addMenuItem("ENTER", () -> {
			StateManager.setCurrentState(new MainMenuGameState(mainMenu));
			primaryStage.getScene().setRoot(sceneRoots.get(MainMenuGameState.class.getName()));
			gameInstance.setTopScore(nameMenu.getName());
			gameInstance.stopGame();
		});
		

		VerticalMenu quitMenu = new VerticalMenu(500, 500);
		quitMenu.addMenuItem("DO YOU WANT TO QUIT?", () -> {});
		quitMenu.addMenuItem("YES", () -> {StateManager.setCurrentState(new MainMenuGameState(mainMenu));
		primaryStage.getScene().setRoot(sceneRoots.get(MainMenuGameState.class.getName())); gameInstance.stopGame();});
		quitMenu.addMenuItem("NO", () -> {
			StateManager.setCurrentState(new InPlayGameState(gameInstance, () -> {
				StateManager.setCurrentState(new QuitMenuGameState(quitMenu));
				primaryStage.getScene().setRoot(sceneRoots.get(QuitMenuGameState.class.getName()));
			}));
			primaryStage.getScene().setRoot(sceneRoots.get(InPlayGameState.class.getName()));
		});
		
		mainMenu.addMenuItem("PLAY", () -> {	
			Game myGame = startGame(primaryStage);
			StateManager.setCurrentState(new InPlayGameState(myGame, () -> {
				if(!myGame.isGameOver()) {
					StateManager.setCurrentState(new QuitMenuGameState(quitMenu));
					primaryStage.getScene().setRoot(sceneRoots.get(QuitMenuGameState.class.getName()));
				} else {
					StateManager.setCurrentState(new HighScoreNameState(nameMenu));
					primaryStage.getScene().setRoot(sceneRoots.get(HighScoreNameState.class.getName()));
				}
					
				}));
				primaryStage.getScene().setRoot(sceneRoots.get(InPlayGameState.class.getName()));
			});
		mainMenu.addMenuItem("CONTROLS", () -> {StateManager.setCurrentState(new ControlMenuState(controlMenu));
			primaryStage.getScene().setRoot(sceneRoots.get(ControlMenuState.class.getName()));});
		mainMenu.addMenuItem("HIGH SCORES", () -> {
			highScores = (ArrayList<Score>) highScoreReposity.getHighScores("ressource/text/highScoreRead.txt");
			for(Score score: highScores) {
				highScoreMenu.addMenuItem(score.getScore() + " " + score.getName(), () -> {});
			}
			highScoreMenu.addMenuItem("GO BACK", () -> {highScoreMenu.deleteMenuItem();StateManager.rollBackToLastState();
			primaryStage.getScene().setRoot(sceneRoots.get(StateManager.getCurrentState().getClass().getName()));});
			StateManager.setCurrentState(new HighScoresMenuState(highScoreMenu));
			primaryStage.getScene().setRoot(sceneRoots.get(HighScoresMenuState.class.getName()));});
		mainMenu.addMenuItem("EXIT", () -> System.exit(0));
		
		// TODO: Change key to enum or something
		sceneRoots.put(MainMenuGameState.class.getName(), mainMenu.getContent());
		sceneRoots.put(ControlMenuState.class.getName(), controlMenu.getContent());
		sceneRoots.put(HighScoresMenuState.class.getName(), highScoreMenu.getContent());
		sceneRoots.put(HighScoreNameState.class.getName(), nameMenu.getContent());
		sceneRoots.put(QuitMenuGameState.class.getName(), quitMenu.getContent());
		StateManager.setCurrentState(new MainMenuGameState(mainMenu));
		
	}
	
	private void letterbox(final Scene scene, final Pane contentPane) {
	    final double initWidth  = scene.getWidth();
	    final double initHeight = scene.getHeight();
	    final double ratio      = initWidth / initHeight;

	    SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
	    scene.widthProperty().addListener(sizeListener);
	    scene.heightProperty().addListener(sizeListener);
	  }

	  private static class SceneSizeChangeListener implements ChangeListener<Number> {
	    private final Scene scene;
	    private final double ratio;
	    private final double initHeight;
	    private final double initWidth;
	    private final Pane contentPane;

	    public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
	      this.scene = scene;
	      this.ratio = ratio;
	      this.initHeight = initHeight;
	      this.initWidth = initWidth;
	      this.contentPane = contentPane;
	    }

	    @Override
	    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
	      final double newWidth  = scene.getWidth();
	      final double newHeight = scene.getHeight();

	      double scaleFactor =
	          newWidth / newHeight > ratio
	              ? newHeight / initHeight
	              : newWidth / initWidth;

	      if (scaleFactor >= 1) {
	        Scale scale = new Scale(scaleFactor, scaleFactor);
	        scale.setPivotX(0);
	        scale.setPivotY(0);
	        scene.getRoot().getTransforms().setAll(scale);

	        contentPane.setPrefWidth (newWidth  / scaleFactor);
	        contentPane.setPrefHeight(newHeight / scaleFactor);
	      } else {
	        contentPane.setPrefWidth (Math.max(initWidth,  newWidth));
	        contentPane.setPrefHeight(Math.max(initHeight, newHeight));
	      }
	    }
	  }
}