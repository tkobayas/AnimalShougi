package jp.gr.java_conf.tkobayas.animalshougi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Animal;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Chick;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Elephant;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Giraffe;
import jp.gr.java_conf.tkobayas.animalshougi.animal.Lion;

public class AnimalShougi extends Application {
	
	private static final int UNIT_SIZE = 90;
	
	private GamePhase gamePhase;
	
	private GameBoard gameBoard;
	
	// drools
	KieServices ks;
	KieContainer kContainer;
	
	// components
	GridPane gridPane;
	Label statusLabel;
	
	// drag&drop tips
    private double initX;
    private double initY;
	private Point2D dragAnchor;
	private double gridLeft;
	private double gridTop;
	private double gridWidth;
	private double gridHeight;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException {
		
		gamePhase = GamePhase.STARTING;
		
		setupGameBoard();

		setupGameView(stage);
		
		setupDrools();
		
		gamePhase = GamePhase.PLAYER1;
		
		statusLabel.setText(gamePhase.toString());
	}

	private void setupGameBoard() {
		gameBoard = new GameBoard();
		gameBoard.addAnimal(new Chick(1, 1, 2));
		gameBoard.addAnimal(new Elephant(1, 0, 3));
		gameBoard.addAnimal(new Lion(1, 1, 3));
		gameBoard.addAnimal(new Giraffe(1, 2, 3));
		gameBoard.addAnimal(new Chick(2, 1, 1));
		gameBoard.addAnimal(new Elephant(2, 2, 0));
		gameBoard.addAnimal(new Lion(2, 1, 0));
		gameBoard.addAnimal(new Giraffe(2, 0, 0));
	}

	private void setupGameView(Stage stage) throws IOException, URISyntaxException {
		AnchorPane root = FXMLLoader.load(getClass().getResource("GameView.fxml"));

		BorderPane borderPane = ((BorderPane) root.getChildren().get(0));
		gridPane = (GridPane) borderPane.getCenter();
		statusLabel = (Label) borderPane.getBottom();
		
		List<Animal> animals = gameBoard.getAllAnimals();

		for (Animal animal : animals) {
			Image image = animal.getImage();
			final ImageView view = new ImageView(image);
			view.setFitWidth(UNIT_SIZE);
			view.setFitHeight(UNIT_SIZE);
			view.setPreserveRatio(true);

			ColorInput unitColor;
			if (animal.getPlayer() == 1) {
				view.setRotate(0);
				unitColor = new ColorInput(0, 0, UNIT_SIZE, UNIT_SIZE, Color.CYAN);
			} else {
				view.setRotate(180);
				unitColor = new ColorInput(0, 0, UNIT_SIZE, UNIT_SIZE, Color.MAGENTA);
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
	                //calculate new position of the view
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

			gridPane.add(view, animal.getCol(), animal.getRow());
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
	
	private void setupDrools() {
        ks = KieServices.Factory.get();
        kContainer = ks.getKieClasspathContainer();
	}

	protected void doRule(MouseEvent event, ImageView view, Animal animal) {
		int newCol = (int)((event.getSceneX() - gridLeft) / (gridWidth / 3));
		int newRow = (int)((event.getSceneY() - gridTop) / (gridHeight / 4));
		
		List<Action> resultList = new ArrayList<Action>();
		
        KieSession kSession = kContainer.newKieSession();
        kSession.setGlobal("resultList", resultList);
        kSession.insert(gameBoard);
        List<Animal> animals = gameBoard.getAllAnimals();
        for (Animal ani: animals) {
        	kSession.insert(ani);
        }
        kSession.insert(new Action(animal, newCol, newRow));
        kSession.fireAllRules();
		
		gameBoard.update(animal, newCol, newRow);
		
		gridPane.getChildren().remove(view);
		view.setTranslateX(initX);
		view.setTranslateY(initY);
		gridPane.add(view, newCol, newRow);
	}
}
