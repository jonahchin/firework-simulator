


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * MyController class
 * @author Jonah Chin
 * @version 1.0
 */
public class MyController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Canvas canvas;
    @FXML
    private Canvas backCanvas;
    @FXML
    private Slider angleSlider;
    @FXML
    private Label windLabel;
    @FXML
    private Label angleLabel;
    @FXML
    private Slider windSlider;
    @FXML
    private Button launchButton;
    @FXML
    private Button exitButton;

    private double startingTime;
    ParticleManager manager = null;
    //setting up sound file
    AudioClip noise = new AudioClip(new File("launchSound.mp3").toURI().toString());

    //Time line initialization
	Timeline timeline = new Timeline(
	            new KeyFrame(Duration.ZERO, actionEvent -> drawScene()),
	            new KeyFrame(Duration.millis(1000/60))
	        );

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"aniCanv\" was not injected: check your FXML file 'Ass4FXML.fxml'.";
        assert angleSlider != null : "fx:id=\"angleSlider\" was not injected: check your FXML file 'Ass4FXML.fxml'.";
        assert windSlider != null : "fx:id=\"windSlider\" was not injected: check your FXML file 'Ass4FXML.fxml'.";
        assert launchButton != null : "fx:id=\"launchButton\" was not injected: check your FXML file 'Ass4FXML.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'Ass4FXML.fxml'.";
        assert backCanvas != null : "fx:id=\"backCanvas\" was not injected: check your FXML file 'Ass4FXML.fxml'.";

        angleSlider.valueProperty().addListener((observable, oldVal, newVal) -> angleLabel.setText("Angle (Degrees): " + String.format("%.2f", newVal.doubleValue()))); //adjust slider labels to show value
        windSlider.valueProperty().addListener((observable, oldVal, newVal) -> windLabel.setText("Wind Velocity (Km/h): " + String.format("%.2f", newVal.doubleValue())));
        GraphicsContext gc2 = backCanvas.getGraphicsContext2D(); //backCanvas is only used to display background image
        gc2.drawImage(new Image("background.png"), 0, 0);
    } //end initialize

    @FXML
    void launch(ActionEvent event) {
		startingTime = System.currentTimeMillis();
		noise.play(); // play first launch sound
		try {
			manager = new ParticleManager(windSlider.getValue(), angleSlider.getValue());
			manager.start(0);
		} catch (EnvironmentException except) {
			System.out.println(except.getMessage());
			return;
		} catch (EmitterException except) {
			System.out.println(except.getMessage());
			return;
		}

			timeline.setCycleCount(Timeline.INDEFINITE); //Time line set up
			timeline.play();
    } //end launch

    @FXML
    void exitButtonPress(ActionEvent event) {
    	Stage stage = (Stage) exitButton.getScene().getWindow();
    	stage.close();
    }

    //draws out the graphics to the canvas
	private void drawScene() {
		ArrayList<Particle> fireworks;


		fireworks = manager.getFireworks((System.currentTimeMillis() - startingTime)/1000);
		if(fireworks.size() == 0) {
			timeline.stop();
		}

		GraphicsContext gc = canvas.getGraphicsContext2D();
		double xPos, yPos, xOrg, yOrg;
		gc.clearRect(0, 0, 900, 475); //wipe
		gc.setStroke(Color.BROWN);
		gc.setLineWidth(10);

		//Adjusts the launcher's position
		if(angleSlider.getValue() < -10)
			gc.strokeLine(450,475,446,465);
		else if(angleSlider.getValue() < -5)
			gc.strokeLine(450,475,447,465);
		else if(angleSlider.getValue() < 0)
			gc.strokeLine(450,475,449,465);
		else if(angleSlider.getValue() == 0)
			gc.strokeLine(450,475,450,465);
		else if(angleSlider.getValue() > 10)
			gc.strokeLine(450,475,454,465);
		else if(angleSlider.getValue() > 5)
			gc.strokeLine(450,475,453,465);
		else if(angleSlider.getValue() > 0)
			gc.strokeLine(450,475,451,465);

		gc.setLineWidth(2); //reset for use in streaks

		for(Particle firework : fireworks){
			xPos = firework.getPosition()[0] * 19 + canvas.getWidth() / 2;
			yPos = canvas.getHeight() - firework.getPosition()[1] * 19;
			gc.setFill(firework.getColour());
			gc.setStroke(firework.getColour());

			if (firework instanceof BurningParticle){ //star
				gc.fillOval(xPos, yPos, 6, 6);
				if(firework.getPosition()[1] <= 1 ){
					noise.play();
				}
			}else if (firework instanceof Streak){ //streak
				xOrg = ((Streak) firework).getOrigin()[0] * 19 + canvas.getWidth() / 2;
				yOrg = canvas.getHeight() - ((Streak) firework).getOrigin()[1] * 19;
				gc.strokeLine(xOrg, yOrg, xPos, yPos);

			}else{ //spark
				gc.fillOval(xPos, yPos, 2, 2);
			}
		}
	} //end drawScene
} //end MyController
