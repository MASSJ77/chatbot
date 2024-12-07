import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer  {
    private List<String> musicLibrary = new ArrayList<>();
    private List<String> queue = new ArrayList<>();
    private int currentTrackIndex = -1;
    private Clip currentClip;

    public MusicPlayer() {
        initializeMusicLibrary();
    }

    private void initializeMusicLibrary() {
        // Add paths to your local music files (must be .wav format)
        musicLibrary.add("music/Jazzy_Jam (online-audio-converter.com).wav"); // Ensure these paths are correct
        musicLibrary.add("music/SongB.wav");
        musicLibrary.add("music/SongC.wav");
    }

   public String playNext() {
    if (!queue.isEmpty()) {
        if (currentClip != null && currentClip.isOpen()) {
            currentClip.stop();
            currentClip.close();
        }
        currentTrackIndex = (currentTrackIndex + 1) % queue.size();
        String trackPath = queue.get(currentTrackIndex);
        try {
            playMusic(trackPath);
            return "Now playing: " + trackPath;
        } catch (Exception e) {
            return "Error playing the track: " + e.getMessage();
        }
    }
    return "The queue is empty.";
}
    
// Method to display the list of available songs
public String displayLibrary() {
    if (musicLibrary.isEmpty()) {
        return "The music library is empty.";
    }
    StringBuilder builder = new StringBuilder("Available Songs:\n");
    for (int i = 0; i < musicLibrary.size(); i++) {
        builder.append((i + 1) + ". " + musicLibrary.get(i) + "\n");
    }
    return builder.toString();
}

// Method to allow the user to select a song by its index
public String selectSong(int songIndex) {
    if (songIndex < 1 || songIndex > musicLibrary.size()) {
        return "Invalid song selection. Please choose a valid index.";
    }
    String selectedSong = musicLibrary.get(songIndex - 1);
    queue.clear();  // Clear the queue to only play the selected song
    queue.add(selectedSong);
    currentTrackIndex = 0;  // Reset track index to start from the selected song
    try {
        playMusic(selectedSong);
        return "Now playing: " + selectedSong;
    } catch (Exception e) {
        return "Error playing the selected track: " + e.getMessage();
    }
}

    public String addToQueue(String trackName) {
        for (String track : musicLibrary) {
            if (track.toLowerCase().contains(trackName.toLowerCase())) {
                queue.add(track);
                return track + " added to the queue.";
            }
        }
        return "Track not found in the library.";
    }

    public String skip() {
        if (queue.size() > 1) {
            if (currentClip != null && currentClip.isOpen()) {
                currentClip.stop();
                currentClip.close();
            }

            currentTrackIndex++;
            String trackPath = queue.get(currentTrackIndex % queue.size());
            try {
                playMusic(trackPath);
                return "Skipped to: " + trackPath;
            } catch (Exception e) {
                return "Error skipping to the track: " + e.getMessage();
            }
        }
        return "No more tracks to skip.";
    }

    public String repeat() {
        if (currentClip != null && currentClip.isOpen()) {
            currentClip.setFramePosition(0); // Restart the track
            currentClip.start();
            return "Repeating the current track.";
        }
        return "No track is currently playing.";
    }
    public String pause() {
    if (currentClip != null && currentClip.isRunning()) {
        currentClip.stop();
        return "Playback paused.";
    }
    return "No track is currently playing.";
}

public String resume() {
    if (currentClip != null && !currentClip.isRunning()) {
        currentClip.start();
        return "Playback resumed.";
    }
    return "No track is currently paused.";
}
    public String stop() {
    if (currentClip != null && currentClip.isOpen()) {
        currentClip.stop();
        currentClip.close();
        currentClip = null;  // Reset the clip reference
        currentTrackIndex = -1;  // Optionally reset the track index
        return "Playback stopped.";
    }
    return "No track is currently playing.";
}


    private void playMusic(String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        currentClip = AudioSystem.getClip();
        currentClip.open(audioStream);
        currentClip.start();
    }
}
