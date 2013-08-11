package jp.gr.java_conf.tkobayas.animalshougi;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
		root.setPrefSize(300, 400);
		
		// Top
		MenuBar bar = new MenuBar();
        Menu gameMenu = new Menu("_Game");
        bar.getMenus().addAll(gameMenu);
        MenuItem restartItem = new MenuItem("_Restart");
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });
        gameMenu.getItems().addAll(restartItem, exitItem);
        root.setTop(bar);
		
		// Center
        GridPane gridPane = new GridPane();
        //gridPane.setPrefSize(400, 300);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        for (Animal animal : defaultPosition) {
        	Image image = animal.getImage();
            ImageView view = new ImageView(image);
            view.setFitWidth(100);
            view.setFitHeight(100);
            view.setPreserveRatio(true);
            gridPane.add(view, animal.getX(), animal.getY());
		}
        
		root.setCenter(gridPane);
		
		stage.setScene(new Scene(root));
		stage.show();
	}
}
