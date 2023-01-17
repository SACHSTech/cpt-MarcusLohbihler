package charts;

public class ObjectsLaunchedGraph extends Application {
 
    private ObservableList<Data> data;
 
    public Parent createContent() {
        Data A, B, C, D;
        data = FXCollections.observableArrayList(A = new Graphdata("Algeria","DZA", "Africa", 2002, 1),
                                                 B = new Data("B", 30),
                                                 C = new Data("C", 10),
                                                 D = new Data("D", 40));
        final PieChart pie = new PieChart(data);
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
