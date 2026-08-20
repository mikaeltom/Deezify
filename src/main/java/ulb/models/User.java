package ulb.models;

import ulb.dtos.UserDTO;
import ulb.exceptions.credentials.InvalidUsernameException;

/**
 * Represents a user with a unique identifier, username, and preferred language.
 * Each user may have multiple playlists and songs associated with them.
 */
public class User implements UserDTO {
    private int id;
    private String username;
    private String language;
    private final boolean stayLogged;
    private String profileImagePath;

    /**
     * Constructor to create a UserD object with the specified ID, username,
     * language and if the user wants to stay connected.
     *
     * @param id       the unique identifier for the user
     * @param username the username of the user
     * @param language the language preference of the user
     */
    public User(int id, String username, String language, boolean stayLogged, String profileImagePath) {
        this.id = id;
        this.username = username;
        this.language = language;
        this.stayLogged = stayLogged;
        this.profileImagePath = profileImagePath;
    }

    /**
     * Retrieves the unique identifier for the user.
     *
     * @return the identifier of the user
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the identifier of the user
     * @throws IllegalArgumentException if the provided ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Trying to set an invalid ID.");
        }
        this.id = id;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the username of the user
     * @throws InvalidUsernameException if the provided username is null or empty
     */
    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.isEmpty()) {
            throw new InvalidUsernameException("invalid_username_null_exception");
        }
        this.username = username;
    }

    /**
     * Retrieves the language preference of the user.
     *
     * @return the language preference of the user
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Sets the language preference for the user.
     *
     * @param language the language to be set
     * @throws IllegalArgumentException if the provided language is null or empty
     */
    public void setLanguage(String language) {
        if (language == null || language.isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty.");
        }
        this.language = language;
    }

    /**
     * Retrieves the stay logged-in preference of the user.
     *
     * @return true if the user prefers to stay logged in, false otherwise
     */
    @Override
    public boolean isStayLogged() {
        return this.stayLogged;
    }

    @Override
    public String getProfileImagePath() {
        return this.profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
