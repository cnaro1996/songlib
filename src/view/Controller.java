package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import song.Song;

public class Controller {
	@FXML GridPane blankPane;
	@FXML GridPane detailsPane;
	@FXML ListView<Song> songListView;
	@FXML Text titleDetailText;
	@FXML Text artistDetailText;
	@FXML Text albumDetailText;
	@FXML Text yearDetailText;
	
	private ObservableList<Song> obsList;
	
	public void init() {
		detailsPane.setVisible(false);
		blankPane.setVisible(true);
		initList();
	}
	
	private void initList() {
		//test songs
		Song testSong1 = new Song("Wishing Well", "Juice WRLD", "Legends Never Die", 2020);
		Song testSong2 = new Song("Dark Queen", "Lil Uzi Vert", "Luv Is Rage 2", 2017);
		Song testSong3 = new Song("rockstar (feat. 21 Savage)", "Post Malone", "beerbongs & bentleys", 2018);
		Song testSong4 = new Song("Rambo", "Bryson Tiller", "T R A P S O U L", 2015);
		
		//create an ObservableList from an ArrayList
		obsList = FXCollections.observableArrayList(testSong1, testSong2, testSong3, testSong4);
		songListView.setItems(obsList);
		
		//set listener for the items
		songListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> { showSongDetails(); });
	}
	
	private void showSongDetails() {
		detailsPane.setVisible(true);
		blankPane.setVisible(false);
		Song currentSong = songListView.getSelectionModel().getSelectedItem();
		titleDetailText.setText(currentSong.getName());
		artistDetailText.setText(currentSong.getArtist());
		albumDetailText.setText(currentSong.getAlbum());
		
		if(currentSong.getYear() > 0) {
			yearDetailText.setText(currentSong.getYear() + "");
		} else {
			yearDetailText.setText("");
		}
	}

}
