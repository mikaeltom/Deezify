package ulb.services.collections;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionService {

    private final MusicQueueService musicQueueService;
    private final PlaylistService playlistService;

    public CollectionService(MusicQueueService musicQueueService, PlaylistService playlistService) {
        this.musicQueueService = musicQueueService;
        this.playlistService = playlistService;
    }

    public Song getNextOrPreviousSong(Boolean isPrevious, Song currentSong,
            AbstractMap.SimpleEntry<List<Song>, String> currentSongList,
            AbstractMap.SimpleEntry<String, Song> lastPlaylist,
            Function<String, Playlist> getPlaylistByTitle,
            Supplier<ArrayList<Song>> getQueue) {

        Song nextQueueSong = musicQueueService.getNext(currentSong);
        if (nextQueueSong != null && !isPrevious) {
            return nextQueueSong;
        }

        if (lastPlaylist == null) {
            return null;
        }

        Playlist previousPlaylist = getPlaylistByTitle.apply(lastPlaylist.getKey());
        if (previousPlaylist == null) {
            return null;
        }

        List<Song> songs = previousPlaylist.getSongs();
        Song lastSong = lastPlaylist.getValue();

        int index = songs.indexOf(lastSong);

        if (index == -1 || songs.isEmpty())
            return null;

        return isPrevious
                ? (index > 0 ? songs.get(index - 1) : songs.getLast())
                : (index < songs.size() - 1 ? songs.get(index + 1) : songs.getFirst());
    }

    public boolean hasSongOrderChanged(List<Song> currentSongs, List<Song> newSongList) {
        if (currentSongs.size() != newSongList.size()) {
            return false;
        }

        for (int i = 0; i < newSongList.size(); i++) {
            if (currentSongs.get(i).getId() != newSongList.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public void updateSongPositions(List<Song> newSongList, int playlistID) throws SQLExceptionHandler {
        for (int i = 0; i < newSongList.size(); i++) {
            Song song = newSongList.get(i);
            int newPosition = i + 1;
            int currentPosition = playlistService.getSongPosition(playlistID, song.getId());

            if (currentPosition != newPosition) {
                playlistService.updateSongPositionInPlaylist(playlistID, song.getId(), newPosition);
            }
        }
    }

    public void handlePlaylistDragAndDrop(List<Song> newSongList, Playlist playlist) throws SQLExceptionHandler {
        int playlistID = Objects.requireNonNull(playlist).getId();
        List<Song> currentSongs = playlistService.getPlaylistSongs(playlistID);
        boolean hasChanged = hasSongOrderChanged(currentSongs, newSongList);
        if (hasChanged) {
            updateSongPositions(newSongList, playlistID);
        }
    }

}