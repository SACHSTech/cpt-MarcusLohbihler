package basic;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataCollector {
    private ArrayList<Graphdata> dataList; 
    public DataCollector (ArrayList<Graphdata> theDataList)throws FileNotFoundException{
        dataList = new ArrayList<Graphdata>();
        String fileName = "c:/Users/marcu/cpt-MarcusLohbihler/Resources/cumulative-number-of-objects-launched-into-outer-space - cumulative-number-of-objects-launched-into-outer-space.csv";
        try (FileReader input = new FileReader(fileName)){
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    

}
