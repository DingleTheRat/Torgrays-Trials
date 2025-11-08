// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(original, 0, 0, width, height, null);
        graphics2D.dispose();

        return scaledImage;
    }
}
