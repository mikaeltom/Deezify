SELECT s.SongID,
       s.SongPath,
       s.Name,
       s.Artist,
       s.Album,
       s.TimeLength,
       s.AudioType,
       s.Lyrics,
       s.ImagePath,
       s.VideoPath
FROM Songs s
         JOIN Playlists_Songs ps ON s.SongID = ps.SongID
WHERE ps.PlaylistID = ?
ORDER BY ps.Position;