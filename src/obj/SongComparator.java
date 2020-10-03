/*
 * Christopher Naro	(cjn64)
 * Zabir Rahman (zwr3)
 *
 * Software Methodology
 * Assignment 1: SongLib
 */
package obj;

import java.util.Comparator;
import song.Song;

public class SongComparator implements Comparator<Song> {
    public int compare(Song s1, Song s2) {
        // First compare song names
        if(s1.getName().compareTo(s2.getName()) == 0) {
            // If they have the same name, compare artist names.
            return s1.getArtist().compareTo(s2.getArtist());
        } else {
            return s1.getName().compareTo(s2.getName());
        }
    }
}
