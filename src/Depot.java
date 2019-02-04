import java.util.ArrayList;

public class Depot {

    private Coordinate coordinate;
    private ArrayList<Vehicle> vehicles;
    private int depotID;
    private int maxDuration;
    private int maxLoad;
    private int maxStartingVehiclesAvailable;

    Depot(int depotID, int maxDuration, int maxLoad, int maxVehiclesAvailable, Coordinate coordinate) {
        this.depotID = depotID;
        this.maxDuration = maxDuration;
        this.maxLoad = maxLoad;
        this.maxStartingVehiclesAvailable= maxVehiclesAvailable;
        this.coordinate = coordinate;
        this.vehicles = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Depot with ID "+depotID;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getDepotID() {
        return depotID;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public int getMaxStartingVehiclesAvailable() {
        return maxStartingVehiclesAvailable;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setCoordinate (Coordinate coordinate){
        this.coordinate = coordinate;
    }
}
