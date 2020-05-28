package systemThreads;

import java.util.List;

import components.AIComponent;
import components.GraphicsComponent;
import components.MoveComponent;
import entities.CollisionType;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import entities.Maze;

public class MoveSystem extends SystemBase {
	private Maze maze;
	private Entity pacman;
	
	public MoveSystem(EntityManager entityManager, Maze maze, Entity pacman) {
		super(entityManager);
		this.maze = maze;
		this.pacman = pacman;
	}
	

	@Override
	public void update() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class.getName());
		MessageEnum pacmanMessage = MessageQueue.consumeEntityMessages(pacman, MoveComponent.class.getName());
		for(Entity entity: entities) {	
			MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), entity);
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			
			
			if(graphic == null) 
				continue;
			
			if(pacmanMessage != null) {
				if(pacmanMessage == MessageEnum.KILLED) {
					move.setDirection(Direction.NONE);
					move.setAwaitingDirection(Direction.NONE);
				} else if((pacmanMessage == MessageEnum.INVINCIBLE_START || pacmanMessage == MessageEnum.INVINCIBLE_END) && entity.equals(pacman)) {
					move.setIsFast(pacmanMessage == MessageEnum.INVINCIBLE_START);
				}
				
			}
			
			if(entity != pacman) {
				MessageEnum message = MessageQueue.consumeEntityMessages(entity, MoveComponent.class.getName());
				if(message != null && message == MessageEnum.KILLED) {		
					message = MessageQueue.consumeEntityMessages(entity, MoveComponent.class.getName());
					move.setCanMoveWhenAble(false);
					move.resetPosition();
					move.setPassedGate(false);

					move.setPassingGate(false);

				} else if (message != null && message.equals(MessageEnum.INVINCIBLE_END)) {
					move.setCanMoveWhenAble(true);	
				}
					
			}

			CollisionType awaitingCollisionType = maze.validateMove(move, move.getAwaitingDirection());
			
			if(!move.isInTunnel() && move.canTurn() &&!move.getAwaitingDirection().equals(Direction.NONE) && 
					( awaitingCollisionType == CollisionType.NONE || (awaitingCollisionType == CollisionType.TUNNEL && move.canPassTunnel()) || (awaitingCollisionType == CollisionType.GATE && move.canPassGate() && !move.canPassGate()))) {
				move.updateDirection();
			} 
			
			CollisionType collisionType = maze.validateMove(move, move.getDirection());
			if(collisionType == CollisionType.NONE) {
				move.setPassedGate(move.isPassingGate());
				move.moveOneFrameBySpeed();
				move.setInTunnel(false);
				graphic.updatePosition(move.getX(), move.getY(), move.getDirection());
			} else if(collisionType == CollisionType.TUNNEL && move.canPassTunnel()) {
				move.setInTunnel(true);
				move.moveOneFrameBySpeed();
				graphic.updatePosition(move.getX(), move.getY(), move.getDirection());
			} else if (collisionType == CollisionType.OVERBOUND) {
				move.passTunnel();

			} else if (collisionType == CollisionType.GATE) {
				move.setPassingGate(true);
				move.moveOneFrameBySpeed();
				graphic.updatePosition(move.getX(), move.getY(), move.getDirection());

			} else if (entity != pacman && (collisionType == CollisionType.TUNNEL || collisionType == CollisionType.COLLIDEWALL)) {
				MessageQueue.addMessage(entity, AIComponent.class.getName(), MessageEnum.HIT_WALL);
			} 			
		}		
	}
	
	public synchronized void respawn() {
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class.getName());
		for(Entity entity: entities) {	
			MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), entity);
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			if(graphic == null) 
				continue;
			move.resetPosition();
			move.setPassedGate(false);
			move.setPassingGate(false);
			graphic.updatePosition(move.getX(), move.getY(), move.getDirection());
			graphic.setSpriteEnum(move.getDirection());
		}
	}

}
