import java.util.ArrayList;

public class Chromosome implements Comparable<Chromosome> {

    // implement checkValidChromosome

    private ArrayList<Route> routes;
    private int totalDistance;
    private ArrayList<Customer> restCustomers;

    public Chromosome() {
        this.routes = new ArrayList<>();
        this.restCustomers = new ArrayList<>();
        this.calculateTotalDistance();
    }

    public Chromosome(Chromosome chromosome) {
        this.routes = this.makeNewRoutes(chromosome.getRoutes());
        this.restCustomers = chromosome.getRestCustomers();
        this.totalDistance = chromosome.getTotalDistance();
    }

    public ArrayList<Route> makeNewRoutes(ArrayList<Route> routes) {
        ArrayList<Route> newRoutes = new ArrayList<>();
        for (Route r : routes) {
            Route newRoute = new Route(r);
            newRoutes.add(newRoute);
        }
        return newRoutes;
    }

    @Override
    public String toString() {
        this.calculateTotalDistance();
        return ""+this.getTotalDistance();
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route){
        if (!this.routes.contains(route)){
            this.routes.add(route);
            this.calculateTotalDistance();
        }
    }

    public void calculateTotalDistance() {
        int distance = 0;
        for (Route route : this.routes) {
                distance += route.getTotalDistance();
            }
        this.totalDistance = distance;
    }

    public ArrayList<Customer> getRestCust() {
        return restCustomers;
    }

    public void addRestCustomer(Customer customer) {
        this.restCustomers.add(customer);
    }

    public ArrayList<Customer> getRestCustomers(){
        return this.restCustomers;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int compareTo(Chromosome o1) {
        return (this.totalDistance - o1.totalDistance);
    }

    public void perfectSwap() {
        for (Route r : this.getRoutes()) {
            if (r.getNodes().size() > 4) {
                for (int i = 1; i < r.getNodes().size() - 1; i++) {
                    for (int j = r.getNodes().size() - 2; j > 0; j--) {
                        r.swapCustomers(i, j);
                    }
                }
            }
        }
        this.calculateTotalDistance();
    }

    public ArrayList<Depot> getChromosomeDepots (){
        ArrayList<Depot> chromosomeDepots = new ArrayList<>();
        for (Route route : this.routes){
            Depot startDepot = route.getStartDepot();
            Depot endDepot = route.getEndDepot();
            if (!chromosomeDepots.contains(startDepot)){
                chromosomeDepots.add(startDepot);
            }
            if (!chromosomeDepots.contains(endDepot)){
                chromosomeDepots.add(endDepot);
            }
        }
        return chromosomeDepots;
    }
}
