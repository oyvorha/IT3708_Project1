
public class Vehicle {

    private int maxLoad;
    private int vehicleID;
    private int maxDistance;


    Vehicle(int vehicleID, int maxLoad, int maxDistance) {
        this.maxLoad = maxLoad;
        this.vehicleID = vehicleID;
        this.maxDistance = maxDistance;
    }

    @Override
    public String toString() {
        return "Vehicle "+this.getVehicleID();
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getMaxLoad() { return maxLoad; }

    public int getMaxDistance() {
        return maxDistance;
    }
}
