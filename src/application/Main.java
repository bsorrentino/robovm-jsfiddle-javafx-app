package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		startWithFXML(primaryStage);
		//startByCode(primaryStage);
	}



	public void startWithFXML(Stage primaryStage) throws IOException {
		
			java.net.URL url = getClass().getClassLoader().getResource("application/MainScene.fxml");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(url);
			loader.setBuilderFactory( new JavaFXBuilderFactory());
			Parent root = (Parent) loader.load( url.openStream() );

			MainController controller = loader.getController();
			
	        Scene scene = new Scene(root, 300, 275);
	    
	        primaryStage.setTitle("FXML Welcome");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	 		
	}
	
	
	public void startByCode(Stage primaryStage) {
		VBox rootNode = new VBox(10);
        rootNode.setStyle("-fx-padding: 20px");

        Label label = new Label("Enter your name");
        rootNode.getChildren().add(label);

        final TextField field = new TextField();
        rootNode.getChildren().add(field);

        final Label outputLabel = new Label();
        rootNode.getChildren().add(outputLabel);

        Button button = new Button("Say Hello");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                outputLabel.setText("Hello " + field.getText());
            }
        });
        rootNode.getChildren().add(button);


        Scene scene = new Scene(rootNode, 400, 200);
        //scene.getStylesheets().add("/styles/styles.css");

        primaryStage.setTitle("RoboVM and JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();		
        
        /*
        try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
