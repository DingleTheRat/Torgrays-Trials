package net.dinglezz.torgrays_trials.effect;

import net.dinglezz.torgrays_trials.entity.Image;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Effect {
    public String name;
    public int time;
    public Image image;
    public Mob host;

    private int counter = 0;

    public Effect(String name, int duration, Mob host) {
        this.name = name;
        time = duration;
        registerEffectImage("disabled");
        this.host = host;

        onApply();
    }

    public void registerEffectImage(String path) {
        BufferedImage image;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/" + path + ".png"));
            } catch (IllegalArgumentException | IOException exception) {
                System.err.println("Warning: \"" + path + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = UtilityTool.scaleImage(image, (Main.game.tileSize) + (Main.game.tileSize / 4), (Main.game.tileSize) + (Main.game.tileSize / 4));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // Serialize the image to a byte array
        this.image = new Image(UtilityTool.serializeImage(image));
    }

    public void update() {
        counter++;

        if (counter == 60) {
            during();

            counter = 0;
            time--;
            if (time <= 0) {
                host.effects.remove(this);
                onEnd();
            }
        }
    }

    public abstract void onApply();
    public abstract void during();
    public abstract void onEnd();
}
