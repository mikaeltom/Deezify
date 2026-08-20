package ulb.utils;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.accounts.LanguageService;
import java.util.ResourceBundle;

/**
 * This class is responsible for internationalization (i18n) in the application.
 * It provides methods to set the language, retrieve messages, and manage the
 * language service.
 */
public class I18n {
    private static final LanguageService languageService = new LanguageService();

    /**
     * Initializes the language service with the default language.
     */
    public static void setLanguage(String language) throws SQLExceptionHandler {
        languageService.updateLanguage(language);
    }

    /**
     * Retrieves a message from the language service using the specified key and
     * arguments.
     *
     * @param key  The key for the message.
     * @param args The arguments to format the message.
     * @return The formatted message.
     */
    public static String get(String key, Object... args) {
        return languageService.getMessage(key, args);
    }

    /**
     * Returns the current language service instance.
     * @return The current language service instance.
     */
    public static LanguageService getService() {
        return languageService;
    }

    /**
     * Returns the current ResourceBundle for the language service.
     * @return The current ResourceBundle.
     */
    public static ResourceBundle getBundle(){
        return languageService.getBundle();
    }

    /**
     * Returns the current language.
     * @return The current language.
     */
    public static String getLanguage(){
        return languageService.getLanguage();
    }

    /**
     * Updates the language in the language service.
     * @param language The new language to set.
     * @throws SQLExceptionHandler If an error occurs during the update.
     */
    public static void updateLanguage(String language) throws SQLExceptionHandler{
        languageService.updateLanguage(language);
    }
}
