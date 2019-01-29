public class Vehicle {

    private int currentLoad;
    private int vehicleID;
    private Depot startDepot;
    private Depot endDepot;

    Vehicle(int vehicleID) {
        this.currentLoad = 0;
        this.vehicleID = vehicleID;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public void setStartDepot(Depot startDepot) {
        this.startDepot = startDepot;
    }

    public void setEndDepot(Depot endDepot) {
        this.endDepot = endDepot;
    }

    public Depot getEndDepot() {
        return endDepot;
    }

    public Depot getStartDepot() {
        return startDepot;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public int getVehicleID() {
        return vehicleID;
    }
}
