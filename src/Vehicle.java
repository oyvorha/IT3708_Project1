
public class Vehicle {

    private int maxLoad;
    private int vehicleID;
    private int maxDistance;


    Vehicle(int vehicleID, int maxDistance, int maxLoad) {
        this.vehicleID = vehicleID;
        if (maxDistance == 0){
            this.maxDistance = 9999999;
        } else {
            this.maxDistance = maxDistance;
        }
        if (maxLoad == 0){
            this.maxLoad = 9999999;
        } else {
            this.maxLoad = maxLoad;
        }
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
