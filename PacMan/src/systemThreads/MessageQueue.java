package systemThreads;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import entities.Entity;

public class MessageQueue {
	
	private static final Map<Entity, Map<String, MessageEnum>> messageQueue = Collections.synchronizedMap(new HashMap<>());
	
	public static synchronized void addMessage(Entity entity, String componentName, MessageEnum message) {
		if(!messageQueue.containsKey(entity)) {
			messageQueue.put(entity, new HashMap<String, MessageEnum>());
		}
		messageQueue.get(entity).put(componentName, message);
	}

	public static synchronized void clearEntityMessages(Entity entity) {
		messageQueue.remove(entity);
	}
	
	public static synchronized MessageEnum consumeEntityMessages(Entity entity, String componentName) {
		if(!messageQueue.containsKey(entity)) {
			return null;
		} else {
			return messageQueue.get(entity).remove(componentName);
		}
	}
}
