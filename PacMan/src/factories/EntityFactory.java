package factories;

import static configs.GameConfig.TILE_SIZE;

import java.util.HashMap;
import java.util.Map;

import components.AIComponent;
import components.AudioComponent;
import components.GraphicsComponent;
import components.InvincibleComponent;
import components.LifeComponent;
import components.MoveComponent;
import components.PhysicsComponent;
import components.ScoreComponent;
import components.UserInputComponent;
import entities.Direction;
import entities.Entity;
import entities.EntityManager;
import entities.SpritesEnum;
import image.ImageRepository;
import strategies.GhostAIStrategy;
import systemThreads.MessageEnum;

public class EntityFactory {
	private EntityManager entityManager;
	
	public EntityFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Entity createPacMan(int x, int y, Direction direction) {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("Pacman"), entity);
		GraphicsComponent graphic = new GraphicsComponent(SpritesEnum.RIGHT, ImageRepository.getImages("pacman", SpritesEnum.RIGHT), x * TILE_SIZE, y * TILE_SIZE, true);
		graphic.addAnimation(SpritesEnum.LEFT, ImageRepository.getImages("pacman", SpritesEnum.LEFT));
		graphic.addAnimation(SpritesEnum.UP, ImageRepository.getImages("pacman", SpritesEnum.UP));
		graphic.addAnimation(SpritesEnum.DOWN, ImageRepository.getImages("pacman", SpritesEnum.DOWN));
		graphic.addAnimation(SpritesEnum.DEATH, ImageRepository.getImages("pacman", SpritesEnum.DEATH));
		entityManager.addComponent(graphic, entity);
		entityManager.addComponent(new InvincibleComponent(), entity);
		
		Map<MessageEnum, String> pacmanAudioMap = new HashMap<>();
		pacmanAudioMap.put(MessageEnum.EATEN, "ressource/audio/waka.wav");
		pacmanAudioMap.put(MessageEnum.INVINCIBLE_START, "ressource/audio/pacman-invincible.wav");
		entityManager.addComponent(new AudioComponent(pacmanAudioMap), entity);
		
		entityManager.addComponent(new MoveComponent(x, y, direction, true, false), entity);
		entityManager.addComponent(new ScoreComponent(), entity);
		entityManager.addComponent(new UserInputComponent(), entity);
		entityManager.addComponent(new LifeComponent(3), entity);
		return entity;
	}
	
	public Entity createGhost(int x, int y, Direction direction, String ghostName, GhostAIStrategy strategy) {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("Ghost"), entity);
		GraphicsComponent graphic = new GraphicsComponent(SpritesEnum.RIGHT, ImageRepository.getImages(ghostName, SpritesEnum.RIGHT), x * TILE_SIZE, y * TILE_SIZE, true);
		graphic.addAnimation(SpritesEnum.LEFT, ImageRepository.getImages(ghostName, SpritesEnum.LEFT));
		graphic.addAnimation(SpritesEnum.UP, ImageRepository.getImages(ghostName, SpritesEnum.UP));
		graphic.addAnimation(SpritesEnum.DOWN, ImageRepository.getImages(ghostName, SpritesEnum.DOWN));
		graphic.addAnimation(SpritesEnum.AFRAID, ImageRepository.getImages("frightened", SpritesEnum.AFRAID));
		graphic.addAnimation(SpritesEnum.BLINKING, ImageRepository.getImages("blinking", SpritesEnum.BLINKING));
		entityManager.addComponent(graphic, entity);
		entityManager.addComponent(new MoveComponent(x , y, direction, false, true), entity);

		entityManager.addComponent(new AIComponent(strategy), entity);

		return entity;
	}
	
	public Entity createGum(int x, int y) {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("Gum"), entity);
		entityManager.addComponent(new GraphicsComponent(ImageRepository.getImages("gum", SpritesEnum.NONE).get(0), x * TILE_SIZE, y * TILE_SIZE, false), entity);
		return entity;
	}
	
	public Entity createSuperGum(int x, int y) {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new PhysicsComponent("SuperGum"), entity);
		entityManager.addComponent(new GraphicsComponent(SpritesEnum.NONE, ImageRepository.getImages("gum", SpritesEnum.NONE), x * TILE_SIZE, y * TILE_SIZE, false), entity);
		return entity;
	}
	
	public Entity createWall(int x, int y, int wallIndex) {
		Entity entity = entityManager.CreateEntity();
		entityManager.addComponent(new GraphicsComponent(ImageRepository.getImages("wall-" + wallIndex , SpritesEnum.NONE).get(0), x * TILE_SIZE, y * TILE_SIZE, false), entity);
		return entity;
	}

}
