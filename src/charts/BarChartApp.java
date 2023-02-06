/* ....Show License.... */
package charts;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import basic.Graphdata;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;
 
 
/**
 * A chart that displays rectangular bars with heights indicating data values
 * for categories. Used for displaying information when at least one axis has
 * discontinuous or discrete data.
 */
public class BarChartApp extends Application {
 
    private BarChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
        
    public Parent createContent() {
        String[] totalObjectsLaunched = {"Total as of 2021"};
        xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(totalObjectsLaunched));
        yAxis = new NumberAxis("Total Number of Objects Launched into Space", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData =
            FXCollections.observableArrayList(
              new BarChart.Series("Russia", FXCollections.observableArrayList(
                new BarChart.Data(totalObjectsLaunched[0], 567))),
              new BarChart.Series("United States", FXCollections.observableArrayList(
                new BarChart.Data(totalObjectsLaunched[0], 956))),
              new BarChart.Series("China", FXCollections.observableArrayList(
                new BarChart.Data(totalObjectsLaunched[0], 1154)))
            );
        chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        return chart;
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