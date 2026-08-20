package ulb.utils;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class responsible for caching images.
 * This class ensures that images are loaded only once and reused when needed.
 */
public class ImageCache {

    private static ImageCache instance;

    private final Map<String, Image> imageCache = new HashMap<>();

    private ImageCache() {
    }

    /**
     * Returns the singleton instance of the ImageCache.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of ImageCache
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    /**
     * Retrieves a cached image for the given image path.
     * If the image is not already cached, it will be loaded and cached.
     * The image is retrieved via the main controller listener.
     *
     * @param imagePath The path to the image file.
     * @return The cached image.
     */
    public Image manageCachedImage(String imagePath) {
        if (!imageCache.containsKey(imagePath)) {
            Image image = new Image(imagePath, 150, 150, true, true);
            imageCache.put(imagePath, image);
        }
        return imageCache.get(imagePath);
    }
}
