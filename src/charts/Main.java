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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
        //toggle button for switching between charts
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
        group.selectToggle(tb1);

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

    //Pie chart
    final PieChart pie = new PieChart(data);
    final String drillDownChartCss =
        getClass().getResource("DrilldownChart.css").toExternalForm();
    pie.getStylesheets().add(drillDownChartCss);
    for(Data d: continentData){
        setDrilldownData(pie, d);
    }
    
    //bar chart
    // BarChart chart;
    // CategoryAxis xAxis;
    // NumberAxis yAxis;
    // String[] totalObjectsLaunched = {""};
    //     xAxis = new CategoryAxis();
    //     xAxis.setCategories(FXCollections.<String>observableArrayList(totalObjectsLaunched));
    //     yAxis = new NumberAxis("Units Sold", 0.0d, 3000.0d, 1000.0d);
    //     ObservableList<BarChart.Series> barChartData =
    //         FXCollections.observableArrayList(
    //           new BarChart.Series("Russia", FXCollections.observableArrayList(
    //             new BarChart.Data(totalObjectsLaunched[0], 567),
    //             new BarChart.Data(totalObjectsLaunched[1], 1292))),
    //           new BarChart.Series("United States", FXCollections.observableArrayList(
    //             new BarChart.Data(totalObjectsLaunched[0], 956),
    //             new BarChart.Data(totalObjectsLaunched[1], 1665))),
    //           new BarChart.Series("China", FXCollections.observableArrayList(
    //             new BarChart.Data(totalObjectsLaunched[0], 1154),
    //             new BarChart.Data(totalObjectsLaunched[1], 1927)))
    //         );
    //     chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        // Check value of buttons
        int chartSelected = 1;
        if(tb1.isSelected()){
            chartSelected = 0;  
        }else if(tb2.isSelected()){
            chartSelected = 1;
        }else if(tb3.isSelected()){
            chartSelected = 2;
        }

        // if(chartSelected == 0){
        //     return pie;  
        // }else if(chartSelected == 1){
        //     return hBox;
        // }else if(chartSelected == 2){
        //     // return chart;
        // }

        if(chartSelected == 0){
            return pie;  
        }else{
            return hBox;
        }
        
    }
 
    private void setDrilldownData(final PieChart pie, final Data data) {
        data.getNode().setOnMouseClicked((MouseEvent t) -> {
            System.out.println("Mouse Clicked");
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
