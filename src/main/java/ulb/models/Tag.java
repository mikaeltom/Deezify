package ulb.models;

import ulb.dtos.TagDTO;

import java.util.Objects;

/**
 * Represents a tag associated with a song.
 */
public final class Tag implements TagDTO {
    private final int userId;
    private final String name;

    /**
     * Constructs a tag with a default user ID of 0.
     *
     * @param name Tag name.
     */
    public Tag(String name) {
        this(1, name);
    }

    /**
     * Constructs a tag with a specified user ID.
     *
     * @param userId User ID associated with the tag.
     * @param name   Tag name.
     */
    public Tag(int userId, String name) {
        this.userId = userId;
        this.name = Objects.requireNonNull(name, "Tag name cannot be null");
    }

    /**
     * Retrieves the name of the tag.
     *
     * @return Tag name.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if two tags are equal based on user ID and name.
     *
     * @param o The object to compare.
     * @return True if both objects have the same user ID and name.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tag tag = (Tag) o;
        return userId == tag.userId && name.equals(tag.name);
    }

    /**
     * Generates a hash code for the tag.
     *
     * @return Hash code based on user ID and name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }

    /**
     * Returns a string representation of the tag.
     *
     * @return Formatted string with tag name and user ID.
     */
    @Override
    public String toString() {
        return name;
    }
}
