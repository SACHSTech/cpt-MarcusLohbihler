package charts;
 
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
/**
 * This sample demonstrates styling toggle buttons with CSS.
 */
public class PillButtonApp extends Application {
 
    public Parent createContent() {
        // create 3 toggle buttons and a toogle group for them
        ToggleButton tb1 = new ToggleButton("DrillDown Pie Chart");
        tb1.setPrefSize(76, 45);
        tb1.getStyleClass().add("left-pill");
        ToggleButton tb2 = new ToggleButton("No Chart");
        tb2.setPrefSize(76, 45);
        tb2.getStyleClass().add("center-pill");
        ToggleButton tb3 = new ToggleButton("Bar Chart");
        tb3.setPrefSize(76, 45);
        tb3.getStyleClass().add("right-pill");
 
        final ToggleGroup group = new ToggleGroup();
        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
        tb3.setToggleGroup(group);
        // select the first button to start with
        group.selectToggle(tb2);
 
        // enforce rule that one of the ToggleButtons must be selected at any
        // time (that is, it is not valid to have zero ToggleButtons selected).
        // (Fix for RT-34920 that considered this to be a bug)
        final ChangeListener<Toggle> listener =
            (ObservableValue<? extends Toggle> observable,
             Toggle old, Toggle now) -> {
                if (now == null) {
                    group.selectToggle(old);
                }
            };
        group.selectedToggleProperty().addListener(listener);
 
        final String pillButtonCss =
            getClass().getResource("PillButton.css").toExternalForm();
        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(tb1, tb2, tb3);
        hBox.getStylesheets().add(pillButtonCss);
 
        return hBox;
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