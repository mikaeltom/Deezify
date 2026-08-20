package ulb.dtos;


/**
 * DTO for a user.
 * This interface defines the structure of a user DTO,
 * which includes the user's ID, username, language preference,
 * stay logged-in status, and profile image path.
 */
public interface UserDTO {
    int getId();

    String getUsername();

    String getLanguage();

    boolean isStayLogged();

    String getProfileImagePath();
}
