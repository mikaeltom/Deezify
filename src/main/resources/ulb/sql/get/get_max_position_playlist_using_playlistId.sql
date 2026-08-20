SELECT MAX(Position) AS max_position
FROM Playlists_Songs
WHERE PlaylistID = ?;