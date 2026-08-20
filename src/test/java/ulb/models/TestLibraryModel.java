package ulb.models;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the LibraryModel class.
 * This class contains unit tests for the methods in the LibraryModel class.
 */
public class TestLibraryModel {

    /**
     * Test for the getMusicFiles() method.
     * @throws IOException If an error occurs while reading the music files.
     */
    @Test
    public void testGetMusicFiles() throws IOException {
        List<File> musicFiles = LibraryModel.getMusicFiles();

        assertNotNull(musicFiles, "The list of music files should not be null.");
        assertTrue(musicFiles.size() >= 0, "The list of music files should contain 0 or more files.");
    }

    /**
     * Test for the getMusicFiles() method with an empty directory.
     */
    @Test
    public void testIsAudioFile() {
        File mp3File = new File("test.mp3");
        File wavFile = new File("test.wav");
        File txtFile = new File("test.txt");
        File songFile = new File("test.song");
        File mp3FileUppercase = new File("test.MP3");

        assertTrue(LibraryModel.isAudioFile(mp3File), "The .mp3 file should be recognized as audio.");
        assertTrue(LibraryModel.isAudioFile(wavFile), "The .wav file should be recognized as audio.");
        assertTrue(LibraryModel.isAudioFile(mp3FileUppercase), "The .MP3 file should be recognized as audio.");
        assertFalse(LibraryModel.isAudioFile(txtFile), "The .txt file should not be recognized as audio.");
        assertFalse(LibraryModel.isAudioFile(songFile), "The .song file should not be recognized as audio.");
    }

    /**
     * Test for the getMusicFile() method.
     * @throws IOException If an error occurs while reading the music file.
     */
    @Test
    public void testGetMusicFile() throws IOException {
        File validMp3File = new File("src/test/resources/music/test.mp3");

        assertTrue(validMp3File.exists(), "The test.mp3 file should exist in the music directory.");

        LibraryModel.MusicFileInfo fileInfo = LibraryModel.getMusicFile(validMp3File);

        assertNotNull(fileInfo, "The file information should not be null.");
        assertEquals("test", fileInfo.musicName, "The file name should match the music name.");
        assertTrue(fileInfo.durationInSeconds > 0, "The duration of the audio file should be greater than 0.");
    }

    /**
     * Test for the getAudioDuration() method.
     */
    @Test
    public void testGetAudioDuration() {
        File mp3File = new File("src/test/resources/music/test.mp3");
        File wavFile = new File("src/test/resources/music/test.wav");
        assertTrue(mp3File.exists(), "The test.mp3 file should exist.");
        assertTrue(wavFile.exists(), "The test.wav file should exist.");

        int mp3Duration = LibraryModel.getAudioDuration(mp3File);
        int wavDuration = LibraryModel.getAudioDuration(wavFile);

        assertEquals(45, mp3Duration, "The duration of the MP3 file should be 45s.");
        assertEquals(45, wavDuration, "The duration of the WAV file should be 45s.");
    }

    /**
     * Test for the getMusicFile() method with an invalid file.
     */
    @Test
    public void testGetMusicFileInvalidFile() {
        File invalidFile = new File("src/main/resources/music/invalid.txt");

        assertThrows(IOException.class, () -> {
            LibraryModel.getMusicFile(invalidFile);
        }, "An exception should be thrown for an invalid file.");

        assertThrows(IOException.class, () -> {
            LibraryModel.getMusicFile(null);
        }, "An exception should be thrown for a null file.");
    }

    /**
     * Test for the getFileExtension() method.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals("mp3", LibraryModel.getFileExtension("song.mp3"), "The file extension should be mp3.");
        assertEquals("wav", LibraryModel.getFileExtension("song.wav"), "The file extension should be wav.");
        assertEquals("flac", LibraryModel.getFileExtension("song.flac"), "The file extension should be flac.");

        // Edge cases
        assertEquals("mp3", LibraryModel.getFileExtension("song.with.dots.mp3"), "The file extension should be mp3.");
        assertEquals("MP3", LibraryModel.getFileExtension("song.MP3"), "The file extension should be MP3.");
        assertEquals("", LibraryModel.getFileExtension("songWithNoExtension"), "Files without extension should return empty string.");
        assertEquals("mp3", LibraryModel.getFileExtension(".hidden.mp3"), "Hidden files with extension should work correctly.");
    }

    /**
     * Test for the string() method of the MusicFileInfo class.
     */
    @Test
    public void testMusicFileInfoToString() throws IOException {
        File validMp3File = new File("src/test/resources/music/test.mp3");
        LibraryModel.MusicFileInfo fileInfo = LibraryModel.getMusicFile(validMp3File);

        String expectedString = String.format("Name: %s, Artist: %s, Type: %s, Path: %s, Duration: %d sec",
                fileInfo.musicName, fileInfo.artist, fileInfo.type, fileInfo.path, fileInfo.durationInSeconds);

        assertEquals(expectedString, fileInfo.toString(), "The toString() method did not return the expected string.");
    }
}
