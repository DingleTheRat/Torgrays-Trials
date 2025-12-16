// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sounds {
    public static final HashMap<String, Sound> SOUND_LIBRARY = new HashMap<>();
    public static final ArrayList<Long> ACTIVE_MUSIC = new ArrayList<>();

    /** Loads in all the .wav files from the {@code sound/music} and {@code sound/sfx} directories
        and adds them to the {@code SOUND_LIBRARY} hashmap. **/
    public static void loadLibrary() {
		List<String> musicFiles = UtilityTool.getFileNames("sound/music", ".wav");

        // Add in all sound files in the music folder to the SOUND_LIBRARY
        musicFiles.forEach(fileName -> {
            // Get the name to put into the hashmap as a key. The name will just be the filename
            String name = fileName.replace(".wav", "");

            SOUND_LIBRARY.put(name,
                    // Generate a new sound with the filePath to music and the fileName
                    Gdx.audio.newSound(Gdx.files.internal("sound/music/" + fileName)));
        });
        Main.LOGGER.info("Loaded " + musicFiles.size() + " music files");

        // Now, for sfx files
		List<String> sfxFiles = UtilityTool.getFileNames("sound/sfx", ".wav");

        // Add in all sound files in the sfx folder to the SOUND_LIBRARY
        sfxFiles.forEach(fileName -> {
            // Get the name to put into the hashmap as a key. The name will just be the filename
            String name = fileName.replace(".wav", "");

            SOUND_LIBRARY.put(name,
                    // Generate a new sound with the filePath to sfx and the fileName
                    Gdx.audio.newSound(Gdx.files.internal("sound/sfx/" + fileName)));
        });
        Main.LOGGER.info("Loaded " + sfxFiles.size() + " sfx files");
    }

    /// Disposes of all the sounds inside the {@code SOUND_LIBRARY} hashmap
    public static void dispose() {
        SOUND_LIBRARY.values().stream()
            .forEach(Sound::dispose);
    }

    /**
     * Plainly plays a sound only once from the {@code SOUND_LIBRARY}.
     * <p>
     * @param soundName The name of the sound you want to play.
     * This will be used as a key to obtain the desired sound from the {@code SOUND_LIBRARY}.
     **/
    public static void playSound(String soundName) {
        // Get the sound from the sound libary and play the sound
        Sound sound = SOUND_LIBRARY.get(soundName);
        sound.play();
    }

    /**
     * Plays a sound only once from the {@code SOUND_LIBRARY}, with a random pitch to avoid repeditiveness.
     * <p>
     * @param sfxName The name of the sound you want to play.
     * This will be used as a key to obtain the desired sound from the {@code SOUND_LIBRARY}.
     **/
    public static void playSFX(String sfxName) {
        // Get the sfx from the sound libary
        Sound sfx = SOUND_LIBRARY.get(sfxName);

        // Get a random pitch to play the sfx with, with the pitch being between 0.8f and 1.2f
        float pitch = 0.8f + Main.random.nextFloat(0.4f);

        // Play the sfx
        sfx.play(1f, pitch, 0f);
    }

    /**
     * Plays a looped sound from the {@code SOUND_LIBRARY}.
     * <p>
     * @param songName The name of the sound you want to play.
     * This will be used as a key to obtain the desired sound from the {@code SOUND_LIBRARY}.
     * @return The {@code loopID} of the looped instance of the sound. Use this to stop the loop.
     **/
    public static long playMusic(String songName) {
        // Get the sound from the sound libary and loop it while getting the loopID
        Sound song = SOUND_LIBRARY.get(songName);
        long loopID = song.loop();

        // Add the loopID to an ArrayList of loopIDs, as well as return it
        ACTIVE_MUSIC.add(loopID);
        return loopID;
    }

    /**
     * Stops a desired looped sound from the {@code SOUND_LIBRARY} with a {@code loopID}
     * <p>
     * @param songName The name of the sound you want to play.
     * This will be used as a key to obtain the desired sound from the {@code SOUND_LIBRARY}.
     * @param loopID Each looped sound is given a loopID to stop a looped instance of a song.
     * This will be used to stop the loop.
     **/
    public static void stopMusic(String songName, long loopID) {
        Sound song = SOUND_LIBRARY.get(songName);
        song.stop(loopID);
    }
}
