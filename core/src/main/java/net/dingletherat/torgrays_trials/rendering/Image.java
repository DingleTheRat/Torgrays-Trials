// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import net.dingletherat.torgrays_trials.Main;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

/** A class that holds data of {@link Texture}
 * {@link Texture} is not serializable, meaning if we want to save the game, we can't, it will throw an exception.
 * This class aims to solve that. Every instance of this class stores the image and also a {@link Texture} that can't be saved.
 * You can get the {@link Texture} using the {@code getImage} method.
 * An instance of this class is created using the {@code loadImage} method in the static version of this class, because the constructor is private.
 * It's private because this method uses a cache system which is more efficient than crating a bunch of new instances of this class **/
public class Image implements Serializable {
    private static final HashMap<String, Image> imageCache = new HashMap<>();

    private final byte[] data;
    private transient Texture image;
    private int width = 16;
    private int height = 16;

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

        // Sadly, there is no way around this; we have to load an image D:
        Image image = new Image(imageName);

        // Added to the cache, so we don't have to do this again and return the image
        imageCache.put(imageName, image);
        return image;
    }

    private Image(String imageName) {
        // Get the file that we will use for the image. It's separately instantiated as it will be used for null checking
        FileHandle file = Gdx.files.internal("drawable/" + imageName + ".png");

        /*
         * Make sure the file is a member of "/drawable/" and is a png. If not, use the disabled file and warn.
         * The way we find out that is by checking if the file is null. If it is, it's likely not a valid member.
         * However, if the imageName was just "", don't warn as it may be a result of an error
         * In any of these cases, we use a placeholder image called "disabled" to indicate that something went wrong.
         */
        if (imageName.isEmpty()) {
            image = new Texture(Gdx.files.internal("drawable/disabled.png"));
            data = serializeImage(image);
            return;
        }
        if (file == null) {
            Main.LOGGER.warn("{} is not a valid member of \"/drawable/\". ", imageName);
            image = new Texture(Gdx.files.internal("drawable/disabled.png"));
            data = serializeImage(image);
            return;
        }

        // If all checks have passed, then set the image
        image = new Texture(file);

        /* Finally, deserialize the image and put it into the data
        Since buffered image is not Serializable, it's not saved when the game is saved, so we must use data to load it*/
        data = serializeImage(image);
    }

    /** @return A texture that is stored in instances the {@link Image} class.
    If a game was loaded in, it turns the data into a texture, then returns it. **/
    public Texture getTexture() {
        // In the case that the game was loaded (meaning the image is null), set the image to the unserialized data
        if (image == null) image = deserializeImage(data, height, width);
        return image;
    }

    /** Scales the image to your preferred size while updating all the data required to do so.
     * <p>
     * @param width The width you want the scaled image to be.
     * @param height The height you want the scaled image to be.
     **/
    public void scaleImage(int width, int height) {
        // Ensure the pixmap is available
        if (!image.getTextureData().isPrepared()) image.getTextureData().prepare();

        Pixmap pixmap = image.getTextureData().consumePixmap();

        // Create new empty pixmap with target size
        Pixmap scaledPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Draw and scale the original image into the new pixmap
        scaledPixmap.drawPixmap(pixmap,
            0, 0, pixmap.getWidth(), pixmap.getHeight(), // source
            0, 0, width, height // destination
        );

        // Create an image from the scaled pixmap
        Texture scaledImage = new Texture(scaledPixmap);

        // Cleanup
        pixmap.dispose();
        scaledPixmap.dispose();

        // Update height and width
        this.height = height;
        this.width = width;

        image = scaledImage;
    }

    public static byte[] serializeImage(Texture image) {
        // Ensure the pixmap is available
        if (!image.getTextureData().isPrepared()) image.getTextureData().prepare();

        // Get Pixmap from texture
        Pixmap pixmap = image.getTextureData().consumePixmap();

        // Convert to a byte array
        ByteBuffer buffer = pixmap.getPixels();
        buffer.rewind(); // reset to start
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        pixmap.dispose();
        return bytes;
    }
    public static Texture deserializeImage(byte[] data, int height, int width) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.getPixels().put(data);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }
}
