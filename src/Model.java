import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Model {

    private ReadFromFile readFromFile;
    private ArrayList<Chromosome> chromosomes;
    private Random random;

    public Model(ReadFromFile readFromFile){
        this.chromosomes = new ArrayList<>();
        this.readFromFile = readFromFile;
        this.random = new Random();
    }

    public void getFirstSolutions(int numberOfSolutions){
        for (int i = 0; i<numberOfSolutions; i++){
            Collections.shuffle(this.readFromFile.getCustomers());
            this.getSolution();
        }
    }

    public void getSolution(){
        Chromosome chromosome = new Chromosome();
        for (int j = 0; j<this.readFromFile.getVehicles().size(); j++) {
            int depotIndex = j/this.readFromFile.getVehiclesPerDepot();
            Depot startDepot = this.readFromFile.getDepots().get(depotIndex);
            Route route = new Route(startDepot);
            route.setVehicle(this.readFromFile.getVehicles().get(j));
            chromosome.addRoute(route);

        }
        long timeStart = System.currentTimeMillis();
        ArrayList<Customer> restCustomers = new ArrayList<>();
        for (Customer customer : this.readFromFile.getCustomers()){
            boolean valid = false;
            Depot closestDepot = this.getClosestDepot(customer, this.readFromFile.getDepots());
            while (!valid) {
                int caseNumber = this.random.nextInt(100);
                if (caseNumber < 50) {
                    Route route = this.doCase0(chromosome, customer);
                    int index = 1;
                    if (route.getNodes().size() > 2) {
                        index = route.getNodes().size()-2;
                    }
                    valid = route.checkValidRoute(customer, closestDepot, index);
                    if (valid){
                        route.addCustomer(customer, index);
                        route.setEndDepot(closestDepot);
                    }
                } else {
                    RouteAndIndex routeAndIndex = this.doCase1(chromosome, customer, this.readFromFile.getDepots());
                    Route route = routeAndIndex.getRoute();
                    valid = routeAndIndex.getValid();
                    if (valid){
                        route.addCustomer(customer, routeAndIndex.getIndex());
                        route.setEndDepot(routeAndIndex.getClosestDepot());
                    }
                }
                if ((System.currentTimeMillis()-timeStart) > 10000){
                    chromosome.addRestCustomer(customer);
                    System.out.println("NOT IN SOLUTION");
                    valid = true;
                    timeStart = System.currentTimeMillis();
                }
            }
        }
        if (chromosome.getRestCustomers().isEmpty()) {
            chromosomes.add(chromosome);
        }
    }

    public Route doCase0(Chromosome chromosome, Customer customer){
    // add customer to random route
        int routeNumber = this.random.nextInt(chromosome.getRoutes().size());
        return chromosome.getRoutes().get(routeNumber);
    }

    public static RouteAndIndex doCase1(Chromosome chromosome, Customer customer, ArrayList<Depot> depots){
        // add customer to the route and position to which it adds the minimal distance
        Route bestRoute = chromosome.getRoutes().get(0);
        double minimalAddedDistance = 10000;
        int index = bestRoute.getNodes().size()-2;
        Depot closestDepot = Model.getClosestDepot(customer, depots);
        Depot bestDepot = closestDepot;
        boolean valid = false;
        for (Route route : chromosome.getRoutes()) {
            ArrayList<Node> routeNodes = route.getNodes();
            for (int i = 1; i < route.getNodes().size(); i++){
                if (i < route.getNodes().size() && routeNodes.size() > 2) {
                    if (routeNodes.get(routeNodes.size()-2) instanceof Customer) {
                        closestDepot = Model.getClosestDepot((Customer) routeNodes.get(routeNodes.size()-2), depots);
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
        double minDistance = customerCoordinate.getEuclidianDistance(depots.get(0).getCoordinate());
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
