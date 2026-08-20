UPDATE Playlists_Songs
SET Position = ?
WHERE PlaylistID = ?
  AND SongID = ?
  AND UserID = ?;