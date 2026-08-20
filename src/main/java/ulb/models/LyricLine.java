package ulb.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single line of lyrics. Contains a timestamp and the text of the line.
 */
public class LyricLine {

    private long timeLine;
    private String textLine;

    public LyricLine(String text) {
        List<String> separatedElement = separateTimeAndLyric(text);
        this.textLine = separatedElement.get(1);
        this.timeLine = parseTimeToMilliseconds(separatedElement.get(0));
    }

    /**
     * Retrieves the time when this line of the lyrics is to be displayed, in milliseconds.
     *
     * @return the time of this line of the lyrics in milliseconds.
     */
    public long getTimeLine() {
        return timeLine;
    }

    /**
     * Sets the time when this line of the lyrics is to be displayed, in milliseconds.
     *
     * @param timeLine the time of this line of the lyrics in milliseconds.
     */
    public void setTimeLine(long timeLine) {
        this.timeLine = timeLine;
    }

    /**
     * Retrieves the text of this line of the lyrics.
     *
     * @return the text of this line of the lyrics.
     */
    public String getTextLine() {
        return textLine;
    }

    /**
     * Sets the text of this line of the lyrics.
     *
     * @param textLine the text of this line of the lyrics.
     */
    public void setTextLine(String textLine) {
        this.textLine = textLine;
    }

    /**
     * Separates the time and lyric text from a given line of lyrics.
     * <p>
     * This method takes a line of lyrics that includes a timestamp in square brackets
     * and separates it into the time part and the lyric text. If the line does not contain
     * a timestamp, the time part is returned as an empty string.
     *
     * @param line the line of lyrics to be separated, potentially containing a timestamp.
     * @return a list where the first element is the time (including brackets) and the second
     * element is the lyric text. If the line is null or empty, both elements are
     * returned as empty strings.
     */
    private List<String> separateTimeAndLyric(String line) {
        List<String> result = new ArrayList<>(List.of("", ""));
        if (line == null || line.isEmpty()) return result;

        int closingBracketIndex = line.indexOf(']');
        if (closingBracketIndex == -1) {
            result.set(1, line);
        } else {
            result.set(0, line.substring(0, closingBracketIndex + 1));
            result.set(1, closingBracketIndex + 1 < line.length() ? line.substring(closingBracketIndex + 1) : "");
        }

        return result;
    }

    /**
     * Converts a time string from the format [mm:ss.cc] to milliseconds.
     * <p>
     * This method takes a time string as input and converts it to milliseconds.
     * The time string is expected to be in the format [mm:ss.cc] where mm, ss and cc
     * are the minutes, seconds and centi seconds respectively. If the time string does
     * not conform to this format, the method returns 0.
     *
     * @param timeString the time string to be converted, must not be null
     * @return the time in milliseconds
     */
    private long parseTimeToMilliseconds(String timeString) {
        if (timeString == null || timeString.isEmpty()) return 0;
        if (timeString.startsWith("[") && timeString.endsWith("]"))
            timeString = timeString.substring(1, timeString.length() - 1);

        String[] parts = timeString.split("[:\\.]");
        if (parts.length != 3) return 0;

        try {
            int min = Integer.parseInt(parts[0]), sec = Integer.parseInt(parts[1]), cs = Integer.parseInt(parts[2]);
            return (long) min * 60000 + sec * 1000L + cs * 10L;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
