SELECT t.TagID, t.Name
FROM Tags t
         JOIN Songs_Tags st ON t.TagID = st.TagID
WHERE st.SongID = ?;
