public class Depot {

    private Coordinate cor;
    private int depotID;
    private int maxDuration;
    private int maxLoad;
    private int maxVehiclesAvailable;

    Depot(int depotID, int maxDuration, int maxLoad, int maxVehiclesAvailable) {
        this.depotID = depotID;
        this.maxDuration = maxDuration;
        this.maxLoad = maxLoad;
        this.maxVehiclesAvailable = maxVehiclesAvailable;
    }

}
