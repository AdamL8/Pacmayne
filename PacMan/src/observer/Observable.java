package observer;

import java.util.ArrayList;

//DESIGN PATTERN : Observer
public class Observable {

	private ArrayList<IObserver> observerList = new ArrayList<IObserver>();
	
	public void register(IObserver observer) {
		if(!observerList.contains(observer)) {
			observerList.add(observer);
		}
	}
	
	public void unregister(IObserver observer) {
		if(observerList.contains(observer)) {
			observerList.remove(observer);
		}
	}
	
	public void notify(IObserver observer) {
		observer.update();
	}
	
	public void notifyAllObservers() {
		for (IObserver observer: observerList) {
			observer.update();
		}
	}
}
