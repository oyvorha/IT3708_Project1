import java.util.ArrayList;

public class Chromosome {

    // implement checkValidChromosome

    private ArrayList<Route> routes;
    private int totalDistance;

    public Chromosome() {
        this.routes = new ArrayList<>();
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route){
        if (!this.routes.contains(route)){
            this.routes.add(route);
        }
    }

    public void calculateTotalDistance() {
        int distance = 0;
        for (Route route : this.routes) {
                distance += route.calculateRoute();
            }
        this.totalDistance = distance;
    }
}
