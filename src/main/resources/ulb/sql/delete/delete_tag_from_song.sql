DELETE
FROM Songs_Tags
WHERE SongID = ?
  AND TagID = ?
  AND UserID = ?