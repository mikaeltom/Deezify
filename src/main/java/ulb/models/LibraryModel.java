package ulb.models;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LibraryModel {

    private static final List<String> VALID_EXTENSIONS = List.of("mp3", "wav", "flac");
    private static final Path MUSIC_DIR = Paths.get("src/main/resources/music/");

    /**
     * Retrieves a list of audio files from the music directory.
     * <p>
     * This method walks through the specified music directory and collects
     * files that are identified as audio files based on their extensions.
     * If the directory does not exist or is not a directory, an empty list
     * is returned. The method throws an IOException if an I/O error occurs
     * when accessing the directory or its contents.
     *
     * @return a list of audio files in the music directory
     * @throws IOException if an I/O error occurs
     */
    public static List<File> getMusicFiles() throws IOException {
        List<File> musicFiles = new ArrayList<>();
        if (!Files.exists(MUSIC_DIR) || !Files.isDirectory(MUSIC_DIR)) {
            return musicFiles;
        }

        Files.walk(MUSIC_DIR).forEach(path -> {
            File file = path.toFile();
            if (isAudioFile(file)) {
                musicFiles.add(file);
            }
        });

        return musicFiles;
    }

    /**
     * Retrieves metadata for the specified audio file.
     *
     * @param file the audio file
     * @return metadata for the audio file
     * @throws IOException if the file is invalid or does not exist
     */
    public static MusicFileInfo getMusicFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile() || !isAudioFile(file) || getAudioDuration(file) <= 0) {
            throw new IOException("❌ Invalid file: " + file);
        }

        return new MusicFileInfo(file.getName(), getFileExtension(file.getName()),
                "src/main/resources/music/" + file.getParentFile().getName() + "/" + file.getName(),
                getAudioDuration(file));
    }

    /**
     * Checks if the specified file is an audio file.
     *
     * @param file the file to check
     * @return true if the file is an audio file, false otherwise
     */
    protected static boolean isAudioFile(File file) {
        return VALID_EXTENSIONS.contains(getFileExtension(file.getName()).toLowerCase());
    }

    /**
     * Retrieves the file extension for the specified file name.
     *
     * @param fileName the file name
     * @return the file extension
     */
    protected static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
    }

    /**
     * Retrieves the duration of the specified audio file in seconds.
     * Returns 0 if the file is invalid or an error occurs.
     */
    protected static int getAudioDuration(File file) {
        try {
            return switch (getFileExtension(file.getName()).toLowerCase()) {
                case "mp3" -> getMp3Duration(file);
                case "wav", "flac" -> getWAVDuration(file);
                default -> 0;
            };
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retrieves the duration of the specified MP3 file in seconds.
     * Returns 0 if the file is invalid or an error occurs.
     *
     * @param file the MP3 file
     * @return the duration of the MP3 file in seconds
     * @throws Exception if an error occurs
     */
    private static int getMp3Duration(File file) throws Exception {
        try (FileInputStream fileStream = new FileInputStream(file)) {
            Bitstream bitstream = new Bitstream(fileStream);
            Header header = bitstream.readFrame();
            if (header == null)
                return 0;

            int frameSize = header.calculate_framesize();
            return (frameSize > 0) ? (int) (((float) file.length() / frameSize) * header.ms_per_frame() / 1000) : 0;
        }
    }

    /**
     * Retrieves the duration of the specified WAV file in seconds.
     * Returns 0 if the file is invalid or an error occurs.
     *
     * @param file the WAV file
     * @return the duration of the WAV file in seconds
     * @throws Exception if an error occurs
     */
    private static int getWAVDuration(File file) throws Exception {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
            return (int) (audioStream.getFrameLength() / audioStream.getFormat().getFrameRate());
        }
    }

    public static class MusicFileInfo {
        public final int durationInSeconds;
        public final String type, path, musicName, artist;

        public MusicFileInfo(String name, String type, String path, int duration) {
            this.type = type;
            this.path = path;
            this.durationInSeconds = duration;
            String[] parts = name.replace("." + type, "").split("-", 2);
            this.artist = (parts.length == 2) ? parts[0].trim() : "Unknown";
            this.musicName = (parts.length == 2) ? parts[1].trim() : parts[0].trim();
        }

        /**
         * Returns a string representation of this MusicFileInfo.
         * The format is "Name: <music name>, Artist: <artist name>, Type: <audio type>,
         * Path: <audio path>, Duration: <duration in seconds> sec".
         *
         * @return a string representation of this MusicFileInfo
         */
        @Override
        public String toString() {
            return String.format("Name: %s, Artist: %s, Type: %s, Path: %s, Duration: %d sec",
                    musicName, artist, type, path, durationInSeconds);
        }
    }
}
