// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.awt.*;
import java.util.HashMap;

public class Images {
	static HashMap<String, Image> imageCache = new HashMap<>();
	// Load an image from the drawable folder or, if queried before, from the cache
	public static Image loadImage(String imageName) {
		if (imageCache.containsKey(imageName)) {
			return imageCache.get(imageName);
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(Images.class.getResource("/drawable/" + imageName + ".png"));
		imageCache.put(imageName, image);
		return image;
	}
}
