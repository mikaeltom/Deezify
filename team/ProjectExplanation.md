# Project Planning Tracking

## 1. Feature Implementation

### Iteration 1

- [x] **Story 1**: The user can browse through the tracks present on their computer (.mp3, .flac,
      etc. files) saved in a specific folder. They can select a track to listen to it. In addition, the user
      can view the total duration of the track as well as the time elapsed since playback started.
- [x] **Story 3**: While a track is playing, a slider displays the progress of the playback and allows
      navigation to any point in the track. Buttons allow interaction with playback (pause, next,
      previous, etc.), and another slider allows volume control.
- [x] **Story 4**: The user can edit a track's metadata, such as the artists, the track title and
      the album name.
- [x] **Story 6**: The user can add predefined tags as well as their own custom tags to tracks. These tags
      are visible in the main menu.
- [x] **Story 7**: In the navigation menu, the user can search for a track by typing its title, the
      artist name, the album name or the associated tags.
- [x] **Bonus Story 1**: Profanity filter for tags created by the user.

### Iteration 2

- [x] **Story 1**:
      The user can delete tracks they had previously added.

- [x] **Story 2**:
      Creation of a queue that the user can fill. The next tracks played will be those present in the
      queue until it is empty.
      The user can remove a track or all tracks from the queue.
- [x] **Story 4**:
      Change of colors of the metadata-editing window and improvements to it.

- [x] **Story 6**:
      Displays an error pop-up if an unauthorized tag is added by the user.
      Change of the color of the window and improvements to it.

- [x] **Story 8**:
      The user can create playlists and add tracks to them. The order of the tracks can be changed via
      drag and drop.
      Playlists can also be deleted.

- [x] **Story 13**:
      The user can import a track's lyrics (in .lrc format) and display them in the application by
      tapping on the currently playing track.
      They can also remove the lyrics.
- [x] **Story 19**:
      The user can import an image for a track. It is displayed next to the track and can be
      displayed larger when clicking on the cover of the currently playing track.
      The cover can also be removed.

### Iteration 3

- [x] **Story 14**:
      The user can import synchronized lyrics for a track. Then, during the
      playback of a track for which synchronized lyrics have been imported,
      the user can watch the lyrics scroll in sync with the music.

- [x] **Story 21**:
      A specific playlist, called "Favorites", must exist by default in the application.
      The user can easily add tracks to it.
- [x] **Refactor**:
      General code refactor
- [x] **Bonus Story 2**:
  The user can find a track in their library by searching for its lyrics in the search bar (3 words are required)

### Iteration 4

- [x] **Story 18**: 
  Multilingual support:
  the user can choose the language of the application from an options menu. The three
  available languages are French, Dutch and English. The user's language is then tied to their account.
- [x] **Story 22**: User account:
  Multiple users can create a (local) account on the application. Each user also has
  access to their own playlists.
  A user menu allows switching from one user to another, and creating
  new users.
- [x] **Bonus Story 3**:
 Users have a password to log in. There is an option that can be checked to stay logged in
 without having to re-enter their password on every connection.
- [x] **Bonus Story 4**:
 The user can resize the application window.
- [x] **Refactor**:
  General code refactor

## 2. Handling Technical Difficulties

### Iteration 1

- **Problem 1**: Difficulty implementing code that reads audio files across different operating systems.
  - **Resolution**: Use of relative file paths.
- **Problem 2**: A few push and merge issues on GitLab causing code losses.
  - **Resolution**: Improved communication and use of branches.
- **Problem 3**: Metadata in the database was only updated when the application started.
  - **Resolution**: Implementation allowing the database to be updated in real time while
    the application is running.

### Iteration 2

- **Problem 4**: Unable to view/play/edit a track that had just been added.
  - **Resolution**: Logical and visual update of the library on every track addition. The uploader no longer returns void
    but an instance of the track.
- **Problem 5**: Tracks in the search bar were not linked to the library, which caused
  problems when editing them from the search bar.
  - **Resolution**: Retrieve a track from the library after finding it with the search bar's
    search algorithm.
- **Problem 6**: The track options menu was not the same in the library and in the search bar,
  which made it harder to add track-editing options.
  - **Resolution**: Merging of the editMetaData and tripleDot menus to create a single track options menu with a
    more compact and simpler design.
- **Problem 7**: Errors were being displayed somewhere other than in the view.
  - **Resolution**: Propagate all errors up to the view.
- **Problem 8**: Custom tags were not displayed.
  - **Resolution**: Creation of a dedicated view and controller for tags, ensuring cleaner code.
- **Problem 9**: Issue with importing covers.
  - **Resolution**: Initially handled in the controller, the import mixed responsibilities and
    made the code harder to maintain. To improve the structure, this feature was moved to
    dedicated classes in
    services and repositories, ensuring a better separation of responsibilities.

### Iteration 3

- **Problem 10**: Issue with using Song models directly in the SongList view, creating tight coupling.

  - **Resolution**: Creation of a SongBox class to encapsulate the data needed for display, strengthening the separation between model and view.

- **Problem 11**: Difficulty integrating several views together within a single main controller.

  - **Resolution**: Implementation of an architecture where each controller exposes its view via a Parent attribute accessible through a getter. This approach allows the MainController to retrieve the different views and assemble them into the main view, making it easier to compose the user interface.

- **Problem 12**: Complexity in coordinating the initialization of the different views when the application loads.
  - **Resolution**: Introduction of a hierarchical initialization sequence where the MainController orchestrates the order in which the sub-controllers' show() methods are called, then retrieves and integrates their visual components into the main view's structure.

### Iteration 4

- **Problem 13**: Issue with refreshing the main view when the language was changed.
  - **Resolution**: Use of a listener. 
