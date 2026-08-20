INSERT INTO Songs (UserID, SongPath, Name, TimeLength, AudioType, Artist, Album, Lyrics, ImagePath, VideoPath)
SELECT ?,
       ?,
       ?,
       ?,
       ?,
       ?,
       ?,
       ?,
       ?,
       ? WHERE NOT EXISTS (
    SELECT 1 FROM Songs WHERE UserID = ? AND Name = ? AND Artist = ? AND Album = ?
);
