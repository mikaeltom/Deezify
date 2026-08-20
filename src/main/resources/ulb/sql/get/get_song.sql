SELECT SongID,
       SongPath,
       Name,
       Artist,
       Album,
       TimeLength,
       AudioType,
       Lyrics,
       ImagePath,
       VideoPath
FROM Songs
WHERE SongID = ?
  AND UserID = ?;
