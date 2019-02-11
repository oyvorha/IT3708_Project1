import java.util.ArrayList;
import java.util.Collections;

public class Model {

    private ReadFromFile readFromFile;
    private ArrayList<Chromosome> chromosomes;

    public Model(ReadFromFile readFromFile){
        this.chromosomes = new ArrayList<>();
        this.readFromFile = readFromFile;
    }

    public void getFirstSolutions(int numberOfSolutions){
        for (int i = 0; i<numberOfSolutions; i++){
            Chromosome chromosome = getEmptyChromosome();
            Collections.shuffle(this.readFromFile.getCustomers());
            this.getSolution(chromosome);
        }
    }

    public Chromosome getEmptyChromosome() {
        Chromosome chromosome = new Chromosome();
        for (int j = 0; j < this.readFromFile.getVehicles().size(); j++) {
            int depotIndex = j / this.readFromFile.getVehiclesPerDepot();
            Depot startDepot = this.readFromFile.getDepots().get(depotIndex);
            Route route = new Route(startDepot);
            route.setVehicle(this.readFromFile.getVehicles().get(j));
            chromosome.addRoute(route);
        }
        return chromosome;
    }

    public void getSolution(Chromosome chromosome){
        for (Customer customer : this.readFromFile.getCustomers()){
            RouteAndIndex routeAndIndex = Model.doCase1(chromosome, customer);
            Route route = routeAndIndex.getRoute();
            boolean valid = routeAndIndex.getValid();
            if (valid){
                route.addCustomer(customer, routeAndIndex.getIndex());
                route.setEndDepot(routeAndIndex.getClosestDepot());
            } else {
                    chromosome.addRestCustomer(customer);
                } if (!chromosome.getRestCustomers().isEmpty()) {
                break;
            }
        }
        if (chromosome.getRestCustomers().isEmpty()) {
            this.chromosomes.add(chromosome);
        } else {
            System.out.println("Could not find solution");
        }
    }

    public static RouteAndIndex doCase1(Chromosome chromosome, Customer customer){
        // add customer to the route and position to which it adds the minimal distance
        Route bestRoute = chromosome.getRoutes().get(0);
        double minimalAddedDistance = 1000;
        int index = bestRoute.getNodes().size()-1;
        Depot closestDepot = Model.getClosestDepot(customer, chromosome.getChromosomeDepots());
        Depot bestDepot = closestDepot;
        boolean valid = false;
        for (Route route : chromosome.getRoutes()) {
            ArrayList<Node> routeNodes = route.getNodes();
            for (int i = 1; i < route.getNodes().size(); i++){
                if (i < route.getNodes().size() && routeNodes.size() > 2) {
                    if (routeNodes.get(routeNodes.size()-2) instanceof Customer) {
                        closestDepot = route.getEndDepot();
                    }
                }
                double addedDistance = route.getAddedDistance(customer, closestDepot, i);
                boolean feasible = route.checkValidRoute(customer, closestDepot, i);
                if ((addedDistance < minimalAddedDistance) && feasible) {
                    bestRoute = route;
                    minimalAddedDistance = addedDistance;
                    index = i;
                    bestDepot = closestDepot;
                    valid = true;
                }
            }
        }
        RouteAndIndex routeAndIndex = new RouteAndIndex(bestRoute, index, bestDepot, valid);
        return routeAndIndex;
    }

    public static Depot getClosestDepot(Customer customer, ArrayList<Depot> depots){
        Coordinate customerCoordinate = customer.getCoordinate();
        Depot closestDepot = depots.get(0);
        double minDistance = customerCoordinate.getEuclidianDistance(closestDepot.getCoordinate());
        for (Depot depot : depots){
            double distance = customerCoordinate.getEuclidianDistance(depot.getCoordinate());
            if (distance < minDistance){
                closestDepot = depot;
                minDistance = distance;
            }
        }
        return closestDepot;
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }
}
