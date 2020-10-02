package obj;

import java.util.Comparator;
import song.Song;

public class SongComparator implements Comparator<Song> {
    public int compare(Song s1, Song s2) {
        // First compare song names
        if(s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase()) == 0) {
            // If they have the same name, compare artist names.
            return s1.getArtist().toLowerCase().compareTo(s2.getArtist().toLowerCase());
        } else {
            return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
        }
    }
}
