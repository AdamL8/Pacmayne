package states;

import components.UserInputComponent;
import entities.Entity;
import gameThreads.Game;
import javafx.scene.input.KeyCode;
import systemThreads.MessageEnum;
import systemThreads.MessageQueue;

//DESIGN PATTERN : State
public class InPlayGameState implements IState{

	private Game game;
	private Runnable r;
	private static boolean isRunning = true;
	
	
	public InPlayGameState(Game game, Runnable r) {
		this.game = game;
		this.r = r;
	}

	@Override
	public void handleInput(KeyCode key) {
		if(key == KeyCode.P) {
			game.getBoard().pauseGame();
			isRunning = !isRunning;				
		}

		if(isRunning) {
			Entity pacman = game.getPacman();
			switch(key) {
			case UP:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.UP);
				break;
			case DOWN:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.DOWN);
				break;
			case LEFT:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.LEFT);
				break;
			case RIGHT:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.RIGHT);
				break;
			case MINUS:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.VOLUME_DOWN);
				break;
			case PLUS:
			case EQUALS:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.VOLUME_UP);
				break;
			case M:
				MessageQueue.addMessage(pacman, UserInputComponent.class.getName(), MessageEnum.MUTE);
				break;
			case ESCAPE:				
				r.run();
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnter() {
		if (!isRunning) {
			if (!game.getBoard().isRunning()) {
				game.getBoard().pauseGame();
			}
			isRunning = !isRunning;	
		}		
	}

	@Override
	public void onExit() {
		if (isRunning) {
			game.getBoard().pauseGame();
			isRunning = !isRunning;	
		}		
	}
}
