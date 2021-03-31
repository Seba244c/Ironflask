package dk.sebsa.ender.game;

import dk.sebsa.ironflask.engine.audio.AudioClip;
import dk.sebsa.ironflask.engine.audio.AudioSource;

public class MusicManager {
	public enum Songs {
		MainMenu,
		Game
	}
	
	public static AudioSource currentSource;
	
	public static AudioSource menuMusic;
	public static AudioSource gameMusic;
	
	public static float musicLevel = 1f;
	
	public static void init() {
		gameMusic = new AudioSource(true, false);
		menuMusic = new AudioSource(true, false);
		gameMusic.setClip(AudioClip.getClip("game-music"));
		menuMusic.setClip(AudioClip.getClip("menu-music"));
	}
	
	private static AudioSource getMusic(Songs song) {
		if(song == Songs.MainMenu) {
			return menuMusic;
		} else {
			return gameMusic;
		}
	}
	
	public static void setLevel(float f) {
		currentSource.setGain(musicLevel);
		musicLevel = f;
	}
	
	public static void start(Songs song) {
		AudioSource newSource = getMusic(song);
		if(newSource.equals(currentSource)) return;
		if(currentSource != null && currentSource.isPlaying()) currentSource.pause();
		currentSource = newSource;
		currentSource.setGain(musicLevel);
		currentSource.play();
	}
}
