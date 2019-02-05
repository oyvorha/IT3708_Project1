import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ReadFromFile {

    private ArrayList<Depot> depots;
    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;

    private String filepath;
    private int depotCount;
    private int vehicleCount;
    private int depotCountForCoordinate;
    private int vehiclesPerDepot;

    public ReadFromFile(String filepath){
        this.depotCount = 0;
        this.vehicleCount = 1;
        this.depotCountForCoordinate = 0;
        this.vehiclesPerDepot = 0;
        this.filepath = filepath;
        this.depots = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.vehicles= new ArrayList<>();
        this.initialize(this.fileToIntArray());
    }

    public ArrayList<List<Integer>> fileToIntArray(){
        String strLine = "";
        ArrayList<List<String>> fileArray = new ArrayList<>();
        ArrayList<List<Integer>> intArray = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.filepath));
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

        for (int i=0; i<intArray.size(); i++){
            List<Integer> intList = intArray.get(i);
            if (intList.size() == 3){
                //General depot info, one line
                this.vehiclesPerDepot = intList.get(0);
            }
            else if (intList.size() == 2){
                //Specs for each 1...n depot
                //initialize depot and add to depots
                Depot d = new Depot(this.depotCount+1, intList.get(0), intList.get(1),
                        this.vehiclesPerDepot, new Coordinate(0,0));
                this.depots.add(d);
                this.depotCount ++;
                //initialize vehiclesPerDepot number of vehicles per depot
                for (int j = 0; j<this.vehiclesPerDepot; j++){
                    this.vehicles.add(new Vehicle(this.vehicleCount, intList.get(0), intList.get(1)));
                    this.vehicleCount++;
                }
            }
            else if (intList.size() == 7){
                //Depot coordinates for each 1...n depot
                Depot d = this.depots.get(this.depotCountForCoordinate);
                Coordinate dc = new Coordinate(intList.get(1), intList.get(2));
                d.setCoordinate(dc);
                this.depotCountForCoordinate++;
            }
            else {
                //Specs for each 1...m customer. Initialize one customer per line
                Coordinate cc = new Coordinate(intList.get(1), intList.get(2));
                int serviceDuration = intList.get(3);
                int demand = intList.get(4);
                Customer c = new Customer(intList.get(0), serviceDuration, demand, cc);
                this.customers.add(c);
            }
        }
    }

    public ArrayList<Depot> getDepots(){
        return this.depots;
    }

    public ArrayList<Customer> getCustomers(){
        return this.customers;
    }

    public ArrayList<Vehicle> getVehicles(){
        return this.vehicles;
    }

    public int getVehiclesPerDepot(){
        return this.vehiclesPerDepot;
    }

    public static void main(String[] args) {
        String filepath = "./Files/DataFiles/p01";
        ReadFromFile readFromFile = new ReadFromFile(filepath);
        System.out.println("No of customers: "+readFromFile.customers.size());
        System.out.println("No of depots: "+readFromFile.depots.size());
    }
}