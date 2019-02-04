import java.util.ArrayList;

public class Chromosome {

    private ArrayList<Depot> depots;
    private int totalDistance;

    public Chromosome() {
        this.depots = new ArrayList<>();
    }

    public ArrayList<Depot> getDepots() {
        return depots;
    }

    public void calculateTotalDistance() {
        int distance = 0;
        for (Depot depot : depots) {
            for (Vehicle vehicle : depot.getVehicles()) {
                distance += vehicle.calculateRoute();
            }
        }
        this.totalDistance = distance;
    }
}
