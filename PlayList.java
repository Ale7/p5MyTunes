import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class which can generate a PlayList object given a title. Includes getter
 * and setter methods for name, playCount (no setter), and songList. Also
 * includes playAll() and toString() methods.
 *
 * @author Alec Wooding
 */
public class PlayList implements PlayListInterface {
	
	private String name;
	private int playCount, side;
	private ArrayList<Song> songList;

	/**
	 * Constructor for the PlayList object
	 * 
	 * @param name
	 *        name of playlist
	 */
	public PlayList(String name) {
		this.name = name;
		playCount = 0;
		songList = new ArrayList<Song>();
	}

	/**
	 * Loads a new playlist from a given file. The file should have the
	 * following format: File Name Song 1 Title Song 1 Artist Song 1 Play time
	 * Song 1 File path Song 2 Title Song 2 Artist Song 2 Play time Song 2 File
	 * path etc.
	 * 
	 * @param filename
	 *        the name of the file to read the songs from
	 * @return a new playlist containing songs with the attributes given in the
	 *         file
	 */
	public PlayList(File file1) {
		songList = new ArrayList<Song>();
		try {
			Scanner scan = new Scanner(file1);
			this.name = scan.nextLine().trim();
			while (scan.hasNextLine()) {
				String title = scan.nextLine().trim();
				String artist = scan.nextLine().trim();
				int playtime = Integer.parseInt(scan.nextLine().trim());
				String file = scan.nextLine().trim();
				Song song = new Song(title, artist, playtime, file);
				this.addSong(song);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.err.println("Failed to load playlist. " + e.getMessage());
		}
	}

	/**
	 * @return name of playlist
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return play count of playlist
	 */
	public int getPlayCount() {
		return this.playCount;
	}

	/**
	 * @return list of songs in playlist
	 */
	public ArrayList<Song> getSongList() {
		return songList;
	}

	/**
	 * @param name
	 *        name of playlist
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param songList
	 *        ArrayList of songs
	 */
	public void setSongList(ArrayList<Song> songList) {
		this.songList = songList;
	}

	/**
	 * @param s
	 *        song
	 */
	public void addSong(Song s) {
		songList.add(s);
	}

	/**
	 * @param id
	 *        index of song in playlist
	 */
	public void removeSong(int id) {
		if (id < songList.size()) {
			songList.remove(id);
		}
	}

	/**
	 * @return number of songs in playlist
	 */
	public int getNumSongs() {
		return songList.size();
	}

	/**
	 * Plays all songs in playlist
	 */
	public void playAll() {
		for (Song i : songList) {
			i.play();
		}
	}

	@Override
	public Song[] getSongArray() {
		Song[] copy = new Song[songList.size()];

		for (int i = 0; i < getNumSongs(); i++) {
			copy[i] = songList.get(i);
		}
		return copy;
	}

	@Override
	public int getTotalPlayTime() {
		int secs = 0;
		for (int i = 0; i < getNumSongs(); i++) {
			secs += songList.get(i).getPlayTime();
		}
		return secs;
	}

	@Override
	public int moveUp(int index) {
		Song songAbove = songList.get(index - 1);
		songList.set(index - 1, songList.get(index));
		songList.set(index, songAbove);
		return index;
	}

	@Override
	public int moveDown(int index) {
		Song songBelow = songList.get(index + 1);
		songList.set(index + 1, songList.get(index));
		songList.set(index, songBelow);
		return index;
	}

	@Override
	public Song[][] getMusicSquare() {
		int q = getNumSongs();
		if (q < 1) {
			q = 1;
		}
		else {
			side = (int) Math.ceil(Math.sqrt(q));
		}		
		Song[][] copy = new Song[side][side];

		for (int i = 0; i < side; i++) {
			for (int j = 0; j < side; j++) {
				int z = (i * side) + j;
				if (z < q) {
					copy[i][j] = songList.get(z);
				} else {
					copy[i][j] = songList.get(z % q);
				}
			}
		}
		return copy;
	}

	/**
	 * @return side length of musicSquare
	 */
	public int getMusicSquareSide() {
		return side;
	}

	/**
	 * @return Playlist title, number of songs, and the list of songs (title,
	 *         artist, play time, file name) in a formatted fashion
	 */
	public String toString() {
		String dash = "------------------";
		String s = "";

		if (songList.size() == 0) {
			return dash + "\n" + getName() + " (" + songList.size() + " songs)\n" + dash +
				   "\nThere are no songs.\n" + dash + "\n";
		} 
		else {
			for (Song i : songList) {
				s = s + "\n" + "(" + songList.indexOf(i) + ") " + i.toString();
			}
			return dash + "\n" + getName() + " (" + songList.size() + " songs)\n" + dash + s + "\n" + dash;
		}
	}
}