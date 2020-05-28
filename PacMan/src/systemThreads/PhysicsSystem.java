package systemThreads;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import components.AudioComponent;
import components.GraphicsComponent;
import components.InvincibleComponent;
import components.LifeComponent;
import components.MoveComponent;
import components.PhysicsComponent;
import components.ScoreComponent;
import entities.Entity;
import entities.EntityManager;
import entities.SpritesEnum;

public class PhysicsSystem extends SystemBase implements Runnable {
	
	private static Logger logger = Logger.getAnonymousLogger();
	private Entity pacman;
	private Entity inky;
	private Entity blinky;
	private Entity pinky;
	private Entity clyde;
	private volatile boolean isRunning = true;
	private int scoreMultiplier = 1;
	private final int BASE_SCORE_FOR_GHOST = 200;
	
	public PhysicsSystem(EntityManager entityManager, Entity pacman, Entity inky, Entity blinky, Entity pinky, Entity clyde) {
		super(entityManager);
		this.pacman = pacman;
		this.inky = inky;
		this.blinky = blinky;
		this.pinky = pinky;
		this.clyde = clyde;
	}
	

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(PhysicsComponent.class.getName());
		GraphicsComponent pacmanGraphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), pacman);		
		for(Entity entity: entities) {
			PhysicsComponent physic = (PhysicsComponent) entityManager.getComponentOfClass(PhysicsComponent.class.getName(), entity);
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			if (graphic == null || entity == pacman)
				continue;
			
			if(pacmanGraphic.getBounds().intersects(graphic.getBounds())) {
				MessageEnum message = MessageQueue.consumeEntityMessages(pacman, PhysicsComponent.class.getName());
				if (message != null && message.equals(MessageEnum.INVINCIBLE_END)) {					
					scoreMultiplier = 1;
				}
				
				if (physic.getCollisionType() == "Gum") { 
					MessageQueue.addMessage(entity, GraphicsComponent.class.getName(), MessageEnum.EATEN);
					MessageQueue.addMessage(pacman, ScoreComponent.class.getName(), MessageEnum.GUMPOINTS);
					MessageQueue.addMessage(pacman, AudioComponent.class.getName(), MessageEnum.EATEN);
				} else if (physic.getCollisionType() == "SuperGum"){
					MessageQueue.addMessage(entity, GraphicsComponent.class.getName(), MessageEnum.EATEN);
					MessageQueue.addMessage(pacman, AudioComponent.class.getName(), MessageEnum.EATEN);
					MessageQueue.addMessage(inky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_START);	
					MessageQueue.addMessage(blinky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_START);
					MessageQueue.addMessage(pinky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_START);
					MessageQueue.addMessage(clyde, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_START);
					MessageQueue.addMessage(pacman, InvincibleComponent.class.getName(), MessageEnum.INVINCIBLE_START);					
					MessageQueue.addMessage(pacman, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_START);					
					MessageQueue.addMessage(pacman, ScoreComponent.class.getName(), MessageEnum.SUPERGUMPOINTS);
					
				} else if(physic.getCollisionType() == "Ghost") {
					if(graphic.getSpriteEnum().equals(SpritesEnum.AFRAID) || graphic.getSpriteEnum().equals(SpritesEnum.BLINKING)) {
						MessageQueue.addMessage(pacman, ScoreComponent.class.getName(), MessageEnum.valueOf("GHOST" + BASE_SCORE_FOR_GHOST * scoreMultiplier));
						scoreMultiplier = scoreMultiplier == 8 ? 1 : scoreMultiplier * 2;
						MessageQueue.addMessage(entity, MoveComponent.class.getName(), MessageEnum.KILLED);
						MessageQueue.addMessage(entity, GraphicsComponent.class.getName(), MessageEnum.KILLED);
					} else if(!pacmanGraphic.getSpriteEnum().equals(SpritesEnum.DEATH)) {
						MessageQueue.addMessage(pacman, LifeComponent.class.getName(), MessageEnum.KILLED);
						MessageQueue.addMessage(pacman, MoveComponent.class.getName(), MessageEnum.KILLED);
						MessageQueue.addMessage(pacman, GraphicsComponent.class.getName(), MessageEnum.KILLED);
					}
				}			
			}
		}		
	}
	
	public void stopThread() {
		isRunning = false;		
	}


	@Override
	public void run() {
		System.out.println("Start Physics Thread");
		while(isRunning) {
			update();
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Stop Physics Thread!");	
	}
}