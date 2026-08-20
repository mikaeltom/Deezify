package ulb.config;

import org.json.JSONObject;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;

import java.io.*;

/**
 * Manages configuration settings for the application, specifically handling
 * the loading, saving, and retrieval of configuration data. This class follows
 * the Singleton design pattern to ensure only one instance is used throughout
 * the application. It manages a JSON configuration file located at
 * "maven-config/config.json" and provides methods to check and set the first
 * launch status of the application.
 */
public class ConfigManager {
    private static final String configFilePath = "maven-config/config.json";
    private JSONObject config;

    public ConfigManager() {
        this.loadConfig();
    }

    /**
     * Loads the configuration from a JSON file located at {@link #configFilePath}.
     * If the file does not exist, it is created with the default configuration.
     * If the file does exist, it is read and parsed into a JSONObject.
     * If an error occurs during the loading process, an ErrorView is displayed.
     */
    private void loadConfig() {
        File file = new File(configFilePath);

        try {
            if (!file.exists()) {

                config = new JSONObject();
                JSONObject app = new JSONObject();
                app.put("firstLaunch", true);
                config.put("app", app);

                saveConfig();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                config = new JSONObject(sb.toString());
            }
        } catch (IOException e) {
            new PopupView("error_load_config_title", I18n.get("error_load_config") + configFilePath, PopupType.ERROR);
        }
    }

    /**
     * Saves the configuration to the JSON file located at {@link #configFilePath}.
     * If the file does not exist, it is created.
     * If the file does exist, it is overwritten.
     * If an error occurs during the saving process, an ErrorView is displayed.
     */
    private void saveConfig() {
        try {
            FileWriter writer = new FileWriter(configFilePath);
            writer.write(config.toString(4));
            writer.close();
        } catch (IOException e) {
            new PopupView("error_save_error_title", I18n.get("error_save_config"), PopupType.ERROR);
        }
    }

    /**
     * Indicates whether this is the first time the application is launched.
     * The value is retrieved from the "app" object in the JSON configuration file.
     *
     * @return true if this is the first launch, false otherwise
     */
    public boolean isFirstLaunch() {
        return config.getJSONObject("app").getBoolean("firstLaunch");
    }

    /**
     * Sets the first launch status of the application to the given value.
     * This sets the "firstLaunch" property in the "app" object of the JSON
     * configuration file to the given value and saves the configuration.
     *
     * @param value the new first launch status
     */
    public void setFirstLaunch(boolean value) {
        config.getJSONObject("app").put("firstLaunch", value);
        saveConfig();
    }
}
