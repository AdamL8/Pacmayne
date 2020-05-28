package systemThreads;

import java.util.List;

import components.AudioComponent;
import components.MoveComponent;
import components.UserInputComponent;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;

public class UserInputSystem extends SystemBase {

	public UserInputSystem(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(UserInputComponent.class.getName());
		for(Entity entity: entities) {
			UserInputComponent input = (UserInputComponent) entityManager.getComponentOfClass(UserInputComponent.class.getName(), entity);
			MessageEnum message = MessageQueue.consumeEntityMessages(entity, UserInputComponent.class.getName());
			
			if(message == null) {
				continue;
			}
			
			if (isDirection(message)) {
				Direction direction = getDirectionFromMessage(message);
				if (direction != Direction.NONE) {
					MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), entity);
					
					if (move != null) {
						move.setAwaitingDirection(direction);
					}
				}
			} else if (isVolumeControl(message)) {
				AudioComponent volume = (AudioComponent) entityManager.getComponentOfClass(AudioComponent.class.getName(), entity);
				if(volume != null) {
					if(message.equals(MessageEnum.VOLUME_DOWN)) {
						volume.decreaseVolume();
					} else if(message.equals(MessageEnum.VOLUME_UP)) {
						volume.increaseVolume();
					} else if(message.equals(MessageEnum.MUTE)) {
						volume.mute();
					}
				}
			}
		}		
	}
	
	private boolean isDirection(MessageEnum message) {
		return message.equals(MessageEnum.DOWN) || message.equals(MessageEnum.UP) || message.equals(MessageEnum.RIGHT) || message.equals(MessageEnum.LEFT);
	}
	
	private boolean isVolumeControl(MessageEnum message) {
		return message.equals(MessageEnum.VOLUME_DOWN) || message.equals(MessageEnum.VOLUME_UP) || message.equals(MessageEnum.MUTE);
	}
	
	private Direction getDirectionFromMessage(MessageEnum message) {
		switch(message) {
			case DOWN :
				return Direction.DOWN;
			case UP :
				return Direction.UP;
			case RIGHT :
				return Direction.RIGHT;
			case LEFT :
				return Direction.LEFT;
			default :
				return Direction.NONE;
		}
	}
}
