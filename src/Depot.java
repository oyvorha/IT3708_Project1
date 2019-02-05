import java.util.ArrayList;

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

    @Override
    public String toString() {
        return "Depot "+depotID+"  x:"+this.getCoordinate().getX()+
                "   y:"+this.getCoordinate().getY();
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

    public void setCoordinate (Coordinate coordinate){
        this.coordinate = coordinate;
    }
}
