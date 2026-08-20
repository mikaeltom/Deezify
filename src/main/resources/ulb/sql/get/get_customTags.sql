SELECT TagID, Name
FROM Tags
WHERE is_predefined = FALSE
  AND UserID = ?