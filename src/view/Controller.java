/*
 * Christopher Naro	(cjn64)
 * Zabir Rahman (zwr3)
 * 
 * Software Methodology
 * Assignment 1: SongLib
 */

package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import song.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 

public class Controller {
	@FXML BorderPane mainPane;
	@FXML GridPane blankPane;
	@FXML GridPane detailsPane;
	@FXML GridPane addPane;
	@FXML ListView<Song> songListView;
	@FXML Text nameDetailText;
	@FXML Text artistDetailText;
	@FXML Text albumDetailText;
	@FXML Text yearDetailText;
	@FXML Button addBtn;
	@FXML Button editBtn;
	@FXML Button deleteBtn;
	@FXML TextField nameTxtField;
	@FXML TextField artistTxtField;
	@FXML TextField albumTxtField;
	@FXML TextField yearTxtField;
	@FXML Button confirmAddBtn;
	@FXML Button cancelAddBtn;
	
	
	private ObservableList<Song> obsList;

	private Stage mainStage;
	
	public void init(Stage mainStage) {
		this.mainStage = mainStage;
		mainPane.setBottom(blankPane);
		initList();
	}
	
	public void buttonListener(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == addBtn) {
			mainPane.setBottom(addPane);
		}
		else if (b == confirmAddBtn) {
			confirmAdd();
		}
		else if (b == cancelAddBtn) {
			clearBottomPane();
			showSongDetails();
		}
		else if (b == editBtn) {
//			TODO: CHRIS
		}
		else if (b == deleteBtn) {
//			TODO: CHRIS
		}
	}
	
	/*
	 * Initialize the song ListView from the library
	 */
	private void initList() {		
		obsList = FXCollections.observableArrayList();
		readLibraryFromFile();
//		TODO:  CHRIS - SORT LIST 
		if(!obsList.isEmpty()) {
			songListView.getSelectionModel().select(0);
			showSongDetails();
		}
		songListView.getSelectionModel().selectedIndexProperty().addListener(
				(obs, oldVal, newVal) -> { showSongDetails(); });
	}
	
	/*
	 * Display song details pane and populate with correct info
	 */
	private void showSongDetails() {
		mainPane.setBottom(detailsPane);
		Song currentSong = songListView.getSelectionModel().getSelectedItem();
		if(null != currentSong) {
			String name = currentSong.getName().length() > 25 ? 
					currentSong.getName().substring(0, 21) + "..." : currentSong.getName();
			String artist = currentSong.getArtist().length() > 25 ? 
					currentSong.getArtist().substring(0, 21) + "..." : currentSong.getArtist();
			String album = currentSong.getAlbum().length() > 35 ? 
					currentSong.getAlbum().substring(0, 31) + "..." : currentSong.getAlbum();
			
			nameDetailText.setText(name);
			artistDetailText.setText(artist);
			albumDetailText.setText(album);
			yearDetailText.setText(currentSong.getYear());
		}
	}
	
	/*
	 * Add a song to the list by getting the user entered 
	 * attributes from the text fields. 
	 * Pops up an error if the name and/or artist field is empty 
	 */
	private void confirmAdd() {
		String name = nameTxtField.getText();
		String artist = artistTxtField.getText();
		String album = albumTxtField.getText();
		String year = yearTxtField.getText();
		
		if(name.isEmpty() || artist.isEmpty()) {
			showAlert("Name and Artist Cannot be Empty!", 
					"Please enter a song title and/or artist");
		} else {
			Song newSong = new Song(name, artist, album, year);
			if(isDuplicate(newSong)) {
				showAlert("This Song Already Exists!", 
						"Cannot add because this song already exists");
			} else {
				try {
					if(0 <= Integer.parseInt(year) && Integer.parseInt(year) <= 2020) {
//						TODO: CHRIS - ADD SONG IN CORRCT POSITION 
						obsList.add(newSong);
						songListView.setItems(obsList);
						songListView.getSelectionModel().select(newSong);
						showSongDetails();
					} else {
						throw new IllegalArgumentException();
					}
				} catch (Exception e) {
					showAlert("Invalid Input!", 
							"Please enter a valid year up to 2020, or leave it empty");
				}
			}
		}
	}
	
	/*
	 * Reads in the song library from a text file called "songLibrary.txt"
	 * Uses '~' to separate song attributes (name, artist, album, year)
	 * Uses '\n' to separate individual songs 
	 * Reads one character at a time
	 */
	private void readLibraryFromFile() {
		try {
			File library = new File("songLibrary.txt");
		    if(library.exists() && library.length() != 0) {
				BufferedReader libReader = new BufferedReader(new FileReader(library));
				int buffer = 0; //variable to hold what's being read in
				int count = 0; //count to track which song attribute is being read
				String current = ""; //holds the current song attribute
				String name = "";
				String artist = "";
				String album = "";
				String year = "";
				while((buffer = libReader.read()) != -1) {
					//convert int value read to a character 
					char character = (char) buffer; 
					if(character == '~') { 
						if (count == 0) {
							name = String.valueOf(current);
						}
						else if (count == 1) {
							artist = String.valueOf(current);
						}
						else if (count == 2) {
							album = String.valueOf(current);
						}
						//increment count to correspond to the next attribute
						count ++; 
						//reset the current attribute 
						current = ""; 
					}
					else if (character == '\n') {
						//last attribute read in before a new song is the year
						year = String.valueOf(current); 
						Song newSong = new Song (name, artist, album, year);
						obsList.add(newSong);
						
						//reset all fields for the next song
						current = "";
						name = "";
						artist = "";
						album = "";
						year = "";
						count = 0;
					} else {
						//concat the current character to the current attribute 
						current = current.concat(""+character);
					}
				}
				libReader.close();
		     }
		    songListView.setItems(obsList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/*
	 * Resets all field in the different bottom panes 
	 * and sets the bottom to the blank pane
	 */
	private void clearBottomPane () {
		nameTxtField.clear();
		artistTxtField.clear();
		albumTxtField.clear();
		yearTxtField.clear();
		mainPane.setBottom(blankPane);
	}
	
	/*
	 * Checks to see if song already exists 
	 */
	private boolean isDuplicate(Song toCheck) {
		for (Song s : obsList) {
			if(s.equals(toCheck)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Pop up alert
	 */
	private void showAlert(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public ObservableList<Song> getObsList() {
		return obsList;
	}
}
