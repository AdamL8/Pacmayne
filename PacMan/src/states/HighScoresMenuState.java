package states;

import javafx.scene.input.KeyCode;
import scenes.VerticalMenu;

public class HighScoresMenuState implements IState {

private final VerticalMenu menu;
	
	public HighScoresMenuState(VerticalMenu menu) {
		this.menu = menu;
	}

	@Override
	public void handleInput(KeyCode key) {
		switch(key) {
		case ENTER:
			menu.getCurrentItem().activate();
			break;
		case UP:
			 menu.selectPreviousItem();
			break;
		case DOWN:
			menu.selectNextItem();
			break;
		default:
			break;
		}	
		
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}


}