- **Problem 14**: Issue translating errors according to the user's language. Java's general errors were all in English.
  - **Resolution**: Stop using .getMessage() on Java errors and display a custom message instead (easy to translate).
- **Problem 15**: Error pop-up displayed twice. Two pop-ups were being created for every issue.
  - **Resolution**: Move the pop-up display inside the try-catch block, creating a PopupView instance directly at that point.
- **Problem 16**: The MainController was too large and acted as the pass-through point for every controller. It had far too many responsibilities.
  - **Resolution**: Creation of intermediate controllers. Controllers that need to interact with other controllers are given interfaces to those other controllers.
# 3. Design Choices and Motivation

## Enhanced MVC Architecture

The application is built on the MVC (Model-View-Controller) pattern, an architecture that structures the code clearly and effectively by separating data management, display, and user interaction. However, we adopted a more structured and modular approach:

- **Clarity and maintainability:** By separating responsibilities, the code is easier to understand and modify.
- **Modularity and scalability:** Since each component is independent, it is easier to add new features or modify specific parts without impacting the rest of the application.
- **Code reuse:** Elements can be used in multiple contexts without unnecessary duplication.

In our architecture:

- **The Model** handles the data and business logic. It defines the classes representing the application's elements (songs, playlists, etc.) and is exclusively manipulated by controllers through services.
- **The View** handles display and user interaction management. It contains no business logic and communicates with its controller through Listener-type interfaces.
- **The Controller** bridges the view and the model. It initializes and manages its associated view, processes user actions transmitted through the Listener interface, and uses services to update the model.

The general workflow is as follows:

1. On launch, the `MainController` is instantiated and initializes the application's main controllers.
2. Each main controller initializes its own view and may also initialize specific sub-controllers.
3. Views communicate with their respective controllers exclusively through Listener interfaces, ensuring a clear separation of responsibilities.
4. When a user interacts with the application, the action is captured by the view, which forwards it to the controller via the Listener.
5. The controller uses the appropriate services to process the action and update the model.
6. The view is then updated to reflect the changes in the model.

This layered approach allows for better code organization and makes collaborative development easier.

## Removal of Singletons and Introduction of Services and Repositories

We removed the use of most Singletons in favor of a more secure, modular and testable architecture:

- **Services:** An intermediate layer between controllers and models, services encapsulate business logic and complex operations. They lighten the controllers and centralize data-handling rules.
- **Repositories:** Classes dedicated to managing database access and persistence operations. They fully isolate data access logic from the rest of the application.

Two singleton classes remain in the code:
- SQLService: using a singleton for SQLService ensures that only one instance of the database connection is maintained for the entire lifetime of the application. This avoids multiple resource-costly connection openings and ensures consistency of read/write operations on the database.

- ImageCache:  
Centralizes the loading and caching of images in the application. This also allows already-loaded images to be reused, reducing memory consumption and improving display performance, while avoiding redundant loading in every view.

The advantages of this new approach are numerous:
- **Secure code:** The former singleton-type classes are no longer accessible from every class.
- **Improved testability:** Components can be tested in isolation thanks to the use of interfaces and dependency injection.
- **Consistent state:** Explicitly passing references avoids issues related to global state.
- **Flexibility:** It is easier to modify or replace specific components without impacting the rest of the system.
- **Code clarity:** Responsibilities are clearly defined and dependencies are explicit.

## Communication Between Components via Listener Interfaces

To ensure a strict separation between views and controllers, we use Listener interfaces:

- Each view defines a Listener interface with the methods needed to handle user actions.
- The controller implements this interface and registers itself with the view via a `setListener()` method.
- The view can then notify the controller of user actions without knowing its full implementation.

This delegation-based approach has several advantages:

- **Low coupling:** The view only depends on the interface, not on the controller's implementation.
- **Flexibility:** The controller can be modified or replaced without affecting the view.
- **Testability:** It is easy to mock the controller's behavior to test the view in isolation.

By combining this enhanced MVC architecture with well-defined services and repositories, and communication through Listener interfaces, the application benefits from a robust, modular and scalable structure that facilitates collaboration between developers and the addition of new features.

![Views-Controllers-Diagram](MVC.jpg)

## Use of DTOs and Immutable Collections

To further reinforce the separation between the layers of our application, we put in place two important mechanisms:

### DTO (Data Transfer Objects)

We implemented DTOs to transfer data from the model to the view. These simplified objects contain only the information necessary for display and only expose getter methods:

- **Data security:** The view cannot directly modify the model's data since it only has access to read-only DTO objects.
- **Stronger decoupling:** DTOs create an additional barrier between the view and the model, eliminating any direct dependency.

### Immutable Collections

In certain parts of the application, we use unmodifiable collections to share lists of objects with the view (example in CollectionsView.java)

### Use of Triggers in the Database

We implemented triggers in our database to lighten the code and ensure that certain actions are mandatory. We used them so that when a user is created, the Library and Favorites playlists are automatically created. We also use them when a new track is added to the database, so that it is automatically added to the Library playlist. This allowed us to remove unnecessary classes and greatly simplify the code.

### Error Handling
The various possible errors in our code are thrown up to the relevant controller. They are then caught in a try/catch
and displayed to the user as explanatory pop-ups. Each pop-up is an instance of PopupView.

### Use of Resource Bundles for Languages
To handle internationalization in our Java application, we chose to use the standard mechanism provided
by java.util.ResourceBundle combined with .properties files. This approach centralizes translations in
simple text files. We set up a dedicated service, called
LanguageService, which dynamically loads the resources based on the language selected by the
user. This service is then used in a utility class, I18n, located in the utils package, to make it easy
to access translations from anywhere in the application. This choice favors readability, maintainability, and
extensibility: it is simple to add a new language by creating an additional .properties file.