package strategies;

import java.util.Random;
import java.util.Timer;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;
import utils.SyncTimerTask;

//DESIGN PATTERN : Strategy
public class GhostAIStupid extends GhostAIStrategy{
	
	private Random random = new Random();
	private boolean canMakeDecision = true;
	private boolean chasing = false;
	
	@Override
	public Direction getDirection(Direction direction, MessageEnum message) {
		if(message != null && message.equals(MessageEnum.PACMAN_SAW)) {
			if(canMakeDecision) {
				chaseTimer();
			}
			return super.getDirection(chasing ? direction : getOppositeDirection(direction), message);
		}
		return super.getDirection(direction, message);
	}
	
	private void chaseTimer() {
		canMakeDecision = false;
		Timer timer = new Timer();
		SyncTimerTask repeatedTask = new SyncTimerTask(() -> {canMakeDecision = true; chasing = !chasing;});
		repeatedTask.startRunning();
		timer.schedule(repeatedTask, (long) (2000));
	}

	@Override
	public Strategy getStrategyEnum() {
		return Strategy.STUPID;
	}
	
	private Direction getOppositeDirection(Direction direction) {
		switch(direction) {
		case UP:
			return Direction.DOWN;
		case DOWN:
			return Direction.UP;
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		default:
			return direction;
			
		}
	}

}
