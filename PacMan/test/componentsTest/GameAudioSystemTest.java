package componentsTest;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import components.AudioComponent;
import components.PhysicsComponent;
import entities.Entity;
import entities.EntityManager;
import systemThreads.GameAudioSystem;
import systemThreads.MessageEnum;
import systemThreads.MessageQueue;

@TestInstance(Lifecycle.PER_CLASS)
class GameAudioSystemTest {

	private static Logger logger = Logger.getAnonymousLogger();
	private GameAudioSystem gameAudioSystem;
	private EntityManager entityManager;
	private Thread tGameAudioSystem;
	
	private void addMessageQueue() {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("Pacman"), entity);
		Map<MessageEnum, String> pacmanAudioMap = new HashMap<>();
		pacmanAudioMap.put(MessageEnum.EATEN, "ressource/audio/waka.wav");
		pacmanAudioMap.put(MessageEnum.INVINCIBLE_START, "ressource/audio/pacman-invincible.wav");
		entityManager.addComponent(new AudioComponent(pacmanAudioMap), entity);
		
		MessageQueue.addMessage(entity, AudioComponent.class.getName() , MessageEnum.EATEN);
	}


	@BeforeEach
	void setUpBeforeClass() throws InterruptedException {
		
		entityManager = new EntityManager();
		gameAudioSystem = new GameAudioSystem(entityManager);
		addMessageQueue();
		tGameAudioSystem = new Thread(gameAudioSystem);
		tGameAudioSystem.start();
		tGameAudioSystem.join(100);
		assertTrue("thread dead", tGameAudioSystem.isAlive());
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
		boolean expectedStop = true;
		boolean actualStop = tGameAudioSystem.isAlive();
		if(actualStop) {
			gameAudioSystem.stopThread();
			try {
				tGameAudioSystem.join(1500);
				actualStop = !tGameAudioSystem.isAlive(); //check if thread is dead
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			assertEquals("thread not stopped" , expectedStop, actualStop);
		}
	}

	@Test
	void playBackgroundMusicTest() {
		assertTimeout(ofMillis(10000), () -> {
			Thread.sleep(200);
			boolean actualPlaying = gameAudioSystem.isPlayingBackground();
			assertTrue("audio is not playing in background", actualPlaying);
		});
	}
	
	@Test
	void updateMusic() {
		assertTimeout(ofMillis(10000), () -> {
			Thread.sleep(200);
			boolean actualUpdateMusic = gameAudioSystem.isUpdatingMusic();
			assertTrue("audio is not playing", actualUpdateMusic);
			gameAudioSystem.stopThread();
			try {
				tGameAudioSystem.join(3000);
				assertFalse("Thread stopped", tGameAudioSystem.isAlive());
				actualUpdateMusic = gameAudioSystem.isUpdatingMusic();
				assertFalse("audio is playing", actualUpdateMusic);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		});
	}
	
	

}
