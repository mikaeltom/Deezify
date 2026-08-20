package ulb.dtos;

import ulb.models.Song;

import java.util.List;

/**
 * DTO for a music collection.
 * This interface defines the structure of a music collection DTO,
 * which includes the name of the collection and a list of songs.
 */
public interface MusicCollectionDTO {
    String getName();

    List<Song> getSongs();
}
