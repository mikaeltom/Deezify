SELECT DISTINCT SongID,
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
WHERE Name LIKE ?
   OR Album LIKE ?
   OR Artist LIKE ?;
