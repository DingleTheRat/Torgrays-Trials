package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.UtilityTool;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Image implements Serializable {
	private final byte[] data;
	private transient BufferedImage image;
	public Image(byte[] data) {
		this.data = data;
	}
	public BufferedImage getImage() {
		if (image == null) image = UtilityTool.deserializeImage(data);
		return image;
	}
}
