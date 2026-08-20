package ulb.dtos;

import javafx.util.Duration;
import ulb.models.Tag;

import java.util.List;
import java.util.Map;

/**
 * DTO for a song.
 * This interface defines the structure of a song DTO,
 * which includes the title, id, artist, album, duration, path, tags, image path, lyrics path, and metadata.
 */
public interface SongDTO {
    String getTitle();

    int getId();

    String getArtist();

    String getAlbum();

    Duration getDuration();

    String getPath();


    List<String> getTagsNames();

    String getImagePath();

    String getLyricsPath();

    Map<String, String> getMetadata();

    String tagsToString();

    List<Tag> getTags();
}
