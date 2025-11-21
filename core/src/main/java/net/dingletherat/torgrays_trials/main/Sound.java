// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Sound {
    public static final HashMap<String, URL> SOUND_LIBRARY = new HashMap<>();
    ArrayList<Clip> clips = new ArrayList<>();
    FloatControl floatControl;
    int volumeScale = 3;
    float volume;

    public static void loadLibrary() {
        // Music
        SOUND_LIBRARY.put("Tech Geek", Sound.class.getResource("/sound/music/tech_geek.wav"));
        SOUND_LIBRARY.put("Umbral Force", Sound.class.getResource("/sound/music/umbral_force.wav"));
        SOUND_LIBRARY.put("Coin Toss", Sound.class.getResource("/sound/music/coin_toss.wav"));
        SOUND_LIBRARY.put("Gloom Over Torgray", Sound.class.getResource("/sound/music/gloom_over_torgray.wav"));
        Main.LOGGER.info("Loaded all music files");

        // SFX
        SOUND_LIBRARY.put("Coin", Sound.class.getResource("/sound/sfx/coin.wav"));
        SOUND_LIBRARY.put("Power Up", Sound.class.getResource("/sound/sfx/power_up.wav"));
        SOUND_LIBRARY.put("Unlock", Sound.class.getResource("/sound/sfx/unlock.wav"));
        SOUND_LIBRARY.put("Hit Monster", Sound.class.getResource("/sound/sfx/hit_monster.wav"));
        SOUND_LIBRARY.put("Receive Damage", Sound.class.getResource("/sound/sfx/receive_damage.wav"));
        SOUND_LIBRARY.put("Cursor", Sound.class.getResource("/sound/sfx/cursor.wav"));
        SOUND_LIBRARY.put("Game Over", Sound.class.getResource("/sound/sfx/game_over.wav"));
        SOUND_LIBRARY.put("Teleport", Sound.class.getResource("/sound/sfx/teleport.wav"));
        SOUND_LIBRARY.put("Swing", Sound.class.getResource("/sound/sfx/swing.wav"));
        Main.LOGGER.info("Loaded all sound files");
    }

	public int getFile(String soundName) {
		try {
			if (SOUND_LIBRARY.get(soundName) != null) {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SOUND_LIBRARY.get(soundName));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				checkVolume();
				clips.add(clip);
				return clips.size() - 1;
			} else {
				Main.LOGGER.warn("Warning: \"{}\" is not a valid sfx.", soundName);
				return -1;
			}
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			Main.handleException(e);
		}
		return -1;
	}
	public void play(int idx) {
		if (idx != -1) {
			Clip clip = clips.get(idx);
			if (clip != null) clip.start();
			else Main.LOGGER.warn("Warning: No clip found to play");
		}
	}
	public void loop(int idx) {
		if (idx != -1) {
			Clip clip = clips.get(idx);
			if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
			else Main.LOGGER.warn("Warning: No clip found to loop");
		}
	}
	public void stop(int idx) {
		if (idx != -1) {
			Clip clip = clips.get(idx);
			if (clip != null) clip.stop();
		}
	}
	public void checkVolume() {
		switch (volumeScale) {
			case 0 -> volume = -80f;
			case 1 -> volume = -20f;
			case 2 -> volume = -12f;
			case 3 -> volume = -5f;
			case 4 -> volume = 1f;
			case 5 -> volume = 6f;
		}
		floatControl.setValue(volume);
	}

	// Static Stuff
	public static Sound music = new Sound();
	public static Sound sfx = new Sound();

	public static void playMusic(String songName) {
		stopMusic();
		int idx = music.getFile(songName);
		music.play(idx);
		music.loop(idx);
	}
	// public static void playMapMusic() {
	// 	stopMusic();
	// 	JSONObject currentMapFile = MapHandler.mapFiles.get(Main.game.currentMap);
	//
	// 	if (currentMapFile.getString("music").equals("default")) {
	// 		playMusic(
	// 				switch (Main.game.environmentManager.lighting.darknessState) {
	// 					case NIGHT, NEW_DUSK, DUSK -> "Umbral Force";
	// 					case GLOOM, LIGHT_GLOOM, DARK_GLOOM -> "Gloom Over Torgray";
	// 				});
	// 	} else {
	// 		try {
	// 			playMusic(currentMapFile.getString("music"));
	// 		} catch (JSONException jsonException) {
	// 			currentMapFile = MapHandler.mapFiles.get("Disabled");
	// 			playMusic(currentMapFile.getString("music"));
	// 		}
	// 	}
	// }
	// public static void playMapMusic(States.DarknessStates darknessState) {
	// 	stopMusic();
	// 	JSONObject currentMapFile = MapHandler.mapFiles.get(Main.game.currentMap);
	//
	// 	if (currentMapFile.getString("music").equals("default")) {
	// 		playMusic(
	// 				switch (darknessState) {
	// 					case DUSK -> "Umbral Force";
	// 					case NEW_DUSK -> "Gloom Over Torgray";
	// 					default -> "Dark Mystery";
	// 				});
	// 	} else {
	// 		try {
	// 			playMusic(currentMapFile.getString("music"));
	// 		} catch (JSONException jsonException) {
	// 			currentMapFile = MapHandler.mapFiles.get("Disabled");
	// 			playMusic(currentMapFile.getString("music"));
	// 		}
	// 	}
	// }
	public static void stopMusic() {
		if (!music.clips.isEmpty()) music.stop(0);
	}
	public static void playSFX(String sfxName) {
		sfx.play(sfx.getFile(sfxName));
	}
}
