package ulb.services.options;

import ulb.dtos.TagDTO;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.exceptions.tag.InvalidTagError;
import ulb.models.PredefinedTagManager;
import ulb.models.Song;
import ulb.models.Tag;
import ulb.services.BannedWordService;
import ulb.services.SQLService;

import java.util.List;

/**
 * Service responsible for all tag-related operations, including database operations,
 * validations, and interactions with songs.
 */
public class TagService {
    private final SQLService sqlService;
    private final BannedWordService bannedWordService;
    private final List<String> predefinedTags;

    /**
     * Constructor for TagService.
     *
     * @param sqlService           The SQL service for database operations
     * @param bannedWordService    Service for checking banned words
     * @param predefinedTagManager Manager for predefined tags
     */
    public TagService(SQLService sqlService, BannedWordService bannedWordService,
                      PredefinedTagManager predefinedTagManager) {
        this.sqlService = sqlService;
        this.bannedWordService = bannedWordService;
        this.predefinedTags = predefinedTagManager.getPredefinedTags();
    }

    /**
     * Inserts a tag into the database if it is valid and not already present.
     * <p>
     * If the tag is valid, it is inserted into the database.
     * If the tag name contains a banned word, an exception is thrown.
     * <p>
     *
     * @param tag          The tag to be inserted.
     * @param isPredefined Whether the tag is predefined or custom.
     * @throws BannedWordException If the tag name contains a banned word
     * @throws SQLExceptionHandler If a database access error occurs
     */
    public void insertTag(Tag tag, boolean isPredefined) throws BannedWordException, SQLExceptionHandler {
        bannedWordService.containsBannedWords(tag.getName());
        sqlService.addNewTag(tag.getName(), isPredefined);
    }

    /**
     * Retrieves all custom tags (non-predefined) from the database.
     * <p>
     * This method fetches all tags from the database
     * that are not predefined and returns them as a list of TagDTO objects.
     * </p>
     *
     * @return A list of custom tags
     * @throws SQLExceptionHandler If a database access error occurs
     */
    public List<? extends TagDTO> getCustomTags() throws SQLExceptionHandler {
        return sqlService.getCustomTags();
    }

    /**
     * Adds a tag to a song if the tag is valid and not already associated with the song.
     *
     * @param song    The song to add the tag to
     * @param tagText The name of Tag object to be added to the song
     * @throws BannedWordException If the tag name contains a banned word
     * @throws SQLExceptionHandler If a database access error occurs
     * @throws InvalidTagError     If the tag is already associated with the song
     */
    public void addTagToSong(Song song, String tagText) throws BannedWordException, SQLExceptionHandler,
            InvalidTagError {
        bannedWordService.containsBannedWords(tagText);
        Tag tag = new Tag(tagText);

        if (!song.getTags().contains(tag)) {
            sqlService.addTagToSong(sqlService.getSongId(song.getTitle()),
                    sqlService.getTagId(tagText));
            song.addTag(tag);
        } else {
            throw new InvalidTagError("tag_invalid_exception");
        }
    }

    /**
     * Removes a tag from a song if the tag is valid and associated with the song.
     *
     * @param song    The song to remove the tag from
     * @param tagText The name of the tag to be removed from the song
     * @throws InvalidTagError     If the tag name is invalid
     * @throws SQLExceptionHandler If a database access error occurs
     */
    public void removeTagFromSong(Song song, String tagText) throws InvalidTagError,
            SQLExceptionHandler {
        Tag tag = new Tag(tagText);
        int tagID = sqlService.getTagId(tag.getName());
        sqlService.removeTagFromSong(sqlService.getSongId(song.getTitle()), tagID);
        if (sqlService.getTagUses(tagID) == 0 && !isPredefinedTag(tagText)) {
            sqlService.removeTag(tagID);
        }
        song.removeTag(tag);
    }

    private boolean isPredefinedTag(String tagText) {
        return predefinedTags.contains(tagText);
    }

    /**
     * Submits a custom tag for a song.
     * <p>
     * This method inserts the custom tag into the database if it does not already exist,
     * and then adds the tag to the specified song.
     * </p>
     *
     * @param song    The song to add the custom tag to
     * @param tagText The text of the custom tag to be submitted
     * @throws SQLExceptionHandler If a database access error occurs
     * @throws InvalidTagError     If the tag is invalid
     * @throws BannedWordException If the tag contains banned words
     */
    public void submitCustomTag(Song song, String tagText) throws SQLExceptionHandler,
            InvalidTagError, BannedWordException {
        Tag tag = new Tag(tagText);
        insertTag(tag, false);
        addTagToSong(song, tagText);
    }
}