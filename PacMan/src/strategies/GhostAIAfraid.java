package strategies;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;

public class GhostAIAfraid extends GhostAIStrategy {
	
	@Override
	public Direction getDirection(Direction direction, MessageEnum message) {
		if(message != null && message.equals(MessageEnum.PACMAN_SAW)) {
			super.removeDirection(direction);
			super.moveNow();
		} else {
			super.initDirections();
		}
		return super.getDirection(direction, message);
	}

	@Override
	public Strategy getStrategyEnum() {
		return Strategy.AFRAID;
	}

}
