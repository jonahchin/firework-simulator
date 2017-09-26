package org.fireworksimulator;

import org.fireworksimulator.util.URLHandler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SplitPane root = (SplitPane) FXMLLoader.load(getClass().getResource(URLHandler.getResource("Ass4FXML.fxml")));
		Scene scene = new Scene(root, 900, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Roman Candle Simulation Version 1.9");
		primaryStage.show();
	}

}
