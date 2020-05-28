package strategies;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;

//DESIGN PATTERN : Strategy
public class GhostAIAmbusher extends GhostAIStrategy{

	@Override
	public Direction getDirection(Direction direction, MessageEnum message) {
		if(message != null && message.equals(MessageEnum.PACMAN_PARALLELE)) {
			super.removeOppositeDirection(direction);
		}
		return super.getDirection(direction, message);
	}

	@Override
	public Strategy getStrategyEnum() {
		return Strategy.AMBUSH;
	}

}
