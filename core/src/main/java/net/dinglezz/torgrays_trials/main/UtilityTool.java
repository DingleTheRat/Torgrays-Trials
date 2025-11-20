package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.event.Event;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class UtilityTool {

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(original, 0, 0, width, height, null);
        graphics2D.dispose();

        return scaledImage;
    }

    public static JSONObject getJsonObject(String filePath) {
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("Warning: \"" + filePath + "\" is not a valid path.");
                return null;
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return new JSONObject(stringBuilder.toString());
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> T generateEntity(String path, TilePoint tilePoint) {
        try {
            Class<?> clazz = Class.forName(path);
            return (T) clazz.getDeclaredConstructor(TilePoint.class).newInstance(tilePoint);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException | ClassCastException exception) {
            throw new RuntimeException(exception);
        }
    }
    @SuppressWarnings("unchecked")
    public static <T extends Entity> T generateEntity(String path, TilePoint tilePoint, String lootTable) {
        try {
            Class<?> clazz = Class.forName(path);
            return (T) clazz.getDeclaredConstructor(TilePoint.class, String.class).newInstance(tilePoint, lootTable);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException | ClassNotFoundException | ClassCastException exception) {
            throw new RuntimeException(exception);
        }
    }
    public static Event generateEvent(String path, String map, int col, int row, JSONObject parameters) {
        TilePoint tilePoint = new TilePoint(map, col, row);
        try {
            Class<?> clazz = Class.forName(path);
            return (Event) clazz.getDeclaredConstructor(TilePoint.class, JSONObject.class).newInstance(tilePoint, parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String[] getResourceFileNames(String directoryPath) {
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(directoryPath)) {
            if (inputStream == null) {
                System.err.println("Warning: \"" + directoryPath + "\" is not a valid path.");
                return new String[0];
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                return bufferedReader.lines().toArray(String[]::new);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // Image serialization
    public static byte[] serializeImage(BufferedImage image) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream); // You can use "jpg", "bmp", etc.
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static BufferedImage deserializeImage(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
