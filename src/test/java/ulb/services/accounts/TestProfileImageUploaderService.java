package ulb.services.accounts;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.SQLService;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProfileImageUploaderService.
 * This class tests the functionality of uploading and deleting profile images.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Use a single test instance for all test methods
public class TestProfileImageUploaderService {

    private ProfileImageUploaderService imageUploaderService;
    private SQLService sqlService;
    private RegisterService registerService;

    private int userId;
    private final String testUsername = "testUserXYZ123";
    private final String testPassword = "password123";
    private File tempImage;

    @BeforeAll
    public void globalSetup() {
        // Initialize services once before all tests
        imageUploaderService = new ProfileImageUploaderService();
        sqlService = SQLService.getInstance();
        registerService = new RegisterService();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Register a new test user before each test
        userId = registerService.registerNewUser(
                testUsername,
                testPassword,
                testPassword,
                null,
                false,
                "en"
        );
        sqlService.setUserById(userId);

        // Create a temporary image file to simulate a profile picture
        tempImage = File.createTempFile("test-profile", ".jpg");
        try (FileWriter writer = new FileWriter(tempImage)) {
            writer.write("fake image content"); // Simulate image file contents
        }
    }

    /**
     * Test for uploading a profile image.
     * Verifies that the image is saved correctly and the path is stored in the database.
     */
    @Test
    public void testAddProfileImage() throws Exception {
        // Test uploading a profile image
        imageUploaderService.addProfileImage(tempImage);

        String savedPath = sqlService.getUserByID(userId).getProfileImagePath();

        // Verify the image path is correctly saved and points to a valid file
        assertNotNull(savedPath, "Profile image path should be set.");
        assertFalse(savedPath.isEmpty(), "Profile image path should not be empty.");
        assertTrue(savedPath.endsWith(".jpg"), "Profile image path should end with .jpg");
        assertTrue(new File(savedPath).exists(), "Saved profile image file should exist.");
    }

    /**
     * Test for deleting a profile image.
     * Verifies that the image is deleted and the path is cleared in the database.
     */
    @Test
    public void testDeleteProfileImage() throws Exception {
        // Upload an image first
        imageUploaderService.addProfileImage(tempImage);
        String savedPath = sqlService.getUserByID(userId).getProfileImagePath();

        // Delete the image
        imageUploaderService.deleteProfileImage(savedPath);
        String clearedPath = sqlService.getUserByID(userId).getProfileImagePath();

        // Verify that the profile image path is cleared and the file is deleted
        assertEquals("", clearedPath, "Profile image path should be cleared.");
        assertFalse(new File(savedPath).exists(), "Profile image file should be deleted.");
    }

    /**
     * Test for uploading an empty file as a profile image.
     * Verifies that the upload is handled correctly and the path is set.
     */
    @Test
    public void testAddProfileImageWithEmptyFile() throws Exception {
        // Create an empty file
        File emptyFile = File.createTempFile("empty", ".jpg");

        // Upload the empty file as a profile image
        imageUploaderService.addProfileImage(emptyFile);
        String savedPath = sqlService.getUserByID(userId).getProfileImagePath();

        // Validate the upload worked and file exists
        assertNotNull(savedPath);
        assertFalse(savedPath.isEmpty());
        assertTrue(new File(savedPath).exists());

        emptyFile.delete(); // Clean up
    }

    @AfterEach
    public void tearDown() throws SQLExceptionHandler {
        // Remove test user and clean up after each test
        try {
            sqlService.setUserById(userId);
            sqlService.removeUser();
        } catch (SQLExceptionHandler e) {
            throw new SQLExceptionHandler(e.getMessage()); // Wrap and rethrow if needed
        }

        // Delete the temporary image file if it still exists
        if (tempImage != null && tempImage.exists()) {
            tempImage.delete();
        }
    }
}
