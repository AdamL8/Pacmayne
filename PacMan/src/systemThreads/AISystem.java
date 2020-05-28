package systemThreads;

import java.util.List;

import components.AIComponent;
import components.GraphicsComponent;
import components.MoveComponent;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import entities.Maze;
import entities.SpritesEnum;

public class AISystem extends SystemBase {
	private Entity pacman;
	private Maze maze;
	
	public AISystem(EntityManager entityManager, Entity pacman, Maze maze) {
		super(entityManager);
		this.pacman = pacman;
		this.maze = maze;
	}

	@Override
	public void update() {
		MoveComponent pacmanMove = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), pacman);
		boolean isScared = false;
		List<Entity> entities = entityManager.getAllEntitiesPosessingComponentOfClass(AIComponent.class.getName());
		for(Entity entity: entities) {
			AIComponent ai = (AIComponent) entityManager.getComponentOfClass(AIComponent.class.getName(), entity);
			MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), entity);
			GraphicsComponent graphic = (GraphicsComponent) entityManager.getComponentOfClass(GraphicsComponent.class.getName(), entity);
			
			
			if(move == null) {
				continue;
			}
			
			if(graphic.getSpriteEnum().equals(SpritesEnum.AFRAID) || graphic.getSpriteEnum().equals(SpritesEnum.BLINKING)) {
				handleChase(move, pacmanMove, ai, entity, true);
			} else {
				switch(ai.getStrategy().getStrategyEnum()) {
				case RANDOM:
					handleRandom(move, ai, entity, false);
					break;
				case AMBUSH:
					handleAmbush(move, pacmanMove, ai, entity, false);
					break;
				case STUPID:
				case CHASE:
					handleChase(move, pacmanMove, ai, entity, false);
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void handleAmbush(MoveComponent move, MoveComponent pacmanMove,  AIComponent ai, Entity entity, boolean isScared) {
		if(Math.abs(move.getX() - pacmanMove.getX()) < 4) {
			if(move.getY() < pacmanMove.getY()) {
				move.setAwaitingDirection(ai.getDirection(Direction.DOWN, isScared, MessageEnum.PACMAN_PARALLELE));
			} else {
				move.setAwaitingDirection(ai.getDirection(Direction.UP, isScared, MessageEnum.PACMAN_PARALLELE));
			}
		} else if(Math.abs(move.getY() - pacmanMove.getY()) < 4) {
			if(move.getX() < pacmanMove.getX()) {
				move.setAwaitingDirection(ai.getDirection(Direction.RIGHT, isScared, MessageEnum.PACMAN_PARALLELE));
			} else {
				move.setAwaitingDirection(ai.getDirection(Direction.LEFT, isScared, MessageEnum.PACMAN_PARALLELE));
			}
		} else {
			handleRandom(move, ai, entity, isScared);
		}
	}
	
	private void handleChase(MoveComponent move, MoveComponent pacmanMove,  AIComponent ai, Entity entity, boolean isScared) {
		boolean chasing = false;
		if(Math.abs(move.getX() - pacmanMove.getX()) < 5) {
			if(move.getY() < pacmanMove.getY()) {
				if(maze.isInSameCorridor(move.getTileY(), pacmanMove.getTileY(), move.getTileX(), pacmanMove.getTileX(), "y")) {
					chasing = true;
					move.setAwaitingDirection(ai.getDirection(Direction.DOWN, isScared, MessageEnum.PACMAN_SAW));
				}
			} else {
				if(maze.isInSameCorridor(pacmanMove.getTileY(), move.getTileY(), pacmanMove.getTileX(), move.getTileX(), "y")) {
					chasing = true;
					move.setAwaitingDirection(ai.getDirection(Direction.UP, isScared, MessageEnum.PACMAN_SAW));
				}
			}
		} else if(Math.abs(move.getY() - pacmanMove.getY()) < 5) {
			if(move.getX() < pacmanMove.getX()) {
				if(maze.isInSameCorridor(move.getTileY(), pacmanMove.getTileY(), move.getTileX(), pacmanMove.getTileX(), "x")) {
					chasing = true;
					move.setAwaitingDirection(ai.getDirection(Direction.RIGHT, isScared, MessageEnum.PACMAN_SAW));
				}
			} else {
				if(maze.isInSameCorridor(pacmanMove.getTileY(), move.getTileY(), pacmanMove.getTileX(), move.getTileX(), "x")) {
					chasing = true;
					move.setAwaitingDirection(ai.getDirection(Direction.LEFT, isScared, MessageEnum.PACMAN_SAW));
				}
			}
		} 
		if(!chasing) {
			handleRandom(move, ai, entity, isScared);
		}
	}
	
	private void handleRandom(MoveComponent move,  AIComponent ai, Entity entity, boolean isScared) {
		move.setAwaitingDirection(ai.getDirection(move.getDirection(), isScared, entity));
	}

}
