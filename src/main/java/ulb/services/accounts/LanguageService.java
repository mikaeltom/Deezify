package ulb.services.accounts;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.SQLService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LanguageService is responsible for managing the language settings of the
 * application.
 * It provides methods to update the language and retrieve the current
 * ResourceBundle.
 */
public class LanguageService {
    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", currentLocale);
    private final SQLService sqlService = SQLService.getInstance();

    /**
     * Updates the language of the application.
     * This method sets the language in the SQLService.
     *
     * @param language The new language to be set.
     * @throws SQLExceptionHandler If there is an error while updating the language.
     */
    public void updateLanguage(String language) throws SQLExceptionHandler {
        sqlService.setLanguage(language); // Keep language in database
        currentLocale = new Locale(language);
        bundle = ResourceBundle.getBundle("languages.messages", currentLocale);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getLanguage() {
        return currentLocale.getLanguage();
    }

    public String getMessage(String key, Object... args) {
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, args);
    }
}
