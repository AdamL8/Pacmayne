package systemThreads;

import java.util.List;
import java.util.Timer;

import components.LifeComponent;
import entities.Entity;
import entities.EntityManager;
import utils.SyncTimerTask;

public class LifeSystem extends SystemBase {
	
	public LifeSystem(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(LifeComponent.class.getName());
		for(Entity entity: entities) {
			LifeComponent life = (LifeComponent) entityManager.getComponentOfClass(LifeComponent.class.getName(), entity);
			MessageEnum message = MessageQueue.consumeEntityMessages(entity, LifeComponent.class.getName());
			
			if (message != null) {
				death(life);
				
			}	
		}
		
	}
	
	private void death(LifeComponent life) {
		Timer timer = new Timer();
		SyncTimerTask repeatedTask = new SyncTimerTask(() -> life.removeLife());
		repeatedTask.startRunning();
		timer.schedule(repeatedTask, (long) (1500));
	}

}
