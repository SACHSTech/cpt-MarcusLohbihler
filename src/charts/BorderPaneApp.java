package charts;
 
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
/**
 * An example of  a BorderPane layout, with placement of children in the top,
 * left, center, right, and bottom positions.
 */
public class BorderPaneApp extends Application {
 
    public Parent createContent() {
         BorderPane borderPane = new BorderPane();
 
        //Top content
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(new Button("Home"));
        toolbar.getItems().add(new Button("Options"));
        toolbar.getItems().add(new Button("Help"));
        borderPane.setTop(toolbar);
 
        //Left content
        Label label1 = new Label("Left hand");
        Button leftButton = new Button("left");
        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(label1, leftButton);
        borderPane.setLeft(leftVbox);
 
        //Right content
        Label rightlabel1 = new Label("Right hand");
        Button rightButton = new Button("right");
 
        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(rightlabel1, rightButton);
        borderPane.setRight(rightVbox);
 
        //Center content
        Label centerLabel = new Label("Center area.");
        centerLabel.setWrapText(true);
        Image ICON_48 = new Image(getClass().getResourceAsStream("icon-48x48.png"));
        ImageView imageView = new ImageView(ICON_48);
 
        //Using AnchorPane only to position items in the center
        AnchorPane centerAP = new AnchorPane();
        AnchorPane.setTopAnchor(centerLabel, Double.valueOf(5));
        AnchorPane.setLeftAnchor(centerLabel, Double.valueOf(20));
        AnchorPane.setTopAnchor(imageView, Double.valueOf(40));
        AnchorPane.setLeftAnchor(imageView, Double.valueOf(30));
        centerAP.getChildren().addAll(centerLabel, imageView);
        borderPane.setCenter(centerAP);
 
        //Bottom content
        Label bottomLabel = new Label("At the bottom.");
        borderPane.setBottom(bottomLabel);
        return borderPane;
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
 
    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }
}
