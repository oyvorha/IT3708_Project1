public class Depot {

    private Coordinate coordinate;
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
}
