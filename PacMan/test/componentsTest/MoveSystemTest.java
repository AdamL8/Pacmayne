package componentsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*; 

import components.MoveComponent;
import entities.CollisionType;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import entities.Maze;
import factories.EntityFactory;
import systemThreads.MoveSystem;

@TestInstance(Lifecycle.PER_CLASS)
class MoveSystemTest {
	MoveSystem system;
	EntityManager entityManager;
	EntityFactory factory;
	Entity pacman;
	@Mock
	private Maze maze;
	

	
	@BeforeAll
	void setup() {
		MockitoAnnotations.initMocks(this);
//		initTiles();
		entityManager = new EntityManager();
		factory = new EntityFactory(entityManager);
		pacman = factory.createPacMan(1, 1 , Direction.RIGHT);
		system = new MoveSystem(entityManager, maze, pacman);
		
	}
	
	@BeforeEach
	void reset() {
		pacman = factory.createPacMan(1, 1 , Direction.RIGHT);
	}
	
	@Test
	public void willNotCollide() {
		when(maze.validateMove(any(MoveComponent.class), any(Direction.class))).thenReturn(CollisionType.NONE);
		for(int i = 0; i<= 10; i++) {
			system.update();
		}
		MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), pacman);
		
		assertEquals(3, move.getTileX());
	}
	
	@Test
	public void willCollide() {
		when(maze.validateMove(any(MoveComponent.class), any(Direction.class))).thenReturn(CollisionType.COLLIDEWALL);
		for(int i = 0; i<= 10; i++) {
			system.update();
		}
		MoveComponent move = (MoveComponent) entityManager.getComponentOfClass(MoveComponent.class.getName(), pacman);
		
		assertEquals(1, move.getTileX());
	}

}
