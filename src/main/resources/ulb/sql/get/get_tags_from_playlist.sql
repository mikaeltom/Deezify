SELECT t.TagID, t.Name
FROM Tags t
         JOIN Playlists_Tags pt ON t.TagID = pt.TagID
WHERE pt.PlaylistID = ?;
