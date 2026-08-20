WITH valid_tag AS (SELECT TagID
                   FROM Tags
                   WHERE TagID = ? AND is_predefined = 0)
SELECT (SELECT COUNT(*)
        FROM Playlists_Tags
        WHERE tagID IN valid_tag
    ) +
    (
SELECT COUNT (*)
FROM Songs_Tags
WHERE tagID IN valid_tag
    ) AS total_occurrences;