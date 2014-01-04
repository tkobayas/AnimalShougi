package jp.gr.java_conf.tkobayas.animalshougi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import jp.gr.java_conf.tkobayas.animalshougi.action.Action;
import jp.gr.java_conf.tkobayas.animalshougi.action.CaptureAction;
import jp.gr.java_conf.tkobayas.animalshougi.action.HenAction;
import jp.gr.java_conf.tkobayas.animalshougi.action.MoveAction;
import jp.gr.java_conf.tkobayas.animalshougi.action.WinAction;
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
	BorderPane borderPane;
	GridPane gridPane;
	Pane leftPane;
	Pane rightPane;
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
		gameBoard.addAnimal(new Chick(Player.PLAYER1, 1, 2));
		gameBoard.addAnimal(new Elephant(Player.PLAYER1, 0, 3));
		gameBoard.addAnimal(new Lion(Player.PLAYER1, 1, 3));
		gameBoard.addAnimal(new Giraffe(Player.PLAYER1, 2, 3));
		gameBoard.addAnimal(new Chick(Player.PLAYER2, 1, 1));
		gameBoard.addAnimal(new Elephant(Player.PLAYER2, 2, 0));
		gameBoard.addAnimal(new Lion(Player.PLAYER2, 1, 0));
		gameBoard.addAnimal(new Giraffe(Player.PLAYER2, 0, 0));
	}

	private void setupGameView(Stage stage) throws IOException, URISyntaxException {
		AnchorPane root = FXMLLoader.load(getClass().getResource("GameView.fxml"));

		borderPane = ((BorderPane) root.getChildren().get(0));
		gridPane = (GridPane) borderPane.getCenter();
		leftPane = (Pane) borderPane.getLeft();
		rightPane = (Pane) borderPane.getRight();
		statusLabel = (Label) borderPane.getBottom();
		
		List<Animal> animals = gameBoard.getAllAnimals();

		for (Animal animal : animals) {
			Image image = animal.getImage();
			final ImageView view = createImageView(animal, image);

			gridPane.add(view, animal.getCol(), animal.getRow());
		}
		
		// to trigger PLAYER2
		borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				System.out.println(event);
				
				if (gamePhase != GamePhase.PLAYER2) {
					event.consume();
					return;
				}
				
				doPlayer2();
				
				event.consume();
			}
		});
		
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

	private ImageView createImageView(Animal animal, Image image) {
		final ImageView view = new ImageView(image);
		view.setFitWidth(UNIT_SIZE);
		view.setFitHeight(UNIT_SIZE);
		view.setPreserveRatio(true);

		ColorInput unitColor;
		if (animal.getPlayer() == Player.PLAYER1) {
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
				
				if (gamePhase != GamePhase.PLAYER1) {
					event.consume();
					return;
				}
				
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
				
				if (gamePhase != GamePhase.PLAYER1) {
					event.consume();
					return;
				}
				
				//System.out.println(event);
				//System.out.println(myAnimal);
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
				
				if (gamePhase != GamePhase.PLAYER1) {
					event.consume();
					return;
				}
				
				System.out.println(event);
				System.out.println(myAnimal);
				
				// Do rule
				doMoveRule(event, view, myAnimal);
				
				event.consume();
			}
		});
		
		animal.setView(view);
		
		return view;
	}
	
	private void setupDrools() {
        ks = KieServices.Factory.get();
        kContainer = ks.getKieClasspathContainer();
        
        // for eagar load
        KieSession kSession = kContainer.newKieSession("ksession_rules_of_game");
        kSession.dispose();
	}

	protected void doMoveRule(MouseEvent event, ImageView view, Animal animal) {
		int newCol = (int)((event.getSceneX() - gridLeft) / (gridWidth / 3));
		int newRow = (int)((event.getSceneY() - gridTop) / (gridHeight / 4));
		
		if (newCol < 0 || newCol > 2 || newRow < 0 || newRow > 3) {
			// invalid
    		view.setTranslateX(initX);
    		view.setTranslateY(initY);
    		return;
		}
		
		MoveAction moveAction = new MoveAction(animal, newCol, newRow);

		Boolean isValid = new Boolean(false);
		List<Action> resultList = new ArrayList<Action>();
		
        KieSession kSession = kContainer.newKieSession("ksession_rules_of_game");

        kSession.setGlobal("isValid", isValid);
        kSession.setGlobal("resultList", resultList);
        
        kSession.insert(gameBoard);
        List<Animal> animals = gameBoard.getAllAnimals();
        for (Animal ani: animals) {
        	kSession.insert(ani);
        }
        kSession.insert(moveAction);
        kSession.fireAllRules();
        
        isValid = (Boolean)kSession.getGlobal("isValid");
        
        kSession.dispose();
        
        // doesn't meet the moving rules
        if (!isValid) {
    		view.setTranslateX(initX);
    		view.setTranslateY(initY);
    		return;
        }
		
        // execute the moving action
        gameBoard.update(moveAction);
        
		gridPane.getChildren().remove(view);
		view.setTranslateX(initX);
		view.setTranslateY(initY);
		gridPane.add(view, newCol, newRow);
        
        // Any result action (win / capture an animal / grow to a hen)
        for (Action resultAction : resultList) {
        	System.out.println(resultAction);
        	if (resultAction instanceof WinAction) {
        		// TODO:
        	} else if (resultAction instanceof CaptureAction) {
        		Animal capturedAnimal = ((CaptureAction)resultAction).getAnimal();
        		spinView(capturedAnimal, moveAction.getAnimal().getPlayer());
        	} else if (resultAction instanceof HenAction) {
        		// TODO:
        	}
		}

		gamePhase = GamePhase.PLAYER2;
		statusLabel.setText(gamePhase.toString());
	}

	private void spinView(final Animal capturedAnimal, final Player toPlayer) {
		final ImageView view = capturedAnimal.getView();
		double toX;
		double toY;
		if (toPlayer == Player.PLAYER1) {
			toX = (gridWidth - view.getLayoutX()) + 30;
			toY = (gridHeight - view.getLayoutY()) - (UNIT_SIZE * (gameBoard.getPlayer1hand().size() +1));
		} else {
			toX = - view.getLayoutX() - 20 - UNIT_SIZE;
			toY = - view.getLayoutY() + (UNIT_SIZE * gameBoard.getPlayer2hand().size());
		}

		System.out.println("toX = " + toX + ", toY = " + toY);
		
		TranslateTransition translateTransition = new TranslateTransition();
		translateTransition.setNode(view);
		translateTransition.setDuration(Duration.millis(0_500L));
		translateTransition.setFromX(view.getTranslateX());
		translateTransition.setFromY(view.getTranslateY());
		translateTransition.setToX(toX);
		translateTransition.setToY(toY);
		translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		gridPane.getChildren().remove(view);
        		gameBoard.captureAnimal(capturedAnimal, toPlayer);
        		ImageView newView = createImageView(capturedAnimal, view.getImage());
        		if (toPlayer == Player.PLAYER1) {
        			newView.setTranslateX(30);
        			newView.setTranslateY(gridHeight - (UNIT_SIZE * gameBoard.getPlayer1hand().size()));
        			rightPane.getChildren().add(newView);
        		} else {
        			newView.setTranslateX(30);
        			newView.setTranslateY(UNIT_SIZE * gameBoard.getPlayer2hand().size());
        			leftPane.getChildren().add(newView);
        		}
            }
        });
		translateTransition.play();
		
		RotateTransition rotateTransition = new RotateTransition();
		rotateTransition.setNode(view);
		rotateTransition.setDuration(Duration.millis(0_500L));
		rotateTransition.setToAngle(720);
		rotateTransition.play();
	}
	
	private void doPlayer2() {
		
		try {Thread.sleep(500);} catch (InterruptedException e) {}
		
		MoveAction moveAction = think();
		doMove(moveAction);
	}

	private MoveAction think() {
        KieSession kSession = kContainer.newKieSession("ksession_algorithm");
        
        kSession.setGlobal("nextMove", null);
        
        kSession.insert(gameBoard);
        List<Animal> animals = gameBoard.getAllAnimals();
        for (Animal ani: animals) {
        	kSession.insert(ani);
        }
        kSession.fireAllRules();
        
        MoveAction nextMove = (MoveAction)kSession.getGlobal("nextMove");
        
        kSession.dispose();
        
        return nextMove;
	}
	
	private void doMove(MoveAction moveAction) {
		Boolean isValid = new Boolean(false);
		List<Action> resultList = new ArrayList<Action>();
		
        KieSession kSession = kContainer.newKieSession("ksession_rules_of_game");

        kSession.setGlobal("isValid", isValid);
        kSession.setGlobal("resultList", resultList);
        
        kSession.insert(gameBoard);
        List<Animal> animals = gameBoard.getAllAnimals();
        for (Animal ani: animals) {
        	kSession.insert(ani);
        }
        kSession.insert(moveAction);
        kSession.fireAllRules();
        
        isValid = (Boolean)kSession.getGlobal("isValid");
        
        kSession.dispose();
        
        // doesn't meet the moving rules
        if (!isValid) {
    		throw new RuntimeException("PLAYER2 should not choose an invalid move");
        }
		
        // execute the moving action
        gameBoard.update(moveAction);
        
        ImageView view = moveAction.getAnimal().getView();
        
		gridPane.getChildren().remove(view);
		gridPane.add(view, moveAction.getNewCol(), moveAction.getNewRow());
        
        // Any result action (win / capture an animal / grow to a hen)
        for (Action resultAction : resultList) {
        	System.out.println(resultAction);
        	if (resultAction instanceof WinAction) {
        		// TODO:
        	} else if (resultAction instanceof CaptureAction) {
        		Animal capturedAnimal = ((CaptureAction)resultAction).getAnimal();
        		spinView(capturedAnimal, moveAction.getAnimal().getPlayer());
        	} else if (resultAction instanceof HenAction) {
        		// TODO:
        	}
		}

		gamePhase = GamePhase.PLAYER1;
		statusLabel.setText(gamePhase.toString());
	}
}
