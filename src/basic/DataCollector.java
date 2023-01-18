package basic;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class DataCollector {
    private ArrayList<Graphdata> dataList; 
    public DataCollector (ArrayList<Graphdata> theDataList){
        dataList = new ArrayList<Graphdata>();
        String fileName = "C:\Users\marcu\Downloads\cumulative-number-of-objects-launched-into-outer-space.xlsx";
        FileReader input = new FileReader(fileName);
    }
    

}
