package states;

import javafx.scene.input.KeyCode;

//DESIGN PATTERN : State
public class StateManager {

	static private IState currentState;
	static private IState lastState;
	
	public void run() {
		// TODO:
	}
	
	public void stop() {
		// TODO:
	}
	
	public static void handleUserInputs(KeyCode key) {
		if (currentState != null) {
			currentState.handleInput(key);
		}
	}
	
	public static void setCurrentState(IState newState) {
		if(currentState != null) {
			currentState.onExit();
		}
		IState tempState = currentState;
		currentState = newState;
		lastState = tempState;
		currentState.onEnter();
	}
	
	public static IState getCurrentState() {
		return currentState;
	}

	public static void rollBackToLastState() {
		currentState.onExit();
		IState tempState = currentState;
		currentState = lastState;
		lastState = tempState;
	}
}
