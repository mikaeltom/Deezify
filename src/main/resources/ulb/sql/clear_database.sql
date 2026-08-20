PRAGMA
foreign_keys = OFF;

DELETE
FROM Users;
DELETE
FROM Songs;
DELETE
FROM Playlists;
DELETE
FROM Tags;
DELETE
FROM Songs_Tags;
DELETE
FROM Playlists_Songs;
DELETE
FROM Playlists_Tags;
DELETE
FROM sqlite_sequence;

PRAGMA
foreign_keys = ON;
SELECT changes();
