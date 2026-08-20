package ulb.controllers.options;


import ulb.models.Song;


public interface OptionListener {
    void returnToSongOptionView(Song newSong);

    void saveCoverChange(Song currentSong, String imagePath);

    void updateCurrentCollection();

    void loadCollections();

    void addToQueue(Song song);

    void deleteFromQueue(Song song);

    void deleteFromFavorites(Song song);

    void addToFavorites(Song song);
}
