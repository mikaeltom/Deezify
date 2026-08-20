UPDATE Playlists_Songs
SET Position = Position - 1
WHERE Position > ?
  AND PlaylistID = ?
  AND UserID = ?;