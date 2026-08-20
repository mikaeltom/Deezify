DELETE
FROM Playlists_Tags
WHERE PlaylistID = ?
  AND TagID = ?
  AND UserID = ?