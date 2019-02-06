import java.util.ArrayList;

public class Route {

    private Vehicle vehicle;
    private ArrayList<Node> nodes;
    private double totalDistance;
    private int currentDemand;

    public Route(Depot initialDepot){
        this.nodes = new ArrayList<>();
        this.totalDistance = 0;
        this.currentDemand = 0;
        this.nodes.add(initialDepot);
        this.nodes.add(initialDepot);
    }

    public void addCustomer(Customer customer) {
        if (!this.nodes.contains(customer)) {
            this.nodes.add(this.nodes.size()-1, customer);
            setTotalDistance();
            this.currentDemand += customer.getDemand();
        }
    }

    public void testAddCustomer(Customer customer) {
        if (!this.nodes.contains(customer)) {
            this.nodes.add(this.nodes.size()-1, customer);
        }
    }

    public void removeCustomer(Customer customer) {
        if (this.nodes.contains(customer)){
            this.nodes.remove(customer);
        }
    }

    public double calculateRoute() {
        int distance = 0;
        Coordinate previousCoordinate = this.nodes.get(0).getCoordinate();
        for (Node node : this.nodes.subList(1, this.nodes.size()-1)) {
            distance += node.getCoordinate().getEuclidianDistance(previousCoordinate);
            previousCoordinate = node.getCoordinate();
        }
        return distance;
    }

    public double getAddedDistance(Customer customer, Depot closestDepot) {
        double oldDistance = this.totalDistance;
        Depot oldDepot = this.getEndDepot();
        testAddCustomer(customer);
        this.setEndDepot(closestDepot);
        double newDistance = this.calculateRoute();
        this.removeCustomer(customer);
        this.setEndDepot(oldDepot);
        return newDistance-oldDistance;
    }

    public boolean checkValidRoute(Customer customer, Depot closestDepot) {
        if ((this.totalDistance+getAddedDistance(customer, closestDepot)) > this.vehicle.getMaxDistance()){
            return false;
        }
        return (this.getCurrentDemand()+customer.getDemand()) < vehicle.getMaxLoad();
    }

    public void removeEndDepot(Depot endDepot) {
        if (this.nodes.contains(endDepot)){
            this.nodes.remove(endDepot);
        }
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

    public double getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setTotalDistance() {
        totalDistance = calculateRoute();
        this.totalDistance = totalDistance;
    }

    public int getCurrentDemand() {
        return currentDemand;
    }
}
