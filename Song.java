import java.text.DecimalFormat;

/**
 * A class which can generate Song objects given a minimum of a title and an
 * artist. Includes getter and setter methods for title, artist, playTime, and
 * fileName. Also includes a toString() method.
 *
 * @author Alec Wooding
 */
public class Song extends PlayableSong {

	private String title, artist, fileName;
	private int playTime;

	/**
	 * Constructor for the Song object
	 * 
	 * @param title
	 *        title of song
	 * @param artist
	 *        artist of song
	 * @param playTime
	 *        play time of song (seconds)
	 * @param fileName
	 *        filepath of song
	 */
	public Song(String title, String artist, int playTime, String fileName) {
		super(fileName);
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.fileName = fileName;
	}

	/**
	 * @return title of song
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return artist of song
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * @return filepath of song
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @return play time of song (seconds)
	 */
	public int getPlayTime() {
		return this.playTime;
	}

	/**
	 * @param title
	 *        title of song
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param artist
	 *        artist of song
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @param fileName
	 *        file name of song
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param playTime
	 *        play time of song (seconds)
	 */
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	/**
	 * @return song title, artist, play time, and file name in a formatted
	 *         fashion.
	 */
	public String toString() {
		int minutes = playTime / 60;
		int seconds = playTime % 60;
		DecimalFormat decFormat = new DecimalFormat("00");
		return String.format("%-20s %-25s %-25s %-20s", title, artist,
			   decFormat.format(minutes) + ":" + decFormat.format(seconds), fileName);
	}
}