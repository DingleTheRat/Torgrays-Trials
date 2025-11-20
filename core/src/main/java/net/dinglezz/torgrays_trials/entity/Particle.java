package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.Main;

import java.awt.*;

public class Particle extends Entity{
    Entity generator;
    Color color;
    int speed;
    int maxTime;
    int time;
    public boolean exists = true;
    int size;
    int xd;
    int yd;

    public Particle(Entity generator, Color color, int size, int speed, int maxTime, int xd, int yd) {
        super("Particle", null);

        // Position
        int offset = (Main.game.tileSize / 2) - (size / 2);
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxTime = maxTime;
        this.xd = xd;
        this.yd = yd;
        time = maxTime;
    }

    public void update() {
        super.update();
        time--;

        if (time == maxTime / 3) yd--;
        worldX += xd * speed;
        worldY += yd * speed;
        if (time == 0) exists = false;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int screenX = worldX - Main.game.player.worldX + Main.game.player.screenX;
        int screenY = worldY - Main.game.player.worldY + Main.game.player.screenY;

        graphics2D.setColor(color);
        graphics2D.fillRect(screenX, screenY, size, size);
    }
}
