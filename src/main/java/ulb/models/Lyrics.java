package ulb.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the lyrics of a song.
 * Provides methods to load the lyrics from a .lrc file and retrieve the
 * lyrics as a list of strings.
 */
public class Lyrics {
    private final Song song;
    private final List<LyricLine> lines;

    public Lyrics(Song song) {
        this.lines = new ArrayList<>();
        this.song = song;
    }

    /**
     * Loads the lyrics from a .lrc file located at the path specified
     * in the song object associated with this instance of Lyrics.
     * The lyrics are stored internally in this instance and can be
     * accessed using the getLyricsText() method.
     *
     * @throws IOException if the file cannot be read from.
     */
    public void loadFromLrcFile() throws IOException {
        if (song == null) {
            throw new IOException("Song is null");
        }
        Path path = Path.of(song.getLyricsPath());

        List<String> lrcLines = Files.readAllLines(path);

        for (String lrcLine : lrcLines) {
            LyricLine lyricLine = new LyricLine(lrcLine);
            this.lines.add(lyricLine);
        }


    }

    /**
     * Returns a list of strings where each string represents a line of the
     * song's lyrics.
     *
     * @return a list of strings where each string is a line of the song's
     * lyrics.
     */
    public List<String> getLyricsText() {
        List<String> lyrics = new ArrayList<>();
        for (LyricLine line : lines) {
            lyrics.add(line.getTextLine());
        }
        return lyrics;
    }

    /**
     * Retrieves the list of LyricLine objects representing the lines of the song's lyrics.
     *
     * @return a list of LyricLine objects.
     */
    public List<LyricLine> getLines() {
        return lines;
    }
}