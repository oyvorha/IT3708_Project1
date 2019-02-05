import java.util.ArrayList;

public class Route {

    private Vehicle vehicle;
    private ArrayList<Customer> customers;
    private Depot startDepot;
    private Depot endDepot;
    private int distance;

    public Route(){
        this.customers = new ArrayList<>();
        this.distance = 0;
    }

    public void addCustomer(Customer customer) {
        if (!this.customers.contains(customer)) {
            this.customers.add(customer);
        }
    }

    public void removeCustomer(Customer customer) {
        if (this.customers.contains(customer)){
            this.customers.remove(customer);
        }
    }

    public int calculateRoute() {
        int distance = 0;
        Coordinate previousCoordinate = this.startDepot.getCoordinate();
        for (Customer c : this.customers) {
            distance += c.getCoordinate().getEuclidianDistance(previousCoordinate);
            previousCoordinate = c.getCoordinate();
        }
        this.distance = distance;
        return distance;
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

}
