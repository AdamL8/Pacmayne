package components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Direction;
import entities.SpritesEnum;
import javafx.geometry.Bounds;
import rendering.Sprite;

public class GraphicsComponent implements IComponent{
	private Sprite sprite;
	private SpritesEnum spriteEnum;
	private Animation currentAnimation;
	private String currentImage;
	boolean isAnimated = false;
	private Map<SpritesEnum, Animation> animationGroup = new HashMap<SpritesEnum, Animation>();
	
	public GraphicsComponent(SpritesEnum spriteEnum, List<String> images, double x, double y, boolean oversize) {
		addAnimation(spriteEnum, images);
		this.spriteEnum = spriteEnum;
		this.sprite = new Sprite(currentImage, x, y, oversize);
		isAnimated = true;
	}
	
	public GraphicsComponent(String image, double x, double y, boolean oversize) {
		this.currentImage = image;
		this.sprite = new Sprite(image, x, y, oversize);
		this.spriteEnum = SpritesEnum.NONE;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public String getCurrentImage() {
		return currentImage;
	}
	
	public void updateImage() {
		if(isAnimated) {
			currentImage = currentAnimation.getNextImage();
			updateSpriteImage();
		}
	}
	
	private void updateSpriteImage() {
		sprite.setImage(currentImage);
	}
	
	public void addAnimation(SpritesEnum spriteEnum, List<String> images)
	{
		
		if (!animationGroup.containsKey(spriteEnum)) {
			Animation anim = new Animation(images);
			animationGroup.put(spriteEnum, anim);
		}
		if(currentImage == null || currentImage.isEmpty()) {
			currentImage = currentAnimation.getNextImage();
		}
	}

	public Bounds getBounds() {
		return sprite.getBounds();
	}
	
	public void removeImage() {
		sprite.removeImage();
	}
	
	public void setSpriteEnum(SpritesEnum spriteEnum) {
		if(this.spriteEnum != spriteEnum) {
			this.spriteEnum = spriteEnum;
			this.currentAnimation = animationGroup.get(spriteEnum);
			this.currentAnimation.imageIndex = 0;
		}
	}
	
	public void setSpriteEnum(Direction direction) {
		switch(direction) {
			case UP:
				this.spriteEnum = SpritesEnum.UP;
				break;
			case DOWN:
				this.spriteEnum = SpritesEnum.DOWN;
				break;
			case RIGHT:
				this.spriteEnum = SpritesEnum.RIGHT;
				break;
			case LEFT:
				this.spriteEnum = SpritesEnum.LEFT;
				break;
			default:
				break;
		}
		this.currentAnimation = animationGroup.get(spriteEnum);
		this.currentAnimation.imageIndex = 0;
	}
	
	
	public void updatePosition(double x, double y, Direction direction) {
		if(!spriteEnum.equals(SpritesEnum.DEATH) && !spriteEnum.equals(SpritesEnum.AFRAID) && !spriteEnum.equals(SpritesEnum.BLINKING)) {
			switch(direction) {
				case UP:
					this.spriteEnum = SpritesEnum.UP;
					break;
				case DOWN:
					this.spriteEnum = SpritesEnum.DOWN;
					break;
				case RIGHT:
					this.spriteEnum = SpritesEnum.RIGHT;
					break;
				case LEFT:
					this.spriteEnum = SpritesEnum.LEFT;
					break;
				default:
					break;
			}
			this.currentAnimation = animationGroup.get(spriteEnum);
		}
		
		sprite.updatePosition(x, y);
	}
	
	private class Animation {
		private List<String> images;
		private int imageIndex = 0;
		
		public Animation(List<String> images)
		{
			this.images = images;
			if (currentAnimation != this) {
				currentAnimation = this;
			}
		}
		
		public String getNextImage()
		{
			
			if (currentAnimation != this) {
				currentAnimation = this;
				this.imageIndex = 0;
			}
			
			int index = (imageIndex++) % images.size();
			if(imageIndex == images.size()) {
				if(spriteEnum == SpritesEnum.DEATH) {
					imageIndex = images.size() -1;
				} else {
					imageIndex = 0;
				}
			}
			
			return images.get(index);
		}
	}

	public SpritesEnum getSpriteEnum() {
		return spriteEnum;
	}
	
	
}
