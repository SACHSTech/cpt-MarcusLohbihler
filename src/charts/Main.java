package charts;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import basic.DataCollector;
import basic.Graphdata;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private List<Graphdata> objectsLaunched;
    private List<Graphdata> latestObjectsLaunched;
    private ObservableList<Data> data;
    private HashMap<String, Integer> continentAggregation;
    private HashMap<String, Integer> countryAggregation;
    public Main()throws IOException{
        objectsLaunched = new DataCollector().getGraphdata();
        Collections.sort(objectsLaunched, (a, b) -> b.getYear() - a.getYear());

        final Set<String> countriesSeen = new HashSet<>();
        latestObjectsLaunched = objectsLaunched.stream().filter(d -> {
            if (!countriesSeen.contains(d.getCountry())) {
                countriesSeen.add(d.getCountry());
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        continentAggregation = new HashMap<>();
        for(Graphdata g: latestObjectsLaunched){
            Integer total = continentAggregation.get(g.getContinent());
            if(total == null){
                total = 0;
            }
            total += g.getObjects();
            continentAggregation.put(g.getContinent(), total);
        }

        countryAggregation = new HashMap<>();
        for(Graphdata g: latestObjectsLaunched){
            Integer total = countryAggregation.get(g.getCountry());
            if(total == null){
                total = 0;
            }
            total += g.getObjects();
            countryAggregation.put(g.getCountry(), total);
        }
         System.out.println(countryAggregation);
         System.out.println(continentAggregation);
    }
 
    public Parent createContent() {
        //data for drilldown and bar charts
        Data[] continentData = continentAggregation.entrySet().stream().map(e -> new Data(e.getKey(), e.getValue())).toArray(Data[]::new);
        Data[] countryData = countryAggregation.entrySet().stream().map(e -> new Data(e.getKey(), e.getValue())).toArray(Data[]::new);
        data = FXCollections.observableArrayList(continentData);

        //Pie chart
        final PieChart pie = new PieChart(data);
        final String drillDownChartCss =
            getClass().getResource("DrilldownChart.css").toExternalForm();
        pie.getStylesheets().add(drillDownChartCss);
        for(Data d: continentData){
            setDrilldownData(pie, d);
        }

         //bar chart
        BarChart chart;
        CategoryAxis xAxis;
        NumberAxis yAxis;
        String[] totalObjectsLaunched = {""};
        xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(totalObjectsLaunched));
        yAxis = new NumberAxis("Units Sold", 0.0d, 3000.0d, 1000.0d);
        ObservableList<Data> barChartData =
            FXCollections.observableArrayList(
            );
        chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);       

        //Stack Panes
        StackPane stackPaneDrilldown = new StackPane();
        StackPane stackPaneBar = new StackPane();
 
        stackPaneDrilldown.getChildren().add(pie);
        stackPaneBar.getChildren().add(chart);

        //Border Pane
        BorderPane borderPane = new BorderPane();
    
        //Top content
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(new Button("Home"));
        toolbar.getItems().add(new Button("Options"));
        toolbar.getItems().add(new Button("Help"));
        borderPane.setTop(toolbar);

        //Left content
        Label label1 = new Label("Left hand");
        Button leftButton = new Button("Bar Chart");
        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(label1, leftButton);
        borderPane.setLeft(leftVbox);

        //Right content
        Label rightlabel1 = new Label("Right hand");
        Button rightButton = new Button("Drilldown Chart");

        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(rightlabel1, rightButton);
        borderPane.setRight(rightVbox);

        //Center content
        Label centerLabel = new Label("Center area.");
        centerLabel.setWrapText(true);

        //Using AnchorPane only to position items in the center
        AnchorPane.setTopAnchor(centerLabel, Double.valueOf(5));
        AnchorPane.setLeftAnchor(centerLabel, Double.valueOf(20));
        borderPane.setCenter(stackPaneDrilldown);

        //Bottom content
        Label bottomLabel = new Label("At the bottom.");
        borderPane.setBottom(bottomLabel);
        return borderPane;
    }
 
    private void setDrilldownData(final PieChart pie, final Data data) {
        data.getNode().setOnMouseClicked((MouseEvent t) -> {
            pie.setData(FXCollections.observableArrayList(getContinentData(data.getName())));
        });
    }

    private Data[] getContinentData(final String continent) {
        return latestObjectsLaunched.stream()
            .filter(e -> e.getContinent().equals(continent))
            .map(e -> new Data(e.getCountry(), e.getObjects()))
            .toArray(Data[]::new);
    }
 
    @Override public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
 
    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
