package net.dingletherat.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.main.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Darkness {
	/// Add entities that emit light here. These entities must have the "light_radius" and "light_intensity"
	/// properties in the {@code properties} variable set to proper float values.
	private final ArrayList<Entity> lightSources = new ArrayList<>();
	public float ambientDarkness = 0.9f;  // How dark it is without lights (0.0 = no darkness, 1.0 = complete darkness)
	
	public void addLightSource(Entity entity) {
		lightSources.add(entity);
		if (entity.properties.containsKey("light_radius")) {
			try {
				float radius = (float) (entity.properties.get("light_radius"));
				entity.properties.put("light_radius", radius);
			} catch (NumberFormatException e) {
				Main.LOGGER.warn("Entity {} light_radius property is not a number. Setting default.", entity.name);
				entity.properties.put("light_radius", 100.0f); // Default radius
			}
		} else {
			Main.LOGGER.warn("Entity {} missing light_radius property. Setting default.", entity.name);
			entity.properties.put("light_radius", 100.0f); // Default radius
		}
		if (entity.properties.containsKey("light_intensity")) {
			try {
				float radius = (float) (entity.properties.get("light_intensity"));
				entity.properties.put("light_intensity", radius);
			} catch (NumberFormatException e) {
				Main.LOGGER.warn("Entity {} light_intensity property is not a number. Setting default.", entity.name);
				entity.properties.put("light_intensity", 1.0f); // Default radius
			}
		} else {
			Main.LOGGER.warn("Entity {} missing light_intensity property. Setting default.", entity.name);
			entity.properties.put("light_intensity", 1.0f); // Default radius
		}
	}
	
	public void removeLightSource(Entity entity) {
		lightSources.remove(entity);
	}
	
	public ArrayList<Entity> getLightSources() {
		return lightSources;
	}

	// Draws the darkness overlay
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		// create darkness layer
		BufferedImage darkness = new BufferedImage(Main.game.screenWidth,
				Main.game.screenHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D dg = darkness.createGraphics();
		dg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// fill full-screen ambient darkness
		dg.setComposite(AlphaComposite.SrcOver);
		dg.setColor(new Color(0f, 0f, 0f, ambientDarkness)); // ambientDarkness 0..1
		dg.fillRect(0, 0, darkness.getWidth(), darkness.getHeight());
		
		// erase darkness where lights are (operate on dg, not g2)
		for (Entity lightSource : lightSources) {
			int radius = Math.round((float) lightSource.properties.get("light_radius"));
			float intensity = ((Number) lightSource.properties.get("light_intensity")).floatValue();
			intensity = Math.max(0f, Math.min(1f, intensity)); // clamp
			
			Point center = new Point(
					Math.round((lightSource.x - Main.game.player.cameraX + Main.game.screenWidth / 2f + 8 * Main.game.scale)),
					Math.round((lightSource.y - Main.game.player.cameraY + Main.game.screenHeight / 2f + 8 * Main.game.scale))
			);
			
			float[] dist = {0f, 0.7f, 1f};
			Color[] colors = {
					new Color(0f,0f,0f,1f),
					new Color(0f,0f,0f,0.5f),
					new Color(0f,0f,0f,0f)
			};
			RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
			
			dg.setPaint(p);
			dg.setComposite(AlphaComposite.DstOut.derive(intensity)); // erase alpha from darkness
			dg.fillOval(center.x - radius, center.y - radius, radius*2, radius*2);
		}
		
		dg.dispose();
		
		// draw scene should have been drawn before calling this method or earlier in this method
		g2.drawImage(darkness, 0, 0, null);
		g2.dispose();
	}
}