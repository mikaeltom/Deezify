package ulb.models;

import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Song class.
 * This class contains unit tests for the methods in the Song class.
 */
public class TestSong {

    private Song createSong(String title, String artist, String album) {
        return new Song(0, title, artist, album, Duration.seconds(180), "", new ArrayList<>(), "", "", "");
    }

    /**
     * Test if hashCode are equals.
     */
    @Test
    public void testEqualsAndHashCode() {
        Song song1 = new Song(1, "A", "B", "C", Duration.seconds(100), "", new ArrayList<>(), "", "", "");
        Song song2 = new Song(1, "X", "Y", "Z", Duration.seconds(999), "", new ArrayList<>(), "", "", "");
        Song song3 = new Song(2, "A", "B", "C", Duration.seconds(100), "", new ArrayList<>(), "", "", "");

        assertEquals(song1, song2); // same ID
        assertNotEquals(song1, song3);
        assertEquals(song1.hashCode(), song2.hashCode());
    }

    /**
     * Test get and set methods for title.
     */
    @Test
    public void testGetSetTitle() {
        Song song = createSong("Old Title", "Artist", "Album");
        song.setTitle("New Title");
        assertEquals("New Title", song.getTitle());
    }

    /**
     * Test get and set methods for artist.
     */
    @Test
    public void testGetSetArtist() {
        Song song = createSong("Title", "Old Artist", "Album");
        song.setArtist("New Artist");
        assertEquals("New Artist", song.getArtist());
    }

    /**
     * Test get and set methods for album.
     */
    @Test
    public void testGetSetAlbum() {
        Song song = createSong("Title", "Artist", "Old Album");
        song.setAlbum("New Album");
        assertEquals("New Album", song.getAlbum());
    }

    /**
     * Test get and set methods for duration.
     */
    @Test
    public void testGetSetDuration() {
        Song song = createSong("Title", "Artist", "Album");
        Duration newDuration = Duration.seconds(240);
        song.setDuration(newDuration);
        assertEquals(song.getDuration(), newDuration);
    }

    /**
     * Test get and set methods for path.
     */
    @Test
    public void testGetSetPath() {
        Song song = createSong("Title", "Artist", "Album");
        song.setPath("/new/path");
        assertEquals("/new/path", song.getPath());
    }

    /**
     * Test get and set methods for tags.
     */
    @Test
    public void testGetSetTags() {
        ArrayList<Tag> newTags = new ArrayList<>();
        newTags.add(new Tag("Category 3"));
        Song song = createSong("Title", "Artist", "Album");
        song.setTags(newTags);
        assertEquals(newTags, song.getTags());
    }

    /**
     * Test get for metadata.
     */
    @Test
    public void testGetMetadata() {
        Song song = createSong("Title", "Artist", "Album");
        Map<String, String> metadata = song.getMetadata();
        assertNotNull(metadata);
        assertFalse(metadata.isEmpty());
    }

    /**
     * Test adding duplicate tags.
     */
    @Test
    public void testAddDuplicateTagThrows() {
        Song song = createSong("Title", "Artist", "Album");
        Tag tag = new Tag("Workout");
        assertDoesNotThrow(() -> song.addTag(tag));
        assertThrows(Exception.class, () -> song.addTag(tag));
    }

    /**
     * Test non existent tag removal.
     */
    @Test
    public void testRemoveNonexistentTagThrows() {
        Song song = createSong("Title", "Artist", "Album");
        Tag tag = new Tag("DoesNotExist");
        assertThrows(Exception.class, () -> song.removeTag(tag));
    }
}
