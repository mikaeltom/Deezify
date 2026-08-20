package ulb.controllers;

import ulb.exceptions.songs.SQLExceptionHandler;

/**
 * Interface for the MainController to provide access to its methods.
 * This interface is used to allow other controllers to interact with the MainController without being able to access
 * all the methods in the class.
 */
public interface MainControllerAccess {
    void updateCurrentCollection();

    void logout();

    void changeLanguage() throws SQLExceptionHandler;
}
