package ulb.models;

import javafx.util.Duration;
import ulb.dtos.SongDTO;
import ulb.exceptions.tag.InvalidTagError;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a song with metadata including title, artist, album, duration, and
 * associated tags.
 */
public class Song implements SongDTO {
    private final int id;
    private final String videoPath;
    private String title;
    private String artist;
    private String album;
    private Duration duration;
    private String path;
    private List<Tag> tags;
    private String imagePath;
    private String lyricsPath;

    /**
     * Constructs a song with full metadata.
     *
     * @param id        Unique identifier.
     * @param title     Song title.
     * @param artist    Song artist.
     * @param album     Album name.
     * @param duration  Duration of the song.
     * @param path      File path to the song.
     * @param tags      List of associated tags.
     * @param imagePath Image path.
     * @param videoPath Video path.
     */
    public Song(int id, String title, String artist, String album, Duration duration,
                String path, List<Tag> tags, String imagePath, String videoPath, String lyricsPath) {
        this.id = id;
        this.title = title != null ? title : "Unknown Title";
        this.artist = artist != null ? artist : "Unknown Artist";
        this.album = album != null ? album : "Unknown Album";
        this.duration = duration != null ? duration : Duration.ZERO;
        this.path = path != null ? path : "Unknown Path";
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.imagePath = imagePath != null ? imagePath : "src/main/resources/img/no-cover/no-cover1.jpg"; // default image
        this.videoPath = videoPath != null ? videoPath : "Unknown Video Path";
        this.lyricsPath = lyricsPath != null ? lyricsPath : "Unknown Lyrics Path";
    }

