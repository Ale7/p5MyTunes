import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.Box;

/**
 * The main panel for MyTunes. The bulk of the code for the application is
 * contained withinthis class, including components, events, and listeners.
 *
 * @author Alec Wooding
 */
@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel {
	private Color uiColor; //Color used across majority of GUI
	private MyTunesPlayList playlist; // Playlist 'data'
	private JList<Song> songList;
	private Timer timer;
	private Song[][] musicSquare; // Music square 'data'
	private JButton[][] musicSquareButtons; // Music square buttons
	private BufferedImage play32Icon, pause32Icon, blackLoop48Icon, orangeLoop48Icon; // Button Icons
	private File file; // Song file uploaded when adding to playlist
	
	private JLabel playlistInfoLabel, playingLabel, playlistNameLabel;
	private JPanel controlButtonsPanel, heatmapSongPanel, masterPanel, playlistPanel, leftPanel,
				   topControlPanel, bottomControlPanel;
	private JButton playlistNameButton, loopButton, playButton, nextButton, prevButton,
			        addSongButton, removeSongButton, upButton, downButton;
	/**
	 * Initializes all GUI components and creates several variables
	 */
	public MyTunesGUIPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		uiColor = new Color(52, 152, 219);

		playlist = new MyTunesPlayList(new File("sounds/playlist.txt"));
		
		songList = new JList<Song>();
		songList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		songList.setListData(playlist.getSongArray());
		songList.setSelectedIndex(0);
		
		timer = new Timer(0, new TimerListener());
		timer.setRepeats(false);

		musicSquare = playlist.getMusicSquare();

		initPlaylistInfoPanel();
		initMasterPanel();
	}
	
	/**
	 * Initializes playlistInfoPanel and adds it to MyTunesGUIPanel.
	 */
	private void initPlaylistInfoPanel() {
		JPanel playlistInfoPanel = new JPanel();
		playlistInfoPanel.setLayout(new BoxLayout(playlistInfoPanel, BoxLayout.X_AXIS));
		playlistInfoPanel.setBackground(uiColor);
		
		playlistNameLabel = new JLabel(playlist.getName());
		playlistNameLabel.setFont(new Font("Arial", Font.BOLD, 30));
		
		playlistNameButton = new JButton();
		playlistNameButton.setBackground(new Color(41, 128, 185));
		playlistNameButton.setBorderPainted(false);
		playlistNameButton.addActionListener(new PlaylistNamerListener());
		playlistNameButton.add(playlistNameLabel);
		
		loopButton = new JButton();
		loopButton.setOpaque(false);
		loopButton.setContentAreaFilled(false);
		loopButton.setBorderPainted(false);
		loopButton.addActionListener(new PlaylistNamerListener());
		try {
			orangeLoop48Icon = ImageIO.read(getClass().getResource("images/orangeloop48.png"));
			blackLoop48Icon = ImageIO.read(getClass().getResource("images/blackloop48.png"));
			loopButton.setIcon(new ImageIcon(blackLoop48Icon));
			loopButton.setBorder(null);
		} catch (IOException ex) {
		}

		int playlistMinutes = playlist.getTotalPlayTime() / 60;
		int playlistSeconds = playlist.getTotalPlayTime() % 60;
		playlistInfoLabel = new JLabel("Songs: " + playlist.getNumSongs() + " ~ Time: " + 
		playlistMinutes + ":" + playlistSeconds);
		playlistInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

		playlistInfoPanel.add(Box.createVerticalStrut(5));
		playlistInfoPanel.add(playlistNameButton);
		playlistInfoPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		playlistInfoPanel.add(loopButton);
		playlistInfoPanel.add(Box.createVerticalStrut(5));
		playlistInfoPanel.add(playlistInfoLabel);
		playlistInfoPanel.add(Box.createVerticalStrut(5));
		
		this.add(playlistInfoPanel);
	}
	
	/**
	 * Initializes masterPanel and adds it to MyTunesGUIPanel.
	 */
	private void initMasterPanel() {
		masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.X_AXIS));
		masterPanel.setBackground(uiColor);
		
		initLeftPanel();
		initHeatmapSongPanel();
		
		this.add(masterPanel);
	}
	
	/**
	 * Initializes leftPanel and adds it to masterPanel.
	 */
	private void initLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(uiColor);
		
		leftPanel.add(Box.createVerticalStrut(5));
		initPlaylistPanel();
		leftPanel.add(Box.createVerticalStrut(5));
		initTopControlPanel();
		initBottomControlPanel();
		
		masterPanel.add(leftPanel);
	}
	
	/**
	 * Initializes playlistPanel and adds it to leftPanel.
	 */
	private void initPlaylistPanel() {
		playlistPanel = new JPanel();
		playlistPanel.setLayout(new BoxLayout(playlistPanel, BoxLayout.X_AXIS));
		playlistPanel.setBackground(uiColor);
		
		playlistPanel.add(Box.createHorizontalStrut(5));
		initPlaylistOrganizerPanel();
		playlistPanel.add(Box.createHorizontalStrut(5));
		initSongListPanel();
		
		leftPanel.add(playlistPanel);
	}
	
	/**
	 * Initializes playlistOrganizerPanel and adds it to playlistPanel.
	 */
	private void initPlaylistOrganizerPanel() {
		JPanel playlistOrganizerPanel = new JPanel();
		playlistOrganizerPanel.setLayout(new BoxLayout(playlistOrganizerPanel, BoxLayout.Y_AXIS));
		playlistOrganizerPanel.setBackground(uiColor);
		
		upButton = new JButton();
		upButton.setOpaque(false);
		upButton.setContentAreaFilled(false);
		upButton.setBorderPainted(false);
		upButton.addActionListener(new PlaylistOrganizerListener());
		try {
			BufferedImage img = ImageIO.read(getClass().getResource("images/up32.png"));
			upButton.setIcon(new ImageIcon(img));
			upButton.setBorder(null);
		} catch (IOException ex) {
		}
		
		downButton = new JButton();
		downButton.setOpaque(false);
		downButton.setContentAreaFilled(false);
		downButton.setBorderPainted(false);
		downButton.addActionListener(new PlaylistOrganizerListener());
		try {
			BufferedImage img = ImageIO.read(getClass().getResource("images/down32.png"));
			downButton.setIcon(new ImageIcon(img));
			downButton.setBorder(null);
		} catch (IOException ex) {
		}
		
		playlistOrganizerPanel.add(upButton);
		playlistOrganizerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		playlistOrganizerPanel.add(downButton);
		playlistPanel.add(playlistOrganizerPanel);
	}
	
	/**
	 * Initializes songListPanel and adds it to playlistPanel.
	 */
	private void initSongListPanel() {
		JPanel songListPanel = new JPanel();
		songListPanel.setLayout(new BoxLayout(songListPanel, BoxLayout.Y_AXIS));
		
		JScrollPane songScrollPane = new JScrollPane(songList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
													 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		songListPanel.add(songScrollPane);
		playlistPanel.add(songListPanel);
	}
	
	/**
	 * Initializes topControlPanel and adds it to leftPanel.
	 */
	private void initTopControlPanel() {
		topControlPanel = new JPanel();
		topControlPanel.setLayout(new BoxLayout(topControlPanel, BoxLayout.X_AXIS));
		topControlPanel.setBackground(uiColor);

		addSongButton = new JButton("Add Song");
		addSongButton.setBackground(new Color(41, 128, 185));
		addSongButton.setBorderPainted(false);
		addSongButton.addActionListener(new ControlPanelListener());

		removeSongButton = new JButton("Remove Song");
		removeSongButton.setBackground(new Color(41, 128, 185));
		removeSongButton.setBorderPainted(false);
		removeSongButton.addActionListener(new ControlPanelListener());

		topControlPanel.add(Box.createHorizontalGlue());
		topControlPanel.add(addSongButton);
		topControlPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		topControlPanel.add(removeSongButton);
		topControlPanel.add(Box.createHorizontalGlue());
		
		leftPanel.add(topControlPanel);
	}
	
	/**
	 * Initializes bottomControlPanel and adds it to leftPanel.
	 */
	private void initBottomControlPanel() {
		bottomControlPanel = new JPanel();
		bottomControlPanel.setLayout(new BoxLayout(bottomControlPanel, BoxLayout.Y_AXIS));
		bottomControlPanel.setBackground(uiColor);

		playingLabel = new JLabel("Nothing - Nobody");
		playingLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
		JPanel playingPanel = new JPanel();
		playingPanel.setLayout(new BoxLayout(playingPanel, BoxLayout.X_AXIS));
		playingPanel.setBackground(uiColor);
		
		controlButtonsPanel = new JPanel();
		controlButtonsPanel.setLayout(new BoxLayout(controlButtonsPanel, BoxLayout.X_AXIS));
		controlButtonsPanel.setBackground(uiColor);

		prevButton = new JButton();
		prevButton.setOpaque(false);
		prevButton.setContentAreaFilled(false);
		prevButton.setBorderPainted(false);
		prevButton.addActionListener(new ControlPanelListener());
		try {
			BufferedImage img = ImageIO.read(getClass().getResource("images/prev32.png"));
			prevButton.setIcon(new ImageIcon(img));
			prevButton.setBorder(null);
		} catch (IOException ex) {
		}

		playButton = new JButton();
		playButton.setOpaque(false);
		playButton.setContentAreaFilled(false);
		playButton.setBorderPainted(false);
		playButton.addActionListener(new ControlPanelListener());
		try {
			play32Icon = ImageIO.read(getClass().getResource("images/play32.png"));
			pause32Icon = ImageIO.read(getClass().getResource("images/pause32.png"));
			playButton.setIcon(new ImageIcon(play32Icon));
			playButton.setBorder(null);
		} catch (IOException ex) {
		}

		nextButton = new JButton();
		nextButton.setOpaque(false);
		nextButton.setContentAreaFilled(false);
		nextButton.setBorderPainted(false);
		nextButton.addActionListener(new ControlPanelListener());
		try {
			BufferedImage img = ImageIO.read(getClass().getResource("images/next32.png"));
			nextButton.setIcon(new ImageIcon(img));
			nextButton.setBorder(null);
		} catch (IOException ex) {
		}

		controlButtonsPanel.add(Box.createHorizontalGlue());
		controlButtonsPanel.add(prevButton);
		controlButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		controlButtonsPanel.add(playButton);
		controlButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		controlButtonsPanel.add(nextButton);
		controlButtonsPanel.add(Box.createHorizontalGlue());

		playingPanel.add(Box.createHorizontalGlue());
		playingPanel.add(playingLabel);
		playingPanel.add(Box.createHorizontalGlue());
		
		bottomControlPanel.add(Box.createVerticalStrut(5));
		bottomControlPanel.add(playingPanel);
		bottomControlPanel.add(Box.createVerticalStrut(5));
		bottomControlPanel.add(controlButtonsPanel);
		bottomControlPanel.add(Box.createVerticalStrut(5));
		
		leftPanel.add(bottomControlPanel);
	}
	
	/**
	 * Initializes heatmapSongPanel and adds it to masterPanel.
	 */
	private void initHeatmapSongPanel() {
		heatmapSongPanel = new JPanel();
		heatmapSongPanel.setLayout(new GridLayout(playlist.getMusicSquareSide(), playlist.getMusicSquareSide()));
		heatmapSongPanel.setPreferredSize(new Dimension(400, 400));
		heatmapSongPanel.setBackground(uiColor);

		musicSquareButtons = new JButton[playlist.getMusicSquareSide()][playlist.getMusicSquareSide()];

		for (int i = 0; i < musicSquareButtons.length; i++) {
			for (int j = 0; j < musicSquareButtons[i].length; j++) {
				musicSquareButtons[i][j] = new JButton();
				heatmapSongPanel.add(musicSquareButtons[i][j]);
				musicSquareButtons[i][j].setBackground(getHeatMapColor(0));
				musicSquareButtons[i][j].setText(musicSquare[i][j].getTitle());
				musicSquareButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 10));
				musicSquareButtons[i][j].addActionListener(new MusicSquareListener());
			}
		}
		
		masterPanel.add(heatmapSongPanel);
	}

	/**
     * Given the number of times a song has been played, this method will
     * return a corresponding heat map color.
     *
     * Sample Usage: Color color = getHeatMapColor(song.getTimesPlayed());
     *
     * This algorithm was borrowed from:
     * http://www.andrewnoske.com/wiki/Code_-_heatmaps_and_color_gradients
     *
     * @param plays The number of times the song that you want the color for has been played.
     * @return The color to be used for your heat map.
     */
    private Color getHeatMapColor(int plays)
    {
         double minPlays = 0, maxPlays = PlayableSong.MAX_PLAYS;    // upper/lower bounds
         double value = (plays - minPlays) / (maxPlays - minPlays); // normalize play count

         // The range of colors our heat map will pass through. This can be modified if you
         // want a different color scheme.
         Color[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED };
         int index1, index2; // Our color will lie between these two colors.
         float dist = 0;     // Distance between "index1" and "index2" where our value is.

         if (value <= 0) {
              index1 = index2 = 0;
         } else if (value >= 1) {
              index1 = index2 = colors.length - 1;
         } else {
              value = value * (colors.length - 1);
              index1 = (int) Math.floor(value); // Our desired color will be after this index.
              index2 = index1 + 1;              // ... and before this index (inclusive).
              dist = (float) value - index1; // Distance between the two indexes (0-1).
         }

         int r = (int)((colors[index2].getRed() - colors[index1].getRed()) * dist)
                   + colors[index1].getRed();
         int g = (int)((colors[index2].getGreen() - colors[index1].getGreen()) * dist)
                   + colors[index1].getGreen();
         int b = (int)((colors[index2].getBlue() - colors[index1].getBlue()) * dist)
                   + colors[index1].getBlue();

         return new Color(r, g, b);
    }
    
    /**
	 * Updates playlistInfoLabel - utilized when loading playlist, adding/removing songs.
	 */
	private void updateInfoLabel() {
		playlistInfoLabel.setText("Songs: " + playlist.getNumSongs() + " ~ Time: "
				+ (playlist.getTotalPlayTime() / 60) + ":" + (playlist.getTotalPlayTime() % 60));
	}
	
	/**
	 * Updates playButton - manages the play/pause icon for the play button.
	 */
	private void updatePlayButton() {
		if (playlist.isPlaying() == true) {
			playButton.setIcon(new ImageIcon(pause32Icon));
		}
		else {
			playButton.setIcon(new ImageIcon(play32Icon));
		}
	}
	
	/**
	 * Updates playingLabel - utilized when no song is playing or when a song beings to play.
	 */
	private void updatePlayingLabel() {
		if (playlist.isPlaying() == true) {
			playingLabel.setText(playlist.getPlaying().getTitle() + " - " + playlist.getPlaying().getArtist());
		}
		else {
			playingLabel.setText("Nothing - Nobody");
		}
		
	}

	/**
	 * Rebuilds the heatmapSongPanel, which allows the music square to stay updated properly.
	 */
	public void updateMusicSquare() {
		try {
	
			masterPanel.remove(heatmapSongPanel);

			heatmapSongPanel = new JPanel();
			heatmapSongPanel.setLayout(new GridLayout(playlist.getMusicSquareSide(), playlist.getMusicSquareSide()));
			heatmapSongPanel.setPreferredSize(new Dimension(400, 400));
			heatmapSongPanel.setBackground(uiColor);

			musicSquare = playlist.getMusicSquare();

			if (playlist.getNumSongs() == 0) {
				musicSquareButtons = new JButton[1][1];
			}
			else {
				musicSquareButtons = new JButton[playlist.getMusicSquareSide()][playlist.getMusicSquareSide()];
			}

			for (int i = 0; i < musicSquareButtons.length; i++) {
				for (int j = 0; j < musicSquareButtons[i].length; j++) {
					musicSquareButtons[i][j] = new JButton();
					heatmapSongPanel.add(musicSquareButtons[i][j]);
					musicSquareButtons[i][j].setText(musicSquare[i][j].getTitle());	
					musicSquareButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 10));
					musicSquareButtons[i][j].setBackground(getHeatMapColor(musicSquare[i][j].getTimesPlayed()));
					musicSquareButtons[i][j].addActionListener(new MusicSquareListener());
				}
			}

			masterPanel.add(heatmapSongPanel);
			
			this.add(masterPanel);
			this.revalidate();
		} catch (NumberFormatException nfe) {
		}
	}

	/**
	 * Form displayed when user renames playlist.
	 */
	private void renamePlaylistForm() {
		JPanel renamePlaylistPanel = new JPanel();
		renamePlaylistPanel.setLayout(new BoxLayout(renamePlaylistPanel, BoxLayout.Y_AXIS));

		JTextField newPlaylistName = new JTextField(20);
		renamePlaylistPanel.add(newPlaylistName);

		int result = JOptionPane.showConfirmDialog(null, renamePlaylistPanel, "Rename Playlist",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String playlistName = newPlaylistName.getText();
			playlist.setName(playlistName);
			playlistNameLabel.setText(playlist.getName());
		}
	}

	/**
	 * File chooser displayed when user adds a song to playlist.
	 */
	private void fileChooser() {
		JPanel addSongPanel = new JPanel();
		addSongPanel.setLayout(new BoxLayout(addSongPanel, BoxLayout.Y_AXIS));

		JFileChooser chooser = new JFileChooser(".");
		JTextArea filePreview = new JTextArea(20, 20);

		int status = chooser.showOpenDialog(null);

		if (status != JFileChooser.APPROVE_OPTION) {
			filePreview.setText("No File Chosen!");
		} else {
			file = chooser.getSelectedFile();
			addSongForm();
		}
	}

	/**
	 * Form displayed when user adds a song to playlist.
	 */
	private void addSongForm() {
		JPanel formInputPanel = new JPanel();
		formInputPanel.setLayout(new BoxLayout(formInputPanel, BoxLayout.Y_AXIS));

		JTextField titleField = new JTextField(20);
		JTextField artistField = new JTextField(20);
		JTextField playTimeField = new JTextField(20);

		formInputPanel.add(new JLabel("Title: "));
		formInputPanel.add(titleField);
		formInputPanel.add(new JLabel("Artist: "));
		formInputPanel.add(artistField);
		formInputPanel.add(new JLabel("Play time (sec): "));
		formInputPanel.add(playTimeField);

		int result = JOptionPane.showConfirmDialog(null, formInputPanel, "Add Song", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String title = titleField.getText();
			String artist = artistField.getText();
			int playTime = 0;
			String fileName = file.toString();
			try {
				playTime = Integer.parseInt(playTimeField.getText());
				if (playTime <= 0) {
					JOptionPane.showMessageDialog(null, "Invalid play time!");
				}
				Song newSong = new Song(title, artist, playTime, fileName);
				playlist.addSong(newSong);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Play time must be a number!");
			}
			songList.setListData(playlist.getSongArray());
		}
		songList.setSelectedIndex(0);
	}
	
	/**
	 * Form displayed when user removes a song from playlist.
	 */
	private void removeSongForm() {
		int i = songList.getSelectedIndex();
		JPanel removeSongPanel = new JPanel();
		removeSongPanel.setLayout(new BoxLayout(removeSongPanel, BoxLayout.Y_AXIS));

		JLabel confirmationLabel = new JLabel(songList.getSelectedValue().getTitle() 
											  + " - " + songList.getSelectedValue().getArtist());

		removeSongPanel.add(confirmationLabel);

		int result = JOptionPane.showConfirmDialog(null, removeSongPanel, "Remove Song", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			playlist.removeSong(i);
			if (playlist.isPlaying() == true) {
				playlist.stop();
				playButton.setIcon(new ImageIcon(play32Icon));
				updatePlayButton();
				updatePlayingLabel();
			}
			songList.setListData(playlist.getSongArray());
			if (playlist.getNumSongs() != 0) {
				songList.setSelectedIndex(0);
			}
		}
	}
	
	/**
	 * Sets the timer inital delay to the specified play time for song and starts the timer.
	 */
	private void startTimer() {
		int playTime = 1000 * playlist.getPlaying().getPlayTime();
		timer.setInitialDelay(playTime);
		timer.start();
	}
	
	/**
	 * Stops the timer.
	 */
	private void stopTimer() {
		timer.stop();
	}
	
	/**
	 * Resets the timer - used every time a song beings to play.
	 */
	private void resetTimer() {
		stopTimer();
		startTimer();
	}
	
	/**
	 * Renaming playlist, enable/disable loop mode.
	 */
	private class PlaylistNamerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			// Rename playlist
			if (e.getSource() == playlistNameButton) {
				renamePlaylistForm();
			}
			
			// Enable/disable loop mode (Flexible play all)
			if (e.getSource() == loopButton) {
				if (playlist.isSequentialPlay() == false) {
					playlist.setSequentialPlay(true);
					loopButton.setIcon(new ImageIcon(orangeLoop48Icon));
				} else {
					playlist.setSequentialPlay(false);
					loopButton.setIcon(new ImageIcon(blackLoop48Icon));
				}
			}
		}
	}
	
	/**
	 * Changing order of playlist.
	 */
	private class PlaylistOrganizerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			int i = songList.getSelectedIndex();

			// Move selected song up
			if (e.getSource() == upButton) {
				if (i - 1 >= 0) {
					playlist.moveUp(i);
					songList.setListData(playlist.getSongArray());
					songList.setSelectedIndex(i - 1);
					updateMusicSquare();
				}
			}

			// Move selected song down
			if (e.getSource() == downButton) {
				if (i + 1 < playlist.getNumSongs()) {
					playlist.moveDown(i);
					songList.setListData(playlist.getSongArray());
					songList.setSelectedIndex(i + 1);
					updateMusicSquare();
				}
			}
		}
	}

	/**
	 * Adding and removing songs, playing/pausing, skipping to next/previous song.
	 */
	private class ControlPanelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addSongButton) {
				fileChooser();
				updateInfoLabel();
				updateMusicSquare();
			}

			if (e.getSource() == removeSongButton) {
				removeSongForm();
				updateInfoLabel();
				updateMusicSquare();
			}

			if (e.getSource() == playButton) {
				if (playlist.isPlaying() == false) {
					playlist.play(songList.getSelectedIndex());
					resetTimer();			
					updatePlayButton();
					updatePlayingLabel();
					updateMusicSquare();
				} else {
					playlist.stop();
					stopTimer();
					updatePlayButton();
					updatePlayingLabel();
				}
			}

			if (e.getSource() == prevButton) {
				if (playlist.isPlaying() == false) {
					int current = songList.getSelectedIndex();
					int prev = current - 1;
					if (prev < 0) {
						prev = playlist.getSongList().size() - 1;
					}
					playlist.play(prev);
				} else {
					playlist.playPrev();
				}
				resetTimer();
				updateMusicSquare();
				songList.setSelectedIndex(playlist.getPlayingIndex());
				updatePlayButton();
				updatePlayingLabel();
			}

			if (e.getSource() == nextButton) {
				if (playlist.isPlaying() == false) {
					int current = songList.getSelectedIndex();
					int next = current + 1;
					if (next >= playlist.getSongList().size()) {
						next = 0;
					}
					playlist.play(next);
				} else {
					playlist.playNext();
				}
				resetTimer();
				updateMusicSquare();
				songList.setSelectedIndex(playlist.getPlayingIndex());
				updatePlayButton();
				updatePlayingLabel();
			}
		}
	}
	
	/**
	 * Updates appropiate parts of application when user interacts with music square.
	 */
	private class MusicSquareListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < musicSquareButtons.length; i++) {
				for (int j = 0; j < musicSquareButtons[i].length; j++) {
					if (e.getSource() == musicSquareButtons[i][j]) {
						int index = ((i * playlist.getMusicSquareSide()) + j) % (playlist.getNumSongs());
						songList.setSelectedIndex(index);
						playlist.play(songList.getSelectedIndex());
						resetTimer();
						updatePlayButton();
						updatePlayingLabel();
						updateMusicSquare();
					}
				}
			}
		}
	}
	
	/**
	 * Updates appropiate parts of application when the timer fires.
	 */
	private class TimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Continue to play when loop mode selected
			if (playlist.isSequentialPlay() == true) {
				int nextSongIndex = songList.getSelectedIndex() + 1;
				if (nextSongIndex >= playlist.getSongList().size()) {
					nextSongIndex = 0;
				}
				songList.setSelectedIndex(nextSongIndex);
				playlist.play(nextSongIndex);
				resetTimer();			
				updatePlayButton();
				updatePlayingLabel();
				updateMusicSquare();
			}
			// Stop playing when loop mode not selected
			else {
				playlist.stop();
				stopTimer();
				updatePlayButton();
				updatePlayingLabel();
			}
		}
	}
}