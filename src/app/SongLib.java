package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Controller;

public class SongLib extends Application {
	
	@Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader rootLoader = new FXMLLoader();
    	rootLoader.setLocation(getClass().getResource("/view/mainwindow.fxml"));
    	BorderPane root = (BorderPane)rootLoader.load();
    	
    	Controller rootController = rootLoader.getController();
        rootController.init();
        
        primaryStage.setTitle("Song Library");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
