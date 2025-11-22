// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import net.dingletherat.torgrays_trials.Main;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

/** A class that holds data of BufferedImages, and a BufferedImage
 * BufferedImages are not serializable, meaning if we want to save the game, we can't, it will throw an exception.
 * This class aims to solve that. Every instance of this class stores the image and also a BufferedImage that can't be saved.
 * You can get the BufferedImage using the {@code getImage} method.
 * An instance of this class is created using the {@code loadImage} method in the static version of this class, because the constructor is private.
 * It's private because this method uses a cache system which is more efficient than crating a bunch of new instances of this class **/
public class Image implements Serializable {
    private static final HashMap<String, Image> imageCache = new HashMap<>();

    private byte[] data;
    private transient BufferedImage image;
    private transient Texture texture;

    /** Loads an image from either a cache or creates an entirely new one.
     * If you are loading an entirely new image, it will create a new instance of this class.
     * However, if the same image was already loaded, it will just use that instance. Instances are stored in a private cache.
     * <p>
     * @param imageName The name of the image you want to load in.
     * The image is loaded in from the {@code /drawable/} directory, so put your image in there
     * If you want to load an image from a directory inside the {@code /drawable/} continue with the path like so: {@code tiles/my_image}
     * <p>
     * @return An instance of this class, Image. If you try to use this to render an image, it likely won't work.
     * Instead, call the {@code getImage} method from the instance of the class to get a buffered image. This will work if you try to draw it. **/
    public static Image loadImage(String imageName) {
        // Check if the image is located in the image cache, if it is, return it to save resources
        if (imageCache.containsKey(imageName)) return imageCache.get(imageName);

        // Sadly, there is no way around this, we have to load an image D:
        Image image = new Image(imageName);

        // Added to the cache, so we don't have to do this again and return the image
        imageCache.put(imageName, image);
        return image;
    }

    private Image(String imageName) {
        /* Entering try and catch zone because this part involves ImageIO.
        The catch also has a NullPointerException because "requireNonNull" is used in the code*/
        try {
            // Get the imageStream that we will use for the image. It's separately instantiated as it will be used for null checking
            FileHandle file = Gdx.files.internal("drawable/" + imageName + ".png");

            /* Make sure the imageStream is a member of "/drawable/" and is a png. If not, use the disabled imageStream and warn.
            * The way we find out that is by checking if the imageStream is null. If it is, it's likely not a valid member.
            * However, if the imageName was just "", don't warn as it may be a result of an error
            In any of these cases, we use a placeholder image called "disabled" to indicate that something went wrong. */
            if (imageName.isEmpty()) {
                image = ImageIO.read(Gdx.files.internal("drawable/disabled.png").read());
                data = serializeImage(image);
                return;
            }
            if (file == null) {
                Main.LOGGER.warn("{} is not a valid member of \"/drawable/\". ", imageName);
                image = ImageIO.read(Gdx.files.internal("drawable/disabled.png").read());
                data = serializeImage(image);
                return;
            }

            // If all checks have passed, then set the image
            image = ImageIO.read(file.read());
        } catch (IOException | NullPointerException e) {
            Main.handleException(e);
        }

        /* Finally, deserialize the image and put it into the data
        Since buffered image is not Serializable, it's not saved when the game is saved, so we must use data to load it*/
        data = serializeImage(image);
    }

    /** @return A buffered image that is stored in instances the {@code Image} class.
    If a game was loaded in, it turns the data into a buffered image, then returns it. **/
    public BufferedImage getImage() {
        // In the case that the game was loaded (meaning the image is null), set the image to the unserialized data
        if (image == null) image = deserializeImage(data);
        return image;
    }

    /** @return A texture that is stored in instances the {@code Image} class.
    If a game was loaded in, it turns the data into a texture, then returns it. **/
    public Texture getTexture() {
        // In the case that the game was loaded (meaning the image is null), set the image to the unserialized data
        if (texture == null) texture = bufferedImageToTexture(getImage());
        return texture;
    }

    /// @return Change a buffered image to a {@code Texture}
    public static Texture bufferedImageToTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a Pixmap in RGBA8888 format
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Copy pixels (BufferedImage is top-down, Pixmap is bottom-up)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);

                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8)  & 0xff;
                int b = (argb)       & 0xff;

                int rgba = (r << 24) | (g << 16) | (b << 8) | a;

                pixmap.drawPixel(x, height - y - 1, rgba);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /** Scales the image to your preferred size while updating all the data required to do so.
     * <p>
     * @param width The width you want the scaled image to be.
     * @param height The height you want the scaled image to be.
     **/
    public void scaleImage(int width, int height) {
        // Create a new BufferedImage with the adjusted height and width
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        /* Create a graphics object to draw the scaled image and draw the original image scaled to the new dimensions
        This is because we need a Graphics object to perform the actual drawing and scaling operations on the new BufferedImage */
        Graphics graphics = scaledImage.createGraphics();
        graphics.drawImage(image, 0, 0, width, height, null);

        // Dispose of graphics context to free up system resources (it's a good practice)
        graphics.dispose();

        // Update the image and data to the new dimensions
        image = scaledImage;
        data = serializeImage(image);
        texture = bufferedImageToTexture(image);
    }

    public static byte[] serializeImage(BufferedImage image) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream); // You can use "jpg", "bmp", etc.
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Main.handleException(e);
            return null;
        }
    }
    public static BufferedImage deserializeImage(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            Main.handleException(e);
            return null;
        }
    }
}
