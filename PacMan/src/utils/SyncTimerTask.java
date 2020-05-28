package utils;

import java.util.TimerTask;

public class SyncTimerTask extends TimerTask {
	
	private boolean hasStarted = false;
	private Runnable task;
	
	public SyncTimerTask(Runnable task) {
		this.task = task;
	}

	@Override
	public void run() {
		hasStarted = false;
		task.run();
//        MessageQueue.addMessage(pacman, AudioComponent.class.getName(), MessageEnum.INVINCIBLE_END);
		// TODO Auto-generated method stub
		
	}
	
	public boolean hasRunStarted() {
		return hasStarted;
	}
	
	public void startRunning() {
		hasStarted = true;
	}
	

}
