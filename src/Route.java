import java.util.ArrayList;

public class Route {

    private Vehicle vehicle;
    private ArrayList<Node> nodes;
    private int totalDistance;

    public Route(){
        this.nodes = new ArrayList<>();
        this.totalDistance = 0;
    }

    public void addCustomer(Customer customer) {
        if (!this.nodes.contains(customer)) {
            this.nodes.add(customer);
        }
    }

    public void removeCustomer(Customer customer) {
        if (this.nodes.contains(customer)){
            this.nodes.remove(customer);
        }
    }

    public int calculateRoute() {
        int distance = 0;
        Coordinate previousCoordinate = this.nodes.get(0).getCoordinate();
        for (Node node : this.nodes.subList(1, this.nodes.size())) {
            distance += node.getCoordinate().getEuclidianDistance(previousCoordinate);
            previousCoordinate = node.getCoordinate();
        }
        this.totalDistance = distance;
        return distance;
    }

    public float getAddedDistance(Customer customer) {
        return 0;
    }

    public boolean checkValidRoute() {
        if (this.totalDistance > this.vehicle.getMaxDistance()){
            return false;
        }
        return this.getTotalDemand() > vehicle.getMaxLoad();
    }

    private int getTotalDemand() {
        int demand = 0;
        for (Node node : this.nodes) {
            if (node instanceof Customer) {
                Customer customer = (Customer) node;
                demand += customer.getDemand();
            }
        }
        return demand;
    }

    public void setStartDepot(Depot startDepot) {
        this.nodes.set(0, startDepot);
    }

    public void setEndDepot(Depot endDepot) {
        this.nodes.set(nodes.size()-1, endDepot);
    }

    public Depot getEndDepot() {
        return (Depot) this.nodes.get(nodes.size()-1);
    }

    public Depot getStartDepot() {
        return (Depot) this.nodes.get(0);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
