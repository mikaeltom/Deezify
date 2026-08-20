package ulb.exceptions.songs;

import ulb.utils.I18n;

/**
 * Custom exception for handling SQL-related errors.
 */
public class SQLExceptionHandler extends Exception {

    private final String sqlQuery;
    private final String parameters;

    public SQLExceptionHandler(String message, String sqlQuery, String parameters, Throwable cause) {
        super(I18n.get("sql_exception_message", message), cause);
        this.sqlQuery = sqlQuery;
        this.parameters = parameters;
    }

    public SQLExceptionHandler(String message, String sqlQuery, String parameters) {
        super(I18n.get("sql_exception_message", message));
        this.sqlQuery = sqlQuery;
        this.parameters = parameters;
    }

    public SQLExceptionHandler(String message) {
        super(I18n.get("sql_exception_message", message));
        this.sqlQuery = "N/A";
        this.parameters = "N/A";
    }

    /**
     * Returns a string representation of the exception, which includes the error
     * message,
     * the SQL query that caused the error, the parameters used in the query, and
     * the
     * cause of the error (if any).
     *
     * @return A string representation of the exception.
     */
    @Override
    public String toString() {
        return String.format(
                "%s\n%s: %s\n%s: %s\n%s: %s",
                getMessage(),
                I18n.get("sql_exception_query"), sqlQuery,
                I18n.get("sql_exception_parameters"), parameters,
                I18n.get("sql_exception_cause"), getCause() != null ? getCause().getMessage() : "None"
        );
    }
}
