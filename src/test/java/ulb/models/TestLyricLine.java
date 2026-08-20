package ulb.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the LyricLine class.
 * This class contains unit tests for the methods in the LyricLine class.
 */
class TestLyricLine {

    private LyricLine lyricLine;

    /**
     * Test for the constructor of the LyricLine class.
     * This test checks if the constructor correctly parses a valid input string.
     */
    @Test
    void testConstructorWithValidInput() {
        String input = "[01:23.45] This is a test lyric";
        lyricLine = new LyricLine(input);

        assertEquals(83450, lyricLine.getTimeLine());
        assertEquals(" This is a test lyric", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with an invalid input string.
     * This test checks if the constructor handles invalid input gracefully.
     */
    @Test
    void testConstructorWithMissingTime() {
        String input = "This lyric has no timestamp";
        lyricLine = new LyricLine(input);

        assertEquals(0, lyricLine.getTimeLine());
        assertEquals("This lyric has no timestamp", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with an empty input string.
     * This test checks if the constructor handles empty input gracefully.
     */
    @Test
    void testConstructorWithEmptyInput() {
        String input = "";
        lyricLine = new LyricLine(input);

        assertEquals(0, lyricLine.getTimeLine());
        assertEquals("", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with a null input string.
     * This test checks if the constructor handles null input gracefully.
     */
    @Test
    void testConstructorWithMissingParts() {
        String input = "[01:23] Incomplete timestamp";
        lyricLine = new LyricLine(input);

        assertEquals(0, lyricLine.getTimeLine(), "Should return 0 when centiseconds are missing.");
        assertEquals(" Incomplete timestamp", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with a non-numeric timestamp.
     * This test checks if the constructor handles non-numeric timestamps gracefully.
     */
    @Test
    void testConstructorWithNonNumericTime() {
        String input = "[aa:bb.cc] Non-numeric timestamp";
        lyricLine = new LyricLine(input);

        assertEquals(0, lyricLine.getTimeLine(), "Should return 0 when timestamp contains non-numeric values.");
        assertEquals(" Non-numeric timestamp", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with a malformed timestamp.
     * This test checks if the constructor handles malformed timestamps gracefully.
     */
    @Test
    void testConstructorWithNullInput() {
        String input = null;
        lyricLine = new LyricLine(input);

        assertEquals(0, lyricLine.getTimeLine());
        assertEquals("", lyricLine.getTextLine());
    }

    /**
     * Test for the constructor of the LyricLine class with a malformed timestamp.
     * This test checks if the constructor handles malformed timestamps gracefully.
     */
    @Test
    void testSetTimeLine() {
        lyricLine = new LyricLine("[01:23.45] Test lyric");
        lyricLine.setTimeLine(5000);

        assertEquals(5000, lyricLine.getTimeLine());
    }

    /**
     * Test for the constructor of the LyricLine class with a malformed timestamp.
     * This test checks if the constructor handles malformed timestamps gracefully.
     */
    @Test
    void testSetTextLine() {
        lyricLine = new LyricLine("[00:10.00] Old lyric");
        lyricLine.setTextLine("New lyric");

        assertEquals("New lyric", lyricLine.getTextLine());
    }
}