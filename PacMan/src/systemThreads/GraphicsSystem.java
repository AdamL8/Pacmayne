package systemThreads;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import components.GraphicsComponent;
import components.MoveComponent;
import entities.Entity;
import entities.EntityManager;
import entities.SpritesEnum;

public class GraphicsSystem extends SystemBase implements Runnable{
	
	private static Logger logger = Logger.getAnonymousLogger();
	private Entity pacman;
	private volatile boolean isRunning = true;
	private int counter = 0;
	
	public GraphicsSystem(EntityManager entityManager, Entity pacman) {
		super(entityManager);
		this.pacman = pacman;
	}

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(GraphicsComponent.class.getName());
		boolean eaten;
		for(Entity entity: entities) {
			eaten = false;
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), entity);
			MessageEnum message = MessageQueue.consumeEntityMessages(entity, GraphicsComponent.class.getName());
			if(message != null) {
				if(message.equals(MessageEnum.EATEN)) {
					graphic.removeImage();
					entityManager.removeEntity(entity);
					eaten = true; //there was a leg of an updated image called after image was removed leaving supergums visible after being eaten
				} else if(message.equals(MessageEnum.KILLED) && entity == pacman) {
					graphic.setSpriteEnum(SpritesEnum.DEATH);
				} else if(message.equals(MessageEnum.INVINCIBLE_START)) {

					if (move != null && move.getPassedGate()) {
						graphic.setSpriteEnum(SpritesEnum.AFRAID);
					}					
				} else if (message.equals(MessageEnum.BLINKING) && graphic.getSpriteEnum() == SpritesEnum.AFRAID) {
					if (move != null && move.getPassedGate()) {
						graphic.setSpriteEnum(SpritesEnum.BLINKING);
					}
				} else if(message.equals(MessageEnum.INVINCIBLE_END) || (entity != pacman && message.equals(MessageEnum.KILLED))) {				
					graphic.setSpriteEnum(move.getDirection());					
				}
			} 
			
			if(counter == 3 && !eaten) {
				graphic.updateImage();	
			} 
		}	
		counter = counter == 3 ? 0 : counter + 1;
	}

	public void stopThread() {
		isRunning = false;		
	}

	@Override
	public void run() {
		System.out.println("Start graphic Thread");
		while(isRunning) {
			update();
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Stop graphic Thread!");	
		
	}

}
