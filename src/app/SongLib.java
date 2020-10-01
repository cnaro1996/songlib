package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import song.Song;
import view.Controller;

public class SongLib extends Application {
	
	static Controller rootController;
	
	@Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader rootLoader = new FXMLLoader();
    	rootLoader.setLocation(getClass().getResource("/view/mainwindow.fxml"));
    	BorderPane root = (BorderPane)rootLoader.load();
    	
    	rootController = rootLoader.getController();
        rootController.init(primaryStage);
        
        primaryStage.setTitle("Song Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	public void run() {
        		ObservableList<Song> obsList = rootController.getObsList();
        		try {
        			File library = new File("songLibrary.txt");
        			if(library.exists()) {
        				library.delete();
        			}
        			library.createNewFile();
        			
        			FileWriter libWriter = new FileWriter("songLibrary.txt");
        			for(Song s : obsList) {
        				libWriter.write(s.toFile());
        			}
        			libWriter.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	} 
        }); 
    }
}
    

