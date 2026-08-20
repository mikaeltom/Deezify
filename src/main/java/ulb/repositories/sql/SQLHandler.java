package ulb.repositories.sql;

import ulb.exceptions.songs.SQLExceptionHandler;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FunctionalInterface
interface SQLFunction<T, R> {
    R apply(T t) throws Exception;
}

public class SQLHandler {
    private final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir")
            + "/src/main/java/ulb/repositories/sql/database/Database.db";

    /**
     * Executes an SQL update (INSERT, UPDATE, DELETE).
     *
     * @param sqlFile The SQL file name.
     * @param params  Parameters for the query.
     * @throws SQLExceptionHandler If an SQL error occurs.
     */
    public void executeUpdate(String sqlFile, Object... params) throws SQLExceptionHandler {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement pragmaStmt = conn.createStatement()) {

            pragmaStmt.execute("PRAGMA foreign_keys = ON;");

            try (PreparedStatement pstmt = conn.prepareStatement(SQLFileLoader.loadSQL(sqlFile))) {
                setParams(pstmt, params);
                if (pstmt.executeUpdate() == 0)
                    throw new SQLExceptionHandler("sql_exception_no_line_affected");
            }

        } catch (SQLException | IOException e) {
            throw new SQLExceptionHandler("sql_exception_error_executing_update", sqlFile, e.getMessage());
        }
    }

    /**
     * Executes an SQL query and returns a ResultSet.
     *
     * @param sqlFile The SQL file name.
     * @param params  Parameters for the query.
     * @return The resulting ResultSet.
     * @throws SQLExceptionHandler If an SQL error occurs.
     */
    public ResultSet executeQuery(String sqlFile, Object... params) throws SQLExceptionHandler {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQLFileLoader.loadSQL(sqlFile));
            setParams(pstmt, params);
            return pstmt.executeQuery();
        } catch (SQLException | IOException e) {
            throw new SQLExceptionHandler("sql_exception_error_executing_query", sqlFile, e.getMessage());
        }
    }

    /**
     * Clears the database by executing all SQL queries in a file.
     *
     * @param sqlFile The SQL file containing the queries to clear the database.
     * @throws SQLExceptionHandler If an SQL error occurs.
     */
    public void executeClearQuery(String sqlFile) throws SQLExceptionHandler {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            String sql = SQLFileLoader.loadSQL(sqlFile);
            String[] queries = sql.split(";"); // Split by semicolon

            for (String query : queries) {
                query = query.trim();
                if (!query.isEmpty()) {
                    stmt.executeUpdate(query);
                }
            }

        } catch (SQLException | IOException e) {
            throw new SQLExceptionHandler("sql_exception_error_clearing_database");
        }
    }

    /**
     * Fetches a single row from a query result and maps it to an object.
     *
     * @param sqlFile The SQL file containing the query.
     * @param mapper  Function to map the result.
     * @param params  Parameters for the query.
     * @param <T>     The type of the mapped object.
     * @return The mapped object or null if no results.
     * @throws SQLExceptionHandler If an SQL error occurs.
     */
    public <T> T fetchSingleRow(String sqlFile, SQLFunction<ResultSet, T> mapper, Object... params)
            throws SQLExceptionHandler {
        try (ResultSet rs = executeQuery(sqlFile, params)) {
            if (rs.next()) {
                try {
                    return mapper.apply(rs);
                } catch (Exception e) {
                    throw new SQLExceptionHandler("sql_exception_message_mapping_single", sqlFile, formatParams(params), e);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLExceptionHandler("sql_exception_message_fetching_single", sqlFile, formatParams(params), e);
        }
    }

    /**
     * Fetches multiple rows from a query result and maps them to a list of objects.
     *
     * @param sqlFile The SQL file containing the query.
     * @param mapper  Function to map each result row.
     * @param params  Parameters for the query.
     * @param <T>     The type of the mapped objects.
     * @return A list of mapped objects.
     * @throws SQLExceptionHandler If an SQL error occurs.
     */
    public <T> List<T> fetchMultipleRows(String sqlFile, SQLFunction<ResultSet, T> mapper, Object... params)
            throws SQLExceptionHandler {
        List<T> results = new ArrayList<>();
        try (ResultSet rs = executeQuery(sqlFile, params)) {
            while (rs.next()) {
                try {
                    results.add(mapper.apply(rs));
                } catch (Exception e) {
                    throw new SQLExceptionHandler("sql_exception_message_mapping_multiple", sqlFile, formatParams(params),
                            e);
                }
            }
        } catch (SQLException e) {
            throw new SQLExceptionHandler("sql_exception_message_fetching_multiple", sqlFile, formatParams(params), e);
        }
        return results;
    }

    /**
     * Formats SQL parameters for debugging purposes.
     *
     * @param params The parameters to format.
     * @return A formatted string representation of the parameters.
     */
    private String formatParams(Object... params) {
        return (params == null || params.length == 0) ? "[]" : Arrays.toString(params);
    }

    /**
     * Sets parameters in a PreparedStatement.
     *
     * @param pstmt  The PreparedStatement object.
     * @param params The parameters to set.
     * @throws SQLException If an SQL error occurs.
     */
    private void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
}
