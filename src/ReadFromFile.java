import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

public class ReadFromFile {

    private ArrayList<Depot> depots;
    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;
    private HashMap<String, Double> benchmarks;

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
        this.benchmarks = new HashMap<>();
        this.fillBenchmark();
        this.initialize(this.fileToIntArray());
    }

    public void fillBenchmark() {
        benchmarks.put("p01", 570.53);
        benchmarks.put("p02", 462.94);
        benchmarks.put("p03", 641.19);
        benchmarks.put("p04", 1013.26);
        benchmarks.put("p05", 753.07);
        benchmarks.put("p06", 882.29);
        benchmarks.put("p07", 896.11);
        benchmarks.put("p08", 4528.74);
        benchmarks.put("p09", 3969.94);
        benchmarks.put("p10", 3786.58);
        benchmarks.put("p11", 3690.5);
        benchmarks.put("p12", 1304.69);
        benchmarks.put("p13", 1318.95);
        benchmarks.put("p14", 1285.69);
        benchmarks.put("p15", 2511.92);
        benchmarks.put("p16", 2572.23);
        benchmarks.put("p17", 2608.84);
        benchmarks.put("p18", 3795.98);
        benchmarks.put("p19", 3839.36);
        benchmarks.put("p20", 4005.92);
        benchmarks.put("p21", 5640.88);
        benchmarks.put("p22", 5751.57);
        benchmarks.put("p23", 6098.21);
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
        this.adjustCoordinates(this.getMinimalX(), this.getMinimalY());
    }

    public double getBenchmark(String file) {
        return this.benchmarks.get(file);
    }

    public int getMinimalX() {
        int minimalX = 0;
        for (Customer customer : this.customers) {
            int newX = customer.getCoordinate().getX();
            if (newX < minimalX) {
                minimalX = newX;
            }
        }
        for (Depot depot : this.depots) {
            int newDepotX = depot.getCoordinate().getX();
            if (newDepotX < minimalX) {
                minimalX = newDepotX;
            }
        }
        return minimalX;
    }

    public int getMinimalY(){
        int minimalY = 0;
        for (Customer customer : this.customers) {
            int newY = customer.getCoordinate().getY();
            if (newY < minimalY) {
                minimalY = newY;
            }
        }
        for (Depot depot : this.depots) {
            int newDepotY = depot.getCoordinate().getY();
            if (newDepotY < minimalY) {
                minimalY = newDepotY;
            }
        }
        return minimalY;
    }

    public void adjustCoordinates(int minimalX, int minimalY){
        for (Customer customer : this.customers){
            Coordinate customerCoordinate = customer.getCoordinate();
            int oldCustomerX = customerCoordinate.getX();
            customer.getCoordinate().setX(oldCustomerX -= minimalX);
            int oldCustomerY = customerCoordinate.getY();
            customer.getCoordinate().setY(oldCustomerY -= minimalY);
        }
        for (Depot depot : this.depots){
            Coordinate depotCoordinate = depot.getCoordinate();
            int oldDepotX = depotCoordinate.getX();
            depot.getCoordinate().setX(oldDepotX -= minimalX);
            int oldDepotY = depot.getCoordinate().getY();
            depot.getCoordinate().setY(oldDepotY-= minimalY);
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

}