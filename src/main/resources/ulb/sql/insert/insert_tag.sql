INSERT
OR IGNORE INTO Tags (UserID, Name, is_predefined)
VALUES (?, ?, COALESCE(?, FALSE));
