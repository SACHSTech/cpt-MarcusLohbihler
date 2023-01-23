package charts;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.input.MouseEvent;
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

        // List<Graphdata> latestObjectLaunched = 


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
        // System.out.println(countryAggregation);
        // System.out.println(continentAggregation);
    }
 
    public Parent createContent() {
        Data[] continentData = continentAggregation.entrySet().stream().map(e -> new Data(e.getKey(), e.getValue())).toArray(Data[]::new);
        data = FXCollections.observableArrayList(continentData);
        final PieChart pie = new PieChart(data);
        final String drillDownChartCss =
            getClass().getResource("DrilldownChart.css").toExternalForm();
        pie.getStylesheets().add(drillDownChartCss);
        return pie;
    }
 
    private void setDrilldownData(final PieChart pie, final Data data,
                                  final String labelPrefix) {
        data.getNode().setOnMouseClicked((MouseEvent t) -> {
            Data[] countrydata = countryAggregation.entrySet().stream()
               .map(e -> {
                    return new Data(e.getKey(), e.getValue()); 
            }).toArray(Data[]::new);
        ;
        });

    }

    // private 

    // private Data[] getContinentData(final String continent) {
    //     objectsLaunched.stream()
    //         .filter(e -> e.getContinent().equals(continent))
    //         .map(e -> new Data(e.getCountry(), e.getObjects()))
    //         .toArray(Data[]::new);
    // }
 
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
