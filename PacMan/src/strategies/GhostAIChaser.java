package strategies;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;

//DESIGN PATTERN : Strategy
public class GhostAIChaser extends GhostAIStrategy{
	
	@Override
	public Direction getDirection(Direction direction, MessageEnum message) {
		if(message != null && message.equals(MessageEnum.PACMAN_SAW)) {
			return direction;
		}
		return super.getDirection(direction, message);
	}

	@Override
	public Strategy getStrategyEnum() {
		return Strategy.CHASE;
	}

}
