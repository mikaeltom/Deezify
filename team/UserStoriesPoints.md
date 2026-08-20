# User Stories and Estimation

### User Story 1 : Playing Tracks

The user can browse through the tracks stored on their computer (.mp3, .flac, etc.) in a specific folder. They can
select a track to listen to. Additionally, the user can also view the total duration of the track as well as the elapsed
time since the track started.

- **Client priority** : 1
- **Developer risks** : 2
- **Introduced in Iteration** : 1
- **Status** : Done
- **Points** : 0

### User Story 2 : Queue

The user can add tracks to the queue, which is the list of tracks to be played after the current track (if there is
one). When the current track ends, the next track in the queue starts automatically. If the queue is empty, playback
stops. The user can remove tracks from the queue as well as clear it entirely.

- **Client priority** : 1
- **Developer risks** : 3
- **Introduced in Iteration** : 2
- **Status** : Done
- **Points** : 0

### User Story 3 : Control Buttons

During the playback of a track, a slider displays the progress of the track and allows navigation to any point within
it. Buttons enable interaction with playback (pause, next, previous, etc.), and a slider allows volume control.

- **Client priority** : 1
- **Developer risks** : 3
- **Introduced in Iteration** : 1
- **Status** : Done
- **Points** : 0

### User Story 4 : Modify Track Data

The user must be able to edit the metadata of the tracks, such as the artist, track name, and album name.

- **Client priority** : 1
- **Developer risks** : 3
- **Introduced in Iteration** : 1
- **Status** : Done
- **Points** : 0

### User Story 5 : Autocompletion of Data

When the user modifies the metadata of a track, autocomplete suggestions should be provided for artists, albums, and
tags that already exist.

- **Client priority** : 2
- **Developer risks** : 2
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 26

### User Story 6 : Add Tags to Tracks

From the navigation menu, the user can add tags to the tracks on their computer. These tags are visible next to the
track name in the navigation menu. Different predefined tag types provided by the application are available to represent
music genres, emotions, or events. The user can also add their own custom tags.

- **Client priority** : 2
- **Developer risks** : 3
- **Introduced in Iteration** : 1
- **Status** : Done
- **Points** : 0

### User Story 7 : Search

In the navigation menu, the user can search for a track by entering its title, artist name, album name, or assigned
tags.

- **Client priority** : 1
- **Developer risks** : 3
- **Introduced in Iteration** : 1
- **Status** : Done
- **Points** : 0

### User Story 8 : Playlists

The user can create playlists with a title, and optionally an image. The user can add tracks to the playlist and modify
their order. The user can explore their playlists and start playback by either replacing the current queue with the
playlist or adding all the tracks to the end of the queue.

- **Client priority** : 1
- **Developer risks** : 2
- **Introduced in Iteration** : 2
- **Status** : Done
- **Points** : 0

### User Story 9 : Track Suggestions

When the user creates a playlist, the program suggests similar tracks based on the artists, tags, and albums of the
existing tracks.

- **Client priority** : 3
- **Developer risks** : 2
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 18

### User Story 10 : DJ Mode

During the playback of a track, the user can apply effects that modify the sound (e.g., cathedral effect, gain effect,
etc.).

- **Client priority** : 2
- **Developer risks** : 1
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 26

### User Story 11 : Equalizer

The user can adjust the frequency balance to enhance bass, treble, etc., within a range of -20 dB to +20 dB using
sliders.

- **Client priority** : 3
- **Developer risks** : 1
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 13

### User Story 12 : Visualizer

During the playback of a track, the user can choose to display a graphical sound visualizer. A randomly generated
animation, synchronized with the rhythm of the music, will then be displayed.

- **Client priority** : 2
- **Developer risks** : 1
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 26

### User Story 13 : Lyrics

The user can import lyrics for each track. During playback, they can view the lyrics.

- **Client priority** : 3
- **Developer risks** : 3
- **Introduced in Iteration** : 2
- **Status** : Done
- **Points** : 0

### User Story 14 : Karaoke Mode

The user can import synchronized lyrics for a track. Then, during playback, if synchronized lyrics have been imported,
the lyrics will scroll in sync with the music.

- **Client priority** : 3
- **Developer risks** : 1
- **Introduced in Iteration** : 3
- **Status** : Done
- **Points** : 0

### User Story 15 : Transition

The user can check an option to create a transition between two tracks, where the volume of the current track gradually
decreases while the volume of the next track gradually increases. When the option is enabled, the user can choose the
duration of the transition using a slider.

- **Client priority** : 3
- **Developer risks** : 2
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 15

### User Story 16 : Miscellaneous Controls

Advanced options are added: playback speed of a track, random music playback, and automatic balance of the different
audio channels.

- **Client priority** : 1
- **Developer risks** : 2
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 24

### User Story 17 : Radio

The user can add a radio stream to their library. The user can listen to a radio stream from the library. The playback
of a radio stream never ends.

- **Client priority** : 2
- **Developer risks** : 1
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 37

### User Story 18 : Multilingual Support

The user can choose the language of the application from an options menu. The three available languages are French,
Dutch, and English.

- **Client priority** : 2
- **Developer risks** : 3
- **Introduced in Iteration** : 4
- **Status** : Done
- **Points** : 0

### User Story 19 : Album Covers

The user can add images (album covers) that are displayed next to the track names in the navigation menus and in large
size during track playback if nothing else is displayed.

- **Client priority** : 2
- **Developer risks** : 2
- **Introduced in Iteration** : 2
- **Status** : Done
- **Points** : 0

### User Story 20 : Use Video as Cover

The user can add images (album covers) that are displayed next to the track names in the navigation menus and in large
size during track playback if nothing else is displayed.

- **Client priority** : 3
- **Developer risks** : 1
- **Introduced in Iteration** : /
- **Status** : Not started
- **Points** : 13

### User Story 21 : Favorites

A specific playlist, called "Favorites," must exist by default in the application. The user can easily add tracks to it.

- **Client priority** : 2
- **Developer risks** : 3
- **Introduced in Iteration** : 3
- **Status** : Done
- **Points** : 0

### User Story 22 : User Account

Multiple users can create a (local) account on the application. This requires several folders to store the tracks: a
global folder accessible by all users, as well as a specific folder for each user. Each user also has access to their
own playlists. A user menu allows switching between users and creating new users. No password is required.

- **Client priority** : 3
- **Developer risks** : 2
- **Introduced in Iteration** : 4
- **Status** : Done
- **Points** : 0
