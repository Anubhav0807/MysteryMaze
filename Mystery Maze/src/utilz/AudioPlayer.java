package utilz;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import static utilz.Constants.AudioConsts.*;

public class AudioPlayer {
	
	private HashMap<String, Clip> audioMap;
	
	public AudioPlayer() {		
		audioMap = new HashMap<String, Clip>();
		audioMap.put(INTRO_MUSIC, loadFile(INTRO_MUSIC));
		audioMap.put(BG_MUSIC, loadFile(BG_MUSIC));
	}
	
	private Clip loadFile(String fileName) {
		URL url = getClass().getResource(fileName);
		AudioInputStream audio;
		Clip clip = null;
		try {
			audio = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
	
	public void play(String fileName) {
		Clip clip = loadFile(fileName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);  // Rewind to the beginning
            clip.start();
        } else {
        	System.out.println("Failed to play: " + fileName);
        }
	}
	
	public void playOnLoop(String fileName) {
		Clip clip = audioMap.get(fileName);
		if (clip != null) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.setFramePosition(0);  // Rewind to the beginning
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
        	System.out.println("Failed to play: " + fileName);
        }
	}
	
	public void stop(String filename) {
		audioMap.get(filename).stop();
	}

}
