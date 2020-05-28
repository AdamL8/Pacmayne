package componentsTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import components.AudioComponent;
import components.GraphicsComponent;
import components.LifeComponent;
import components.PhysicsComponent;
import components.ScoreComponent;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import factories.EntityFactory;
import strategies.GhostAIRandom;
import systemThreads.MessageEnum;
import systemThreads.MessageQueue;
import systemThreads.PhysicsSystem;

@TestInstance(Lifecycle.PER_CLASS)
class PhysicsSystemTest {

	private static Logger logger = Logger.getAnonymousLogger();
	PhysicsSystem system;
	EntityManager entityManager;
	EntityFactory factory;
	Entity pacman;
	Entity inky;
	Entity blinky;
	Entity pinky;
	Entity clyde;
	private Thread physicsSystemThread;
	
	@BeforeAll
	void setup() {
		entityManager = new EntityManager();
		factory = new EntityFactory(entityManager);
		pacman = factory.createPacMan(1, 1 , Direction.RIGHT);
		inky = factory.createGhost(15, 15, Direction.RIGHT, "inky", new GhostAIRandom());
		blinky = factory.createGhost(15, 15, Direction.RIGHT, "blinky", new GhostAIRandom());
		pinky = factory.createGhost(15, 15, Direction.RIGHT, "pinky", new GhostAIRandom());
		clyde = factory.createGhost(15, 15, Direction.RIGHT, "clyde", new GhostAIRandom());
		system = new PhysicsSystem(entityManager, pacman, inky, blinky, pinky, clyde);	
		try {
			startPysicsThread();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	@AfterAll
	void tearDown() {
		try {
			stopPysicsThread();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	void startPysicsThread() throws InterruptedException {
		System.out.println("BEFOREEACH = " + System.currentTimeMillis());
		physicsSystemThread = new Thread(system);
		physicsSystemThread.start();
		physicsSystemThread.join(33);
		assertTrue("thread dead", physicsSystemThread.isAlive());
	}
	

	void stopPysicsThread() throws Exception {
		System.out.println("AFTEREACH = " + System.currentTimeMillis());
		boolean expectedStop = true;
		boolean actualStop = physicsSystemThread.isAlive();
		if(actualStop) {
			system.stopThread();
			try {
				physicsSystemThread.join(33);
				actualStop = !physicsSystemThread.isAlive(); //check if thread is dead
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			assertEquals("thread not stopped" , expectedStop, actualStop);
		}
	}
	
	
	@Test
	public void testGumCollision() throws Exception {
		Entity gum = factory.createGum(1, 1);

		Thread.sleep(150);
		MessageEnum message = MessageQueue.consumeEntityMessages(pacman, AudioComponent.class.getName());
		assertTrue("", message == MessageEnum.EATEN);	
		message = MessageQueue.consumeEntityMessages(pacman, ScoreComponent.class.getName());
		assertTrue("", message == MessageEnum.GUMPOINTS);
		message = MessageQueue.consumeEntityMessages(gum, GraphicsComponent.class.getName());
		assertTrue("", message == MessageEnum.EATEN);	
	}
	
	@Test
	public void testSuperGumCollision() throws Exception {
		Entity superGum = factory.createSuperGum(1, 1);
		Thread.sleep(100);
		
		MessageEnum message = MessageQueue.consumeEntityMessages(superGum, GraphicsComponent.class.getName());
		assertTrue("Message = " + message, message == MessageEnum.EATEN);
		message = MessageQueue.consumeEntityMessages(pacman, ScoreComponent.class.getName());
		assertTrue("Message = " + message, message == MessageEnum.SUPERGUMPOINTS);
		message = MessageQueue.consumeEntityMessages(pacman, AudioComponent.class.getName());
		assertTrue("Message = " + message, message == MessageEnum.EATEN);	

	}
	
	@Test
	public void testGhostCollision() throws Exception {
		Entity ghost = factory.createGhost(1, 1, Direction.DOWN, "Blinky", new GhostAIRandom());
		Thread.sleep(100);
		
		MessageEnum message = MessageQueue.consumeEntityMessages(pacman, LifeComponent.class.getName());
		assertTrue("Message = " + message, message == MessageEnum.KILLED);
	}
	
	private void addMessageQueue() {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("Pacman"), entity);
		Map<MessageEnum, String> pacmanAudioMap = new HashMap<>();
		pacmanAudioMap.put(MessageEnum.EATEN, "ressource/audio/waka.wav");
		entityManager.addComponent(new AudioComponent(pacmanAudioMap), entity);
		
		MessageQueue.addMessage(entity, AudioComponent.class.getName() , MessageEnum.EATEN);
	}

}