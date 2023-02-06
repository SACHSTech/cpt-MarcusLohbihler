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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private List<Graphdata> objectsLaunched;
    private List<Graphdata> latestObjectsLaunched;
    private List<Graphdata> mostObjectsLaunched;
    private ObservableList<Data> data;
    private HashMap<String, Integer> continentAggregation;
    private HashMap<String, Integer> countryAggregation;
    public Main()throws IOException{
        //contains read data from csv file
        objectsLaunched = new DataCollector().getGraphdata();

        //sorts data by years
        Collections.sort(objectsLaunched, (a, b) -> b.getYear() - a.getYear());

        //returns an array featuring the most recent launch time from each country 
        final Set<String> countriesSeen = new HashSet<>();
        latestObjectsLaunched = objectsLaunched.stream().filter(d -> {
            if (!countriesSeen.contains(d.getCountry())) {
                countriesSeen.add(d.getCountry());
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        //creates list featuring the countries who have made more than 50 launches as of 2021
        mostObjectsLaunched = latestObjectsLaunched.stream().filter(d -> d.getObjects() >= 50).collect(Collectors.toList());

        //gets the total amount of space launches for every continent
        continentAggregation = new HashMap<>();
        for(Graphdata g: latestObjectsLaunched){
            Integer total = continentAggregation.get(g.getContinent());
            if(total == null){
                total = 0;
            }
            total += g.getObjects();
            continentAggregation.put(g.getContinent(), total);
        }

        //gets the total amount of space launches for every country
        countryAggregation = new HashMap<>();
        for(Graphdata g: latestObjectsLaunched){
            Integer total = countryAggregation.get(g.getCountry());
            if(total == null){
                total = 0;
            }
            total += g.getObjects();
            countryAggregation.put(g.getCountry(), total);
        }
     }
 
     /*
      * Creates final product
      */
    public Parent createContent() {
        //data used in drilldown and bar charts
        Data[] continentData = continentAggregation.entrySet().stream().map(e -> new Data(e.getKey(), e.getValue())).toArray(Data[]::new);
        data = FXCollections.observableArrayList(continentData);

        //Creates pie chart that will be projected
        final PieChart pie = new PieChart(data);
        final String drillDownChartCss =
            getClass().getResource("DrilldownChart.css").toExternalForm();
        pie.getStylesheets().add(drillDownChartCss);
        for(Data d: continentData){
            setDrilldownData(pie, d);
        }

         //bar chart axes
        BarChart<String, Number> chart;
        CategoryAxis xAxis;
        NumberAxis yAxis;
        String[] totalObjectsLaunched = {"Total number of objects launched into space per country as of 2021"};
        xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(totalObjectsLaunched));
        yAxis = new NumberAxis("Total Number of Objects Launched into Space", 0.0d, 6000.0d, 100.0d);
        
        //formats data to be placed in bar chart
        ObservableList<Series<String, Number>> barChartData = FXCollections.observableArrayList(
            mostObjectsLaunched.stream().map(gd -> {
                return new Series<String, Number>(gd.getCountry(), FXCollections.observableArrayList(
                    Collections.singletonList(new BarChart.Data<String, Number>(totalObjectsLaunched[0], gd.getObjects()))
                ));
            }).collect(Collectors.toList())
        );

        //creates bar chart that will be projected
        chart = new BarChart<String, Number>(xAxis, yAxis, barChartData, 25.0d);

        //Creates border pane
        BorderPane borderPane = new BorderPane();
    
        //Top content of border pane
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(new Button("Home"));
        toolbar.getItems().add(new Button("Options"));
        toolbar.getItems().add(new Button("Help"));
        borderPane.setTop(toolbar);

        //Left content of border pane
        Button leftButton = new Button("Bar Chart");
        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(leftButton);
        leftButton.setOnMouseClicked((MouseEvent t) -> {
            borderPane.setCenter(chart);
        });
        borderPane.setLeft(leftVbox);

        //Right content of border pane
        Button rightButton = new Button("Drilldown Chart");
        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(rightButton);
        borderPane.setRight(rightVbox);
        rightButton.setOnMouseClicked((MouseEvent t) -> {
            borderPane.setCenter(pie);
        });

        //Center content of border pane
        Label centerLabel = new Label("Center area.");
        centerLabel.setWrapText(true);

        //Using AnchorPane only to position items in the center
        AnchorPane.setTopAnchor(centerLabel, Double.valueOf(5));
        AnchorPane.setLeftAnchor(centerLabel, Double.valueOf(20));
        borderPane.setCenter(pie);

        //Bottom content of border pane
        Label bottomLabel = new Label("Data found at https://ourworldindata.org/space-exploration-satellites");
        Button drilldownReset = new Button("Exit Drilldown");
        VBox exitDrilldown = new VBox();
        exitDrilldown.getChildren().addAll(drilldownReset, bottomLabel);
        drilldownReset.setOnMouseClicked((MouseEvent t) -> {
            pie.setData(data);
        });
        borderPane.setBottom(exitDrilldown);

        return borderPane;

    }
 
    /*
     * creates the drilldown pie chart
     * @param pie The pie chart
     * @param data The data used in the drilldown pie chart
     */
    private void setDrilldownData(final PieChart pie, final Data data) {
        data.getNode().setOnMouseClicked((MouseEvent t) -> {
            pie.setData(FXCollections.observableArrayList(getContinentData(data.getName())));
        });
    }

    /*
     * Filters the data by its continent value so it can be sorted in the pie chart
     * @param continent The name of the continent
     * @return the array with every datapoint that has the same continent value
     */
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
