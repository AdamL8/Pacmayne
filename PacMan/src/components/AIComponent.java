package components;

import entities.Direction;
import entities.Entity;
import strategies.GhostAIAfraid;
import strategies.GhostAIStrategy;
import systemThreads.MessageEnum;
import systemThreads.MessageQueue;

public class AIComponent implements IComponent{
	private GhostAIStrategy strategy;
	private GhostAIStrategy afraidStrategy = new GhostAIAfraid();
	
	public Direction getDirection(Direction currentDirection, boolean isScared, Entity entity) {
		MessageEnum message = MessageQueue.consumeEntityMessages(entity, AIComponent.class.getName());
		return  isScared ? afraidStrategy.getDirection(currentDirection, message) : strategy.getDirection(currentDirection, message);
	}
	
	public Direction getDirection(Direction desiredDirection, boolean isScared, MessageEnum message) {
		return isScared ? afraidStrategy.getDirection(desiredDirection, message) : strategy.getDirection(desiredDirection, message);
	}
	
	public AIComponent(GhostAIStrategy strategy) {
		this.strategy = strategy;
	}

	public GhostAIStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(GhostAIStrategy strategy) {
		this.strategy = strategy;
	}

}
