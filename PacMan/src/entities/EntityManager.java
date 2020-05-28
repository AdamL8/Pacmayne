package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.IComponent;
import systemThreads.MessageQueue;

public class EntityManager {
	private List<Entity> entities;
	private Map<String, Map<Entity, IComponent>> componentsByClass;
	private int lowestUnassingedId;
	
	public EntityManager() {
		lowestUnassingedId = 0;
		entities = new ArrayList<>();
		componentsByClass = new HashMap<String, Map<Entity, IComponent>>();
	}
	
	public int generateNewId() {
		return lowestUnassingedId++; 
		
	}
	
	public Entity CreateEntity() {
		return new Entity(generateNewId());
	}
	
	public synchronized void addComponent(IComponent component, Entity entity) {
		String name = component.getClass().getName();
		if(!componentsByClass.containsKey(name)) {
			componentsByClass.put(name, new HashMap<Entity, IComponent>());
			componentsByClass.get(name).put(entity, component);
		} else {
			if(!componentsByClass.get(name).containsKey(entity)) {
				componentsByClass.get(name).put(entity, component);
			}
		}
	}
	
	public synchronized void removeEntity(Entity entity) {
		for (Map.Entry<String, Map<Entity, IComponent>> parentEntry : componentsByClass.entrySet()) {
			if(parentEntry.getValue().containsKey(entity)) {
				parentEntry.getValue().remove(entity);
			}
		}
		entities.remove(entity);
		MessageQueue.clearEntityMessages(entity);
	}
	
	public synchronized IComponent getComponentOfClass(String className, Entity entity) {
		return componentsByClass.get(className).get(entity);
	}
	
	public synchronized List<Entity> getAllEntitiesPosessingComponentOfClass(String className) {
		List<Entity> entities = new ArrayList<>();
		Map<Entity, IComponent> classMap = componentsByClass.get(className);
		if(classMap == null)
			return entities;
		for (Map.Entry<Entity, IComponent> entry : classMap.entrySet()) {
			entities.add(entry.getKey());
		}
		
		return entities;
	}
	
	public void dispose() {
		for (Map.Entry<String, Map<Entity, IComponent>> parentEntry : componentsByClass.entrySet()) {
			parentEntry.getValue().clear();
			
		}
		componentsByClass.clear();
	}
	

}
