package states;

import javafx.scene.input.KeyCode;
import scenes.NameMenu;

public class HighScoreNameState implements IState {

	private final NameMenu menu;
	
	public HighScoreNameState(NameMenu menu) {
		this.menu = menu;
	}
	

	@Override
	public void handleInput(KeyCode key) {
		switch(key) {
		case ENTER:
			menu.getCurrentItem().activate();
			break;
		case LEFT:
			menu.selectPreviousItem();
			break;
		case RIGHT:
			menu.selectNextItem();
			break;
		case UP:
			menu.selectNextLetter();
			break;
		case DOWN:
			menu.selectPreviousLetter();
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
