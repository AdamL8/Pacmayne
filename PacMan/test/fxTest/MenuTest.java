package fxTest;
import java.awt.AWTException;
import java.awt.Robot;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;
import rendering.RenderingSystem;
import states.ControlMenuState;
import states.HighScoresMenuState;
import states.MainMenuGameState;
import states.StateManager;

public class MenuTest extends ApplicationTest{
	
	RenderingSystem renderingSystem = new RenderingSystem();
	
	 @Override
	    public void start(Stage stage) throws Exception {
		 System.out.println("START");
		 renderingSystem.start(stage);
	    }
	 
	 @Test
	    public void menuTest() throws InterruptedException, AWTException {
	    	Thread.sleep(200);
		    Robot r = new Robot();
		    r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_ENTER);
	    	Thread.sleep(500);
	        Assert.assertTrue(StateManager.getCurrentState().getClass().getName().equals(HighScoresMenuState.class.getName()));
	        r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	 r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_ENTER);
	    	Thread.sleep(200);
	    	Assert.assertTrue(StateManager.getCurrentState().getClass().getName().equals(MainMenuGameState.class.getName()));
	    	r.keyPress(java.awt.event.KeyEvent.VK_UP);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_ENTER);
	    	Thread.sleep(200);
	    	Assert.assertTrue(StateManager.getCurrentState().getClass().getName().equals(ControlMenuState.class.getName()));
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	 r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_ENTER);
	    	Thread.sleep(500);
	    	Assert.assertTrue(StateManager.getCurrentState().getClass().getName().equals(MainMenuGameState.class.getName()));
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	Thread.sleep(200);
	    	r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
	    	
	    }

}
