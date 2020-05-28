package components;

public class PhysicsComponent implements IComponent {
	private String collisionType;
	
	public PhysicsComponent(String collisionType) {
		this.collisionType = collisionType;
	}

	public String getCollisionType() {
		return collisionType;
	}
}