    /**
     * Retrieves the unique identifier for the song.
     *
     * @return The unique identifier for the song.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Retrieves the title of the song.
     *
     * @return The title of the song.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title The new title of the song.
     * @throws IllegalArgumentException if the title is null or empty.
     */
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
    }

    /**
     * Retrieves the artist of the song.
     *
     * @return The artist of the song.
     */
    @Override
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the artist of the song.
     * <p>
     * If the given artist is null or empty, a warning message is printed to
     * the console and the artist is not changed.
     *
     * @param artist The new artist of the song.
     */
    public void setArtist(String artist) {
        if (artist != null && !artist.trim().isEmpty()) {
            this.artist = artist;
        }
    }

    /**
     * Retrieves the album of the song.
     *
     * @return The album of the song.
     */
    @Override
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the album of the song.
     * <p>
     * If the given album is null or empty, a warning message is printed to
     * the console and the album is not changed.
     *
     * @param album The new album of the song.
     */
    public void setAlbum(String album) {
        if (album != null && !album.trim().isEmpty()) {
            this.album = album;
        }
    }

    /**
     * Retrieves the duration of the song.
     *
     * @return The duration of the song. If the duration is unknown, the
     * returned duration is zero.
     */
    @Override
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song.
     * <p>
     * If the given duration is null or not positive, a warning message is printed
     * to
     * the console and the duration is not changed.
     *
     * @param duration The new duration of the song.
     */
    public void setDuration(Duration duration) {
        if (duration != null && duration.greaterThan(Duration.ZERO)) {
            this.duration = duration;
        }
    }

    /**
     * Retrieves the file path to the song.
     *
     * @return The file path to the song.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Sets the file path to the song.
     * <p>
     * If the given path is null or empty, a warning message is printed to
     * the console and the path is not changed.
     *
     * @param path The new file path to the song.
     */
    public void setPath(String path) {
        if (path != null && !path.trim().isEmpty()) {
            this.path = path;
        }
    }

    /**
     * Retrieves the tags associated with the song.
     *
     * @return A list of tags associated with the song.
     */

    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the tags associated with the song.
     * If the provided list is null, the tags are set to an empty list.
     *
     * @param tags List of tags to associate with the song.
     */
    public void setTags(List<Tag> tags) {
        this.tags = (tags != null) ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public List<String> getTagsNames() {
        List<String> tagsNames = new ArrayList<>();
        tags.forEach(tag -> tagsNames.add(tag.getName()));
        return tagsNames;
    }

    /**
     * Checks if the image path is valid and exists.
     * <p>
     * If the image path is null, does not exist, or is set to the default directory,
     * it is updated to the default "no cover" image path.
     */
    private void checkImagePath() {
        if (this.imagePath == null || !Files.exists(Paths.get(this.imagePath))
                || this.imagePath.equals("src/main/resources/img/")) {
            setImagePath("src/main/resources/img/no-cover/no-cover1.jpg");
        }
    }

    /**
     * Retrieves the path of the song's cover image.
     * If the image path does not exist or is set to the default directory,
     * it is updated to the default "no cover" image path.
     *
     * @return the path of the song's cover image
     */
    @Override
    public String getImagePath() {
        checkImagePath();
        return this.imagePath;
    }

    /**
     * Sets the path of the song's cover image.
     * If the provided image path is null, it defaults to the "no cover" image path.
     *
     * @param imagePath The path to set for the song's cover image.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "src/main/resources/img/no-cover/no-cover1.jpg";
    }


    /**
     * Retrieves the path to the lyrics file associated with the song.
     *
     * @return The path to the lyrics file as a String.
     */
    @Override
    public String getLyricsPath() {
        return this.lyricsPath;
    }

    /**
     * Sets the path to the lyrics file associated with the song.
     * If the provided lyrics path is null, it defaults to "Unknown Lyrics Path".
     *
     * @param lyricsPath The path to set for the song's lyrics file.
     */
    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath != null ? lyricsPath : "Unknown Lyrics Path";
    }

    /**
     * Adds a tag to the song if it's not already in the list.
     *
     * @param tag Tag to add.
     */
    public void addTag(Tag tag) throws InvalidTagError {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
        } else {
            throw new InvalidTagError("tag_null_exception");
        }
    }

    /**
     * Removes a tag from the song if it exists.
     *
     * @param tag Tag to remove.
     */
    public void removeTag(Tag tag) throws InvalidTagError {
        if (tag != null && tags.contains(tag)) {
            tags.remove(tag);
        } else {
            throw new InvalidTagError("tag_empty_exception");
        }
    }

    /**
     * Retrieves song metadata as a key-value map.
     *
     * @return Map containing metadata fields and values.
     */
    @Override
    public Map<String, String> getMetadata() {
        return Map.of(
                "Title", Optional.ofNullable(title).orElse("N/A"),
                "Artist", Optional.ofNullable(artist).orElse("N/A"),
                "Album", Optional.ofNullable(album).orElse("N/A"),
                "Duration", Optional.ofNullable(duration).map(Duration::toString).orElse("N/A"),
                "Path", Optional.ofNullable(path).orElse("N/A"),
                "Tags", tags.isEmpty() ? "No Tags" : tagsToString(),
                "Images", Optional.ofNullable(imagePath).orElse("N/A"));
    }

    /**
     * Updates song metadata from a given key-value map.
     *
     * @param metadata Map containing metadata fields and values.
     */
    public void setMetadata(Map<String, String> metadata) {
        metadata.forEach(this::setData);
    }

    /**
     * Updates a specific metadata field.
     *
     * @param fieldName The field to update.
     * @param value     The new value.
     */
    public void setData(String fieldName, String value) {
        if (value == null || value.trim().isEmpty())
            return;
        switch (fieldName.toLowerCase()) {
            case "title" -> setTitle(value);
            case "artist" -> setArtist(value);
            case "album" -> setAlbum(value);
            case "duration" -> setDuration(Duration.seconds(Double.parseDouble(value)));
            case "images" -> setImagePath(value);
            case "path" -> setPath(value);
        }
    }

    /**
     * Converts tags list to a formatted string.
     *
     * @return String representation of tags.
     */
    @Override
    public String tagsToString() {
        return tags.isEmpty() ? "No Tags" : tags.stream().map(Tag::toString).collect(Collectors.joining(", "));
    }

    /**
     * Compares this song to the specified object. The result is true if and only if
     * the argument is not null and is a Song object that has the same ID as this
     * song.
     *
     * @param obj The object to compare this Song against.
     * @return true if the given object represents a Song equivalent to this song,
     * false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Song other = (Song) obj;
        return this.id == other.id;
    }

    /**
     * Generates a hash code for the song.
     *
     * @return Hash code based on song ID.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}