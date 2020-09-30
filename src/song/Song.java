package song;

public class Song {
	String name;
	String artist;
	String album;
	int year;
	
	public Song (String name, String artist) {
		this.name = name;
		this.artist = artist;
		this.album = "";
		this.year = -1;
	}
	
	public Song (String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public String toString() {
		return name + " by " + artist; 
	}
	
}
