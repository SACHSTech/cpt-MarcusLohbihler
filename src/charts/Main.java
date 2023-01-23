package charts;
import basic.DataCollector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {
 
    private DataCollector data;
 
    public Parent createContent() {
        data = new DataCollector(objectsLaunched);
        
        final PieChart pie = new PieChart((ObservableList<Data>) data);
        final String drillDownChartCss =
            getClass().getResource("DrilldownChart.css").toExternalForm();
        pie.getStylesheets().add(drillDownChartCss);
 
        setDrilldownData(pie, A, "a");
        setDrilldownData(pie, B, "b");
        setDrilldownData(pie, C, "c");
        setDrilldownData(pie, D, "d");
        return pie;
    }
 
    private void setDrilldownData(final PieChart pie, final Data data,
                                  final String labelPrefix) {
        data.getNode().setOnMouseClicked((MouseEvent t) -> {
            pie.setData(FXCollections.observableArrayList(
                    new Data(labelPrefix + "-1", 7),
                    new Data(labelPrefix + "-2", 2),
                    new Data(labelPrefix + "-3", 5),
                    new Data(labelPrefix + "-4", 3),
                    new Data(labelPrefix + "-5", 2)));
        });
    }
 
    @Override public void start(Stage primaryStage) throws Exception {
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
