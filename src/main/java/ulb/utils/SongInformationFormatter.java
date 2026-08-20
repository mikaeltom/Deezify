package ulb.utils;

import ulb.dtos.SongDTO;


/**
 * Utility class for formatting song information.
 * This class provides methods to format strings and song tags for display.
 */
public class SongInformationFormatter {

    public static String getFormattedString(String string) {
        return (string == null || string.equals("Unknown")) ? I18n.getBundle().getString("unknown") : string;
    }

    public static String getFormattedTags(SongDTO song) {
        return (song.getTags() == null || song.getTags().isEmpty())
                ? I18n.getBundle().getString("no_tags")
                : song.getTags().toString().replaceAll("[\\[\\]]", "");
    }
}
