UPDATE Songs
SET Name      = COALESCE(?, Name),
    ImagePath = COALESCE(?, ImagePath),
    VideoPath = COALESCE(?, VideoPath),
    Artist    = COALESCE(?, Artist),
    Album     = COALESCE(?, Album)
WHERE SongID = ?
  AND UserID = ?
  AND NOT EXISTS (SELECT 1
                  FROM Songs
                  WHERE UserID = Songs.UserID
                    AND Name = COALESCE(?, Name)
                    AND Artist = COALESCE(?, Artist)
                    AND Album = COALESCE(?, Album)
                    AND SongID <> Songs.SongID)