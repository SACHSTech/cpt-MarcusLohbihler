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
        //String fileName = "c:/Users/marcu/cpt-MarcusLohbihler/Resources/cumulative-number-of-objects-launched-into-outer-space - cumulative-number-of-objects-launched-into-outer-space.csv";
        String fileName = "Z:/Marcus/cpt-MarcusLohbihler/Resources/cumulative-number-of-objects-launched-into-outer-space - cumulative-number-of-objects-launched-into-outer-space.csv";
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = input.readLine()) != null){
                String[] parts = line.split(",");
                Graphdata gd = new Graphdata(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4]);
                dataList.add(gd);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Graphdata> getGraphdata(){
        return dataList;
    }
}
