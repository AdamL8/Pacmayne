package strategies;

import entities.Direction;
import entities.Strategy;
import systemThreads.MessageEnum;

//DESIGN PATTERN : Strategy
public class GhostAIRandom extends GhostAIStrategy{
	
	@Override
	public Direction getDirection(Direction currentDirection, MessageEnum message) {
		return super.getDirection(currentDirection, message);
	}
	
	@Override
	public Strategy getStrategyEnum() {
		return Strategy.RANDOM;
	}
	


}
