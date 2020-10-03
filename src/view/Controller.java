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
import obj.SongComparator;
import song.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Controller {
	@FXML BorderPane mainPane;
	@FXML GridPane blankPane;
	@FXML GridPane detailsPane;
	@FXML GridPane addPane;
	@FXML GridPane editPane;
	@FXML GridPane deletePane;
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
	@FXML Button editFinishBtn;
	@FXML Button editCancelBtn;
	@FXML Button deleteCancelBtn;
	@FXML Button deleteConfirmBtn;
	@FXML TextField editNameTxtField;
	@FXML TextField editArtistTxtField;
	@FXML TextField editAlbumTxtField;
	@FXML TextField editYearTxtField;

	private ObservableList<Song> obsList;

	private Stage mainStage;
	
	public void init(Stage mainStage) {
		this.mainStage = mainStage;
		mainPane.setBottom(blankPane);
		initList();
	}

	/**
	 * Alphabetizes a list of songs by name. If duplicate song names exist,
	 * artist names are then used for order priority. Incomplete - waiting
	 * for further clarification on some edge cases by the instructors.
	 * @param list the list to be alphabetized.
	 * @return the alphabetized version of the list.
	 */
	public void alphabetizeList(ObservableList<Song> list){
		SongComparator sc = new SongComparator();
		Song[] temp = new Song[obsList.size()];
		temp = list.toArray(temp);
		Arrays.sort(temp, sc);
		list.remove(0, list.size());
		for(int i=0; i<temp.length; i++) {
			list.add(temp[i]);
		}
		return;
	}

	/**
	 * Changes the scene views based on which button is clicked.
	 *
	 * @param e
	 */
	public void buttonListener(ActionEvent e) {
		Button b = (Button) e.getSource();
		if (b == addBtn) {
			mainPane.setBottom(addPane);
		} else if (b == confirmAddBtn) {
			confirmAdd();
		} else if (b == cancelAddBtn) {
			clearBottomPane();
			showSongDetails();
		} else if (b == editBtn) {
			setToEditPane();
		} else if(b == editFinishBtn){
			finishEditing();
		} else if (b == editCancelBtn) {
			mainPane.setBottom(blankPane);
			showSongDetails();
		} else if (b == deleteBtn) {
			setToDeletePane();
		} else if (b == deleteConfirmBtn) {
			finishDeletion();
		} else if (b == deleteCancelBtn) {
			mainPane.setBottom(blankPane);
			showSongDetails();
		}
	}

	/**
	 * Reloads and alphabetizes the song list. Preselects a specified song.
	 *
	 * @param selection which song in the song list you'd like to have selected.
	 */
	private void updateSongList(Song selection){
		alphabetizeList(obsList);
		songListView.setItems(obsList);
		songListView.getSelectionModel().select(selection);
		showSongDetails();
	}

	/**
	 * Reloads and alphabetizes the song list. Selects the first song in the list.
	 */
	private void updateSongList(){
		alphabetizeList(obsList);
		songListView.setItems(obsList);
		songListView.getSelectionModel().selectFirst();
		showSongDetails();
	}

	/**
	 * Finished deleting selected song and updates view panes.
	 */
	private void finishDeletion() {
		obsList.remove(songListView.getSelectionModel().getSelectedItem());
		updateSongList();
		if(obsList.isEmpty()) {
			clearBottomPane();
		}
	}

	/**
	 * Sets the bottom pane to the delete scene
	 */
	private void setToDeletePane() {
		if(obsList.isEmpty()) {
			showAlert("Cannot delete from an empty list!", "");
			return;
		}
		mainPane.setBottom(deletePane);
		return;
	}

	/**
	 * Sets the bottom pane to the edit scene
	 */
	private void setToEditPane() {
		Song selection = songListView.getSelectionModel().getSelectedItem();
		if (null != selection) {
			mainPane.setBottom(editPane);
			editNameTxtField.setText(selection.getName());
			editArtistTxtField.setText(selection.getArtist());
			editAlbumTxtField.setText(selection.getAlbum());
			editYearTxtField.setText(selection.getYear());
		} else {
			showAlert("No Song Selected!", "Please select a song to edit");
		}
		return;
	}

	/**
	 * Makes the changes specified by the user and resets the bottom scene.
	 * Only allows legal inputs.
	 */
	private void finishEditing() {
		Song editedSong = new Song(editNameTxtField.getText(),
				editArtistTxtField.getText(),
				editAlbumTxtField.getText(),
				editYearTxtField.getText());

		// Remove and store the old version of the song being edited temporarily.
		Song temp = songListView.getSelectionModel().getSelectedItem();
		obsList.remove(songListView.getSelectionModel().getSelectedItem());

		try {
			if (editedSong.getName().isEmpty() || editedSong.getArtist().isEmpty()) {
				showAlert("Name or artist cannot be Empty!",
						"Please enter a song title and/or artist");
			} else {
				if (isDuplicate(editedSong)) {
					showAlert("This Song Already Exists!", 
							"Cannot add because this song already exists");
				} else {
					if (0 <= Integer.parseInt(editedSong.getYear()) &&
							Integer.parseInt(editedSong.getYear()) <= 2020) {
						obsList.add(editedSong);
						updateSongList(editedSong);
					} else {
						throw new IllegalArgumentException();
					}
				}
			}
		} catch (IllegalArgumentException e) {
			showAlert("Invalid Input!",
					"Please enter a valid year up to 2020, or leave it empty");
			//Re-add the old version of the song before canceling.
			obsList.add(temp);
		}
		return;
	}

	/*
	 * Initialize the song ListView from the library
	 */
	private void initList() {		
		obsList = FXCollections.observableArrayList();
		readLibraryFromFile();
	 	alphabetizeList(obsList);
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
						obsList.add(newSong);
						alphabetizeList(obsList);
						songListView.setItems(obsList);
						songListView.getSelectionModel().select(newSong);
						clearBottomPane();
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
