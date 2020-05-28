package components;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import systemThreads.MessageEnum;

public class AudioComponent implements IComponent{
	
	private static Logger logger = Logger.getAnonymousLogger();
	private final Map<MessageEnum, AudioInputStream> soundMap;
	private Clip clipWaka;
	private Clip clipInvincible;
	private boolean playingWaka = false;
	private boolean playingInvincible = false;
	private FloatControl gainControl;
	private double volume = 0.5;
	private boolean isMuted = false;
	
	public AudioComponent(Map<MessageEnum, String> soundMap) {
		this.soundMap = new HashMap<MessageEnum, AudioInputStream>();
		
		try {
			AudioInputStream inputStream;			
			
			for (Map.Entry<MessageEnum, String> entry : soundMap.entrySet()) {
				inputStream = AudioSystem.getAudioInputStream(new File(entry.getValue()));
				this.soundMap.put(entry.getKey(), inputStream);
			}	
		
			clipWaka = AudioSystem.getClip();
			clipWaka.open(this.soundMap.get(MessageEnum.EATEN));
			clipInvincible = AudioSystem.getClip();
			clipInvincible.open(this.soundMap.get(MessageEnum.INVINCIBLE_START));
			gainControl = (FloatControl) clipWaka.getControl(FloatControl.Type.MASTER_GAIN);

			clipWaka.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					if (event.getType() != Type.STOP) {						
						return;
					} else if (event.getType() == Type.STOP) {
						clipWaka.setFramePosition(0);
						playingWaka = false;
					}
				}
			});
			clipInvincible.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					if (event.getType() != Type.STOP) {						
						return;
					} else if (event.getType() == Type.STOP) {
						clipInvincible.setFramePosition(0);
						playingInvincible = false;
					}
				}
			});
		} catch (LineUnavailableException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (UnsupportedAudioFileException e) {
			logger.log(Level.SEVERE, e.getMessage());;
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public void playWaka() {
		if(!playingWaka) {
			playingWaka = true;
			clipWaka.start();
		}
	}
	
	public void playInvincible() {
		if(!playingInvincible) {
			playingInvincible = true;
			clipInvincible.start();
		}
	}
	
	public void stopInvincible() {
		if (playingInvincible) {
			clipInvincible.stop();
		}
	}
	
	public void increaseVolume() {
		if(volume < 1.0) {
			volume += 0.1;
			roundVolume();
			setVolume(volume);
		}
	}
	
	public void decreaseVolume() {
		if(volume > 0.0) {
			volume -= 0.1;
		    roundVolume();
		    setVolume(volume);
		}
	}
	
	public void mute() {
		isMuted = !isMuted;
		if(isMuted) {
			setVolume(0.0);
		} else {
			setVolume(volume);
		}
	}
	
	private void roundVolume() { //without rounding, you may end up with very small divergence that make a big difference in DB calculation
		int scale = (int) Math.pow(10, 2);
	    volume = (double) Math.round(volume * scale) / scale;
	}
	
	public void setVolume(double volume) {
		float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
	}
}
