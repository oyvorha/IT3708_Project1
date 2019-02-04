import java.util.ArrayList;

public class Vehicle {

    private int currentLoad;
    private int vehicleID;
    private Depot startDepot;
    private Depot endDepot;
    private ArrayList<Customer> customers;

    Vehicle(int vehicleID) {
        this.currentLoad = 0;
        this.vehicleID = vehicleID;
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if (!customer.visited() && !customers.contains(customer)) {
            customers.add(customer);
            customer.setVisitingVehicle(this);
        }
    }

    public int calculateRoute() {
        int distance = 0;
        Coordinate previousCoordinate = startDepot.getCoordinate();
        for (Customer c : customers) {
            distance += c.getCoordinate().getEuclidianDistance(previousCoordinate);
            previousCoordinate = c.getCoordinate();
        }
        return distance;
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
