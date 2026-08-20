package ulb.repositories.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Responsible for loading SQL files from the resources directory.
 * <p>
 * This class provides a utility method to load the content of an SQL file
 * as a string, which can be used for executing SQL queries and updates.
 * It handles locating the file within the predefined SQL directory and
 * reading its contents.
 * <p>
 * Exceptions that may be thrown include IOException, which occurs if the file
 * is not found or an error arises during file reading.
 */
public class SQLFileLoader {

    private static final String SQL_DIRECTORY = "ulb/sql/";

    /**
     * Loads an SQL file from the specified directory and returns its content as a string.
     *
     * @param filename The name of the SQL file to load.
     * @return The content of the SQL file as a string.
     * @throws IOException If the SQL file is not found or an I/O error occurs while reading the file.
     */
    public static String loadSQL(String filename) throws IOException {
        String fullPath = SQL_DIRECTORY + filename;
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullPath);

        if (inputStream == null) {


            throw new IOException("❌ SQL file not found: " + SQL_DIRECTORY + filename);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
