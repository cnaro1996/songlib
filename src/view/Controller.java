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
	
	private void initList() {		
		//create an ObservableList from an ArrayList
		obsList = FXCollections.observableArrayList();
		
		try {
			File library = new File("songLibrary.txt");
		    if(library.exists() && library.length() != 0) {
				BufferedReader libReader = new BufferedReader(new FileReader(library));
				int buffer = 0;
				int count = 0;
				String word = "";
				String name = "";
				String artist = "";
				String album = "";
				String year = "";
				while((buffer = libReader.read()) != -1) {
					char character = (char) buffer;
					if(character == '~') {
						if (count == 0) {
							name = String.valueOf(word);
						}
						else if (count == 1) {
							artist = String.valueOf(word);
						}
						else if (count == 2) {
							album = String.valueOf(word);
						}
						count ++;
						word = "";
					}
					else if (character == '\n') {
						year = String.valueOf(word);
						
						Song newSong = new Song (name, artist, album, year);
						obsList.add(newSong);
						
						word = "";
						name = "";
						artist = "";
						album = "";
						year = "";
						count = 0;
					} else {
						word = word.concat(""+character);
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
		
		//set listener for the items
		songListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> { showSongDetails(); });
	}
	
	private void showSongDetails() {
		mainPane.setBottom(detailsPane);
		Song currentSong = songListView.getSelectionModel().getSelectedItem();
		nameDetailText.setText(currentSong.getName());
		artistDetailText.setText(currentSong.getArtist());
		albumDetailText.setText(currentSong.getAlbum());
		yearDetailText.setText(currentSong.getYear());
	}
	
	public void buttonListener(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == addBtn) {
			mainPane.setBottom(addPane);
		}
		else if (b == confirmAddBtn) {
			String name = nameTxtField.getText();
			String artist = artistTxtField.getText();
			String album = albumTxtField.getText();
			String year = yearTxtField.getText();
			
			if(name.isEmpty() || artist.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Error");
				alert.setHeaderText("Name and Artist cannot be empty");
				String content = "Please enter a song title and/or artist";
				alert.setContentText(content);
				alert.showAndWait();
			} else {
				Song newSong = new Song(name, artist, album, year);
				obsList.add(newSong);
				songListView.setItems(obsList);
				resetBottomPane();
			}
		}
		else if (b == cancelAddBtn) {
			resetBottomPane();
		}
	}
	
	private void resetBottomPane () {
		nameTxtField.clear();
		artistTxtField.clear();
		albumTxtField.clear();
		yearTxtField.clear();
		mainPane.setBottom(blankPane);
	}
	
	public ObservableList<Song> getObsList() {
		return obsList;
	}
}
