import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ReadFromFile {

    private ArrayList<Depot> depotList = new ArrayList<>();
    private ArrayList<Customer> custList = new ArrayList<>();


    public ArrayList<List<Integer>> fileToIntArray(String filepath){
        String strLine = "";
        ArrayList<List<String>> fileArray = new ArrayList<>();
        ArrayList<List<Integer>> intArray = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while (strLine != null) {
                strLine = br.readLine();
                if (strLine == null) {
                    break;
                }
                List<String> strList = Arrays.asList(strLine.trim().split("\\s+"));
                fileArray.add(strList);
            }
            for (int i=0; i<fileArray.size(); i++){
                List<Integer> lineOfIntegers = new ArrayList<>();
                for (int j=0; j<fileArray.get(i).size(); j++){
                    lineOfIntegers.add(Integer.parseInt(fileArray.get(i).get(j)));
                }
                intArray.add(lineOfIntegers);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Unable to read the file.");
        }
        return intArray;
    }

    public void initialize(ArrayList<List<Integer>> intArray){
        int depCount = 0;
        int custCount = 0;
        int carsPerDepot = 0;
        for (int i=0; i<intArray.size(); i++){
            List<Integer> intList = intArray.get(i);
            if (intList.size() == 3){
                //General depot info, 1 line
                carsPerDepot = intList.get(0);
            }
            else if (intList.size() == 2){
                //Specs for each 1...n depot
                //initialize depot and add to depotList
                Depot d = new Depot(depCount, intList.get(0), intList.get(1),
                        carsPerDepot, new Coordinate(0,0));
                depotList.add(d);
            }
            else if (intList.size() == 7){
                //Depot coordinates for each 1...n depot
                Depot d = depotList.get(depCount);
                Coordinate dc = new Coordinate(intList.get(1), intList.get(2));
                d.setCoordinate(dc);
                depCount ++;
            }
            else {
                //Specs for each 1...m customer. Initialize customer per line
                //initialize customers and add to custList
                Coordinate cc = new Coordinate(intList.get(1), intList.get(2));
                int service_dur = intList.get(3);
                int demand = intList.get(4);
                Customer c = new Customer(custCount, service_dur, demand, cc);
                custList.add(c);
                custCount++;
            }
        }
    }
    /*
    public void getFirstSolution(){
        int vehicleCount = 0;
        for each depot in depot list:
            instansier max_no_vehicles antall Vehicles and add as vehicles(field)
    }
    */

    public static void main(String[] args) {
        String filepath = "./Files/DataFiles/p01";
        ReadFromFile rff = new ReadFromFile();
        /*
        for (int i=0; i<rff.fileToIntArray(filepath).size(); i++){
            System.out.println(rff.fileToIntArray(filepath).get(i));
        }
        */
        rff.initialize(rff.fileToIntArray(filepath));
        System.out.println("No of customers: "+rff.custList.size());
        System.out.println("No of depots: "+rff.depotList.size());
        for (int i = 0; i<rff.custList.size(); i++){
            System.out.println(rff.custList.get(0));
        }
        for (int i = 0; i<rff.depotList.size(); i++){
            System.out.println(rff.depotList.get(0));
        }
    }
}