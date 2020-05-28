package systemThreads;

import java.util.List;
import java.util.Timer;

import components.AudioComponent;
import components.GraphicsComponent;
import components.InvincibleComponent;
import components.MoveComponent;
import components.PhysicsComponent;
import entities.Entity;
import entities.EntityManager;
import utils.SyncTimerTask;

public class InvincibleSystem extends SystemBase {

	private Entity pacman;
	private Entity inky;
	private Entity blinky;
	private Entity pinky;
	private Entity clyde;
	private SyncTimerTask invincibleTask;
	private SyncTimerTask blinkingTask;
	private Timer invincibleTimer = new Timer();
	private Timer blinkingTimer = new Timer();
	
	public InvincibleSystem(EntityManager entityManager, Entity pacman, Entity inky, Entity blinky, Entity pinky, Entity clyde) {
		super(entityManager);
		this.pacman = pacman;
		this.inky = inky;
		this.blinky = blinky;
		this.pinky = pinky;
		this.clyde = clyde;
		invincibleTask = new SyncTimerTask(() -> addInvincibleEndMessages());
		blinkingTask = new SyncTimerTask(() -> addBlinkingMessage());
		
	}

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(InvincibleComponent.class.getName());
		for(Entity entity: entities) {
			InvincibleComponent invincible = (InvincibleComponent) entityManager.getComponentOfClass(InvincibleComponent.class.getName(), entity);
			
			if (invincible == null)
				continue;

			MessageEnum message = MessageQueue.consumeEntityMessages(entity, InvincibleComponent.class.getName());
			if(message != null && message.equals(MessageEnum.INVINCIBLE_START)) {
				MessageQueue.addMessage(pacman, AudioComponent.class.getName(), MessageEnum.INVINCIBLE_START); 
				startInvincibleTimer(invincible.getImmunityTime());
			}
		}			
	}
	
	private void addInvincibleEndMessages() {
		MessageQueue.addMessage(pacman, AudioComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(pacman, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_END);					
		MessageQueue.addMessage(pacman, PhysicsComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(inky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(blinky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(pinky, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(clyde, GraphicsComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(inky, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(blinky, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(pinky, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		MessageQueue.addMessage(clyde, MoveComponent.class.getName(), MessageEnum.INVINCIBLE_END);
	}
	
	private void addBlinkingMessage() {
		MessageQueue.addMessage(inky, GraphicsComponent.class.getName(), MessageEnum.BLINKING);
		MessageQueue.addMessage(blinky, GraphicsComponent.class.getName(), MessageEnum.BLINKING);
		MessageQueue.addMessage(pinky, GraphicsComponent.class.getName(), MessageEnum.BLINKING);
		MessageQueue.addMessage(clyde, GraphicsComponent.class.getName(), MessageEnum.BLINKING);
	}
	
	private void startInvincibleTimer(double timeInSeconds) {
		if(invincibleTask.hasRunStarted()) {
			invincibleTimer.cancel();
		}
		
		if(blinkingTask.hasRunStarted()) {
			blinkingTimer.cancel();
		}
		
		blinkingTimer = new Timer();
		invincibleTimer = new Timer();
		
		invincibleTask = new SyncTimerTask(() -> addInvincibleEndMessages());
		blinkingTask = new SyncTimerTask(() -> addBlinkingMessage());
		invincibleTask.startRunning();		
		blinkingTask.startRunning();
		
		invincibleTimer.schedule(invincibleTask, (long) (timeInSeconds * 1000));		
		blinkingTimer.schedule(blinkingTask, (long) (timeInSeconds * 1000 * 3 / 4));
	}
}
