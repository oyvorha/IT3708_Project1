import java.util.ArrayList;
import java.util.Collections;

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

    public Route(Route route){
        this.nodes = new ArrayList<>();
        for (Node node : route.getNodes()){
            this.nodes.add(node);
        }
        this.totalDistance = route.getTotalDistance();
        this.currentDemand = route.getCurrentDemand();
        this.vehicle = route.getVehicle();
        this.setStartDepot(route.getStartDepot());
        this.setEndDepot(route.getEndDepot());
    }

    public void addCustomer(Customer customer, int index) {
        if (!this.nodes.contains(customer)) {
            this.nodes.add(index, customer);
            this.setTotalDistance();
            this.currentDemand += customer.getDemand();
        }
    }

    public void removeCustomer(Customer customer) {
        if (this.nodes.contains(customer)){
            this.nodes.remove(customer);
            this.setTotalDistance();
            this.currentDemand = this.getCurrentDemand() - customer.getDemand();
        }
    }

    public double calculateRoute() {
        int distance = 0;
        Coordinate previousCoordinate = this.nodes.get(0).getCoordinate();
        for (Node node : this.nodes.subList(1, this.nodes.size())) {
            distance += node.getCoordinate().getEuclidianDistance(previousCoordinate);
            previousCoordinate = node.getCoordinate();
        }
        return distance;
    }

    public double getAddedDistance(Customer customer, Depot closestDepot, int index) {
        double oldDistance = this.totalDistance;
        Depot oldDepot = this.getEndDepot();
        this.addCustomer(customer, index);
        this.setEndDepot(closestDepot);
        double newDistance = this.getTotalDistance();
        this.removeCustomer(customer);
        this.setEndDepot(oldDepot);
        return newDistance-oldDistance;
    }

    public boolean checkValidRoute(Customer customer, Depot closestDepot, int index) {
        if ((this.totalDistance+getAddedDistance(customer, closestDepot, index)) > this.vehicle.getMaxDistance()){
            return false;
        }
        return ((this.getCurrentDemand()+customer.getDemand()) <= vehicle.getMaxLoad());
    }

    public boolean checkValid() {
        if (this.totalDistance > this.vehicle.getMaxDistance()) {
            return false;
        } return (this.getCurrentDemand() <= this.vehicle.getMaxLoad());
    }

    public void swapCustomers(int index1, int index2) {
        // double oldDistance = this.getTotalDistance();
        Collections.swap(this.nodes, index1, index2);
        this.setTotalDistance();
        if (!this.checkValid()) {
            Collections.swap(this.nodes, index1, index2);
            this.setTotalDistance();
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

    public void resetRoute(){
        ArrayList<Node> removableNodes = new ArrayList<>();
        for (Node node : this.getNodes()){
            if (node instanceof Customer){
                removableNodes.add(node);
            }
        }
        for (Node node : removableNodes){
            this.removeCustomer((Customer) node);
        }
        this.setEndDepot(this.getStartDepot());
        this.setTotalDistance();
    }

    public int getCurrentDemand() {
        return currentDemand;
    }
}
