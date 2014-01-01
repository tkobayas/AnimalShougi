package jp.gr.java_conf.tkobayas.animalshougi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Chick;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Elephant;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Giraffe;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Lion;

public class AnimalShougi extends Application {
	
	private static List<Animal> defaultPosition = new ArrayList<Animal>();
	
	static {
		defaultPosition.add(new Chick(1, 1, 2));
		defaultPosition.add(new Elephant(1, 0, 3));
		defaultPosition.add(new Lion(1, 1, 3));
		defaultPosition.add(new Giraffe(1, 2, 3));
		defaultPosition.add(new Chick(2, 1, 1));
		defaultPosition.add(new Elephant(2, 2, 0));
		defaultPosition.add(new Lion(2, 1, 0));
		defaultPosition.add(new Giraffe(2, 0, 0));
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException {

		AnchorPane root = FXMLLoader.load(getClass().getResource("GameView.fxml"));
        
		BorderPane borderPane = ((BorderPane)root.getChildren().get(0));
		GridPane gridPane = (GridPane)borderPane.getCenter();
		
        for (Animal animal : defaultPosition) {
        	Image image = animal.getImage();
            ImageView view = new ImageView(image);
            view.setFitWidth(80);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            
            ColorInput unitColor;
            if (animal.getPlayer() == 1) {
            	view.setRotate(0);
            	unitColor = new ColorInput(0, 0, 80, 80, Color.CYAN);
            } else {
            	view.setRotate(180);
            	unitColor = new ColorInput(0, 0, 80, 80, Color.MAGENTA);
            }
            
            Blend blend = new Blend();
            blend.setMode(BlendMode.SOFT_LIGHT);
            blend.setTopInput(unitColor);
            blend.setBottomInput(new DropShadow(2, 6, 6, Color.DARKGRAY));
            blend.setOpacity(0.9);
            view.setEffect(blend);
            
            gridPane.add(view, animal.getX(), animal.getY());
		}

        stage.setScene(new Scene(root));
        stage.show();
        
        Media media = new Media(getClass().getResource("chap-filmmix1.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
	}
}
