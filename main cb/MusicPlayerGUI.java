import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerGUI {
    private MusicPlayer musicPlayer;
    private JFrame musicPlayerFrame;
    private JTextArea displayArea;
    private JLabel currentSongLabel;

    public MusicPlayerGUI(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void showMusicPlayer() {
        // Create the main frame
        musicPlayerFrame = new JFrame("Music Player");
        musicPlayerFrame.setSize(500, 600);
        musicPlayerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        musicPlayerFrame.setLayout(new BorderLayout());
        musicPlayerFrame.getContentPane().setBackground(new Color(245, 250, 255));

        // Top Panel: Current Song Display
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 144, 255));
        topPanel.setLayout(new BorderLayout());
        currentSongLabel = new JLabel("No song playing", SwingConstants.CENTER);
        currentSongLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        currentSongLabel.setForeground(Color.WHITE);
        topPanel.add(currentSongLabel, BorderLayout.CENTER);
        musicPlayerFrame.add(topPanel, BorderLayout.NORTH);

        // Center: Display Area for Song Library
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 2));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        musicPlayerFrame.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Control Buttons
        JPanel controlPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        controlPanel.setBackground(new Color(220, 230, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton showLibraryButton = createStyledButton("Show Library");
        JButton selectSongButton = createStyledButton("Select Song");
        JButton playButton = createStyledButton("Play");
        JButton pauseButton = createStyledButton("Pause");
        JButton skipButton = createStyledButton("Skip");
        JButton stopButton = createStyledButton("Stop");

        controlPanel.add(showLibraryButton);
        controlPanel.add(selectSongButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(skipButton);
        controlPanel.add(stopButton);

        musicPlayerFrame.add(controlPanel, BorderLayout.SOUTH);

        // Button Listeners
        showLibraryButton.addActionListener(e -> displayArea.setText(musicPlayer.displayLibrary()));

        selectSongButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(musicPlayerFrame, "Enter song number to select:");
            if (input != null) {
                try {
                    int songIndex = Integer.parseInt(input);
                    String message = musicPlayer.selectSong(songIndex);
                    currentSongLabel.setText("Now Playing: " + message.split(":")[1]);
                    displayArea.append("\n" + message + "\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(musicPlayerFrame, "Invalid input. Please enter a number.");
                }
            }
        });

        playButton.addActionListener(e -> updateCurrentSong(musicPlayer.playNext()));
        pauseButton.addActionListener(e -> displayArea.append("\n" + musicPlayer.pause() + "\n"));
        skipButton.addActionListener(e -> updateCurrentSong(musicPlayer.skip()));
        stopButton.addActionListener(e -> {
            displayArea.append("\n" + musicPlayer.stop() + "\n");
            currentSongLabel.setText("No song playing");
        });

        // Add animations and effects
        addHoverEffects(showLibraryButton);
        addHoverEffects(selectSongButton);
        addHoverEffects(playButton);
        addHoverEffects(pauseButton);
        addHoverEffects(skipButton);
        addHoverEffects(stopButton);

        // Display the music player
        musicPlayerFrame.setVisible(true);
    }

    // Helper Method: Update Current Song Label
    private void updateCurrentSong(String message) {
        if (message.startsWith("Now playing:")) {
            currentSongLabel.setText(message.split(":")[1]);
        }
        displayArea.append("\n" + message + "\n");
    }

    // Helper Method: Create Styled Buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(30, 215, 96));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Helper Method: Add Hover Effects to Buttons
    private void addHoverEffects(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(25, 190, 85)); // Darker shade on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 215, 96)); // Original color
            }
        });
    }
}
