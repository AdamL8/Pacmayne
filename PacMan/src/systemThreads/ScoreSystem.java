package systemThreads;

import java.util.List;

import components.ScoreComponent;
import entities.Entity;
import entities.EntityManager;

public class ScoreSystem extends SystemBase{

	public ScoreSystem(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(ScoreComponent.class.getName());
		for(Entity entity: entities) {
			ScoreComponent score = (ScoreComponent) entityManager.getComponentOfClass(ScoreComponent.class.getName(), entity);
			MessageEnum message = MessageQueue.consumeEntityMessages(entity, ScoreComponent.class.getName());
			
			if (message != null) {
				score.addScore(message.getPoints());
			}
		}
	}

}
