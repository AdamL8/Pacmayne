package systemThreads;

import entities.Entity;

public class Message {
	
	private Entity entity;
	private MessageEnum message;
	
	public Message(Entity entity, MessageEnum message) {
		this.entity = entity;
		this.message = message;
	}

	public Entity getEntity() {
		return entity;
	}

	public MessageEnum getMessage() {
		return message;
	}

}
