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
import javafx.geometry.Point2D;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	
	GridPane gridPane;
	
	// grid calculate
	private double gridLeft;
	private double gridTop;
	private double gridWidth;
	private double gridHeight;
	
	// drag&drop tips
    private double initX;
    private double initY;
	private Point2D dragAnchor;

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

		BorderPane borderPane = ((BorderPane) root.getChildren().get(0));
		gridPane = (GridPane) borderPane.getCenter();

		for (Animal animal : defaultPosition) {
			Image image = animal.getImage();
			final ImageView view = new ImageView(image);
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

			final Animal myAnimal = animal;

			view.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println(event);
					System.out.println(myAnimal);
	                initX = view.getTranslateX();
	                initY = view.getTranslateY();
	                dragAnchor = new Point2D(event.getSceneX(), event.getSceneY());
					event.consume();
				}
			});
			view.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println(event);
					System.out.println(myAnimal);
					double dragX = event.getSceneX() - dragAnchor.getX();
	                double dragY = event.getSceneY() - dragAnchor.getY();
	                //calculate new position of the circle
	                double newXPosition = initX + dragX;
	                double newYPosition = initY + dragY;
	                view.setTranslateX(newXPosition);
	                view.setTranslateY(newYPosition);
					event.consume();
				}
			});
			view.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println(event);
					System.out.println(myAnimal);
					
					// Do rule
					doRule(event, view, myAnimal);
					
					event.consume();
				}
			});
			
			animal.setView(view);

			gridPane.add(view, animal.getX(), animal.getY());
		}
		
		stage.setTitle("Animal Shougi");
		stage.setScene(new Scene(root));
		stage.show();

		Media media = new Media(getClass().getResource("opening.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		
		gridLeft = gridPane.getLayoutX();
		gridTop = gridPane.getLayoutY();
		gridWidth = gridPane.getWidth();
		gridHeight = gridPane.getHeight();
	}

	protected void doRule(MouseEvent event, ImageView view, Animal animal) {
		// TODO Auto-generated method stub
		int gridCol = (int)((event.getSceneX() - gridLeft) / (gridWidth / 3));
		System.out.println((event.getSceneX() - gridLeft) / (gridWidth / 3));
		int gridRow = (int)((event.getSceneY() - gridTop) / (gridHeight / 4));
		System.out.println((event.getSceneY() - gridTop) / (gridHeight / 4));
		
		System.out.println(gridCol);
		System.out.println(gridRow);
		
		animal.setX(gridCol);
		animal.setY(gridRow);
		
		gridPane.getChildren().remove(view);
		view.setTranslateX(initX);
		view.setTranslateY(initY);
		gridPane.add(view, gridCol, gridRow);
	}
}
