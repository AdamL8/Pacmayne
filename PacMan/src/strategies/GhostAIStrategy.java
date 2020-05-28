package strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;

//DESIGN PATTERN : Strategy
public abstract class GhostAIStrategy {
	private Random r = new Random();
	private int moveIndex = 0;
	private final int DIRECTION_CHANGE_INDEX = 200;
	private List<Direction> directions = new ArrayList<>();
	
	public GhostAIStrategy() {
		initDirections();
	}

	public synchronized Direction getDirection(Direction currentDirection, MessageEnum message) {
		if(moveIndex == DIRECTION_CHANGE_INDEX || (message != null && message.equals(MessageEnum.HIT_WALL))) {
			moveIndex = 0;
			return directions.get(r.nextInt(directions.size()));
		} else {
			moveIndex++;
			return currentDirection;
		}
	}
	
	protected synchronized void removeOppositeDirection(Direction direction) {
		switch(direction) {
		case UP:
			removeDirection(Direction.DOWN);
			break;
		case DOWN:
			removeDirection(Direction.UP);
			break;
		case LEFT:
			removeDirection(Direction.RIGHT);
			break;
		case RIGHT:
			removeDirection(Direction.LEFT);
			break;
		default:
			break;
			
		}
	}
	protected void moveNow() {
		this.moveIndex = DIRECTION_CHANGE_INDEX;
	}
	
	protected synchronized void removeDirection(Direction direction) {
		directions.clear();
		initDirections();
		directions.remove(direction);
	}
	
	protected synchronized void initDirections() {
		directions.add(Direction.UP);
		directions.add(Direction.DOWN);
		directions.add(Direction.LEFT);
		directions.add(Direction.RIGHT);
	}
	
	public abstract Strategy getStrategyEnum();

	
	// TODO: getEvadeDirection()
}
