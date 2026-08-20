package ulb.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Tag class.
 * This class contains unit tests for the methods in the Tag class.
 */
public class TestTag {
    private Tag tag;

    /**
     * Test for the constructor and fields of the Tag class.
     */
    @Test
    public void testConstructorAndFields() {
        String category = "Pop";
        tag = new Tag(0, category);
        assertEquals(category, tag.getName());
    }

    /**
     * Test for the constructor with only name.
     */
    @Test
    void testConstructorWithNameOnly() {
        tag = new Tag("Pop");

        assertEquals("Pop", tag.getName());
        assertNotNull(tag); // Ensure tag is instantiated
    }

    /**
     * Test for the constructor with userId and name.
     */
    @Test
    void testConstructorWithUserIdAndName() {
        tag = new Tag(1, "Rock");

        assertEquals("Rock", tag.getName());
    }

    /**
     * Test for the constructor with null name.
     */
    @Test
    void testConstructorWithNullName() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Tag(1, null);
        });

        assertEquals("Tag name cannot be null", exception.getMessage());
    }

    /**
     * Test if two tags with the same ID and name are equal.
     */
    @Test
    void testEqualsWithSameTag() {
        tag = new Tag(1, "Pop");
        Tag sameTag = new Tag(1, "Pop");

        assertEquals(tag, sameTag);
    }

    /**
     * Test if two tags with different IDs and names are not equal.
     */
    @Test
    void testEqualsWithDifferentName() {
        tag = new Tag(1, "Pop");
        Tag differentNameTag = new Tag(1, "Rock");

        assertNotEquals(tag, differentNameTag);
    }

    /**
     * Test if two tags with the same name but different IDs are not equal.
     */
    @Test
    void testEqualsWithDifferentUserId() {
        tag = new Tag(1, "Pop");
        Tag differentUserIdTag = new Tag(2, "Pop");

        assertNotEquals(tag, differentUserIdTag);
    }

    /**
     * Test if two tags with different with one being null are not equal.
     */
    @Test
    void testEqualsWithNullObject() {
        tag = new Tag(1, "Pop");

        assertNotEquals(null, tag);  // Should return false when comparing with null
    }

    /**
     * Test if two tags with different classes are not equal.
     */
    @Test
    void testEqualsWithDifferentClass() {
        tag = new Tag(1, "Pop");

        assertNotEquals("NotATag", tag);  // Should return false when comparing with an object of different class
    }

    /**
     * Test if two tags with the same ID and name have the same hash code.
     */
    @Test
    void testHashCodeWithEqualTags() {
        tag = new Tag(1, "Pop");
        Tag sameTag = new Tag(1, "Pop");

        assertEquals(tag.hashCode(), sameTag.hashCode());
    }

    /**
     * Test if two tags with different IDs and names have different hash codes.
     */
    @Test
    void testHashCodeWithDifferentTags() {
        tag = new Tag(1, "Pop");
        Tag differentTag = new Tag(2, "Rock");

        assertNotEquals(tag.hashCode(), differentTag.hashCode());
    }

    /**
     * Test convert to string.
     */
    @Test
    void testToString() {
        tag = new Tag("Jazz");

        assertEquals("Jazz", tag.toString());  // Should match the name of the tag
    }

    /**
     * Test tag name access.
     */
    @Test
    void testTagNameAccess() {
        tag = new Tag("Country");

        assertEquals("Country", tag.getName());  // Should match the name of the tag
    }

    /**
     * Test tag name with spaces.
     */
    @Test
    void testTagNameWithSpaces() {
        tag = new Tag("Rock Music");
        assertEquals("Rock Music", tag.getName());  // Ensure tag name can contain spaces
    }

    /**
     * Test tag name with different case.
     */
    @Test
    void testTagEqualityWithDifferentCase() {
        tag = new Tag(1, "pop");
        Tag anotherTag = new Tag(1, "POP");

        assertNotEquals(tag, anotherTag);  // Should not be equal due to case difference
    }
}
