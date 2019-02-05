
public class Vehicle {

    private int maxLoad;
    private int vehicleID;
    private int maxDuration;


    Vehicle(int vehicleID, int maxLoad, int maxDuration) {
        this.maxLoad = maxLoad;
        this.vehicleID = vehicleID;
        this.maxDuration = maxDuration;
    }

    @Override
    public String toString() {
        return "Vehicle "+this.getVehicleID();
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getMaxLoad() { return maxLoad; }

}
