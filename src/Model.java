import java.util.ArrayList;
import java.util.Random;

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
            this.getSolution();
        }
    }

    public void getSolution(){
        Chromosome chromosome = new Chromosome();
        for (int j = 0; j<this.readFromFile.getVehicles().size(); j++) {
            Depot startDepot = this.readFromFile.getDepots().get(j/this.readFromFile.getDepots().size());
            Route route = new Route(startDepot);
            route.setVehicle(this.readFromFile.getVehicles().get(j));
            chromosome.addRoute(route);
        }
        // hva med å shuffle customers her
        for (Customer customer : this.readFromFile.getCustomers()){
            Boolean valid = false;
            Depot closestDepot = this.getClosestDepot(customer);
            while (!valid) {
                int caseNumber = this.random.nextInt(2);
                if (caseNumber == 0) {
                    System.out.println("Case 0");
                    Route route = this.doCase0(chromosome, customer);
                    valid = route.checkValidRoute(customer, closestDepot);
                    if (valid){
                        route.addCustomer(customer);
                        route.setEndDepot(closestDepot);
                    }
                } else if (caseNumber == 1) {
                    System.out.println("Case 1");
                    Route route = this.doCase1(chromosome, customer);
                    valid = route.checkValidRoute(customer, closestDepot);
                    if (valid){
                        route.addCustomer(customer);
                        route.setEndDepot(closestDepot);
                    }
                }
                /*
                else if (caseNumber == 2){
                    Route route = this.doCase2(chromosome, customer);
                }
                else if (caseNumber == 3){
                    Route route = this.doCase3(chromosome, customer);
                }
                */
            }
        }
        chromosomes.add(chromosome);
    }

    public Route doCase0(Chromosome chromosome, Customer customer){
    // add customer to random route
        int routeNumber = this.random.nextInt(chromosome.getRoutes().size());
        return chromosome.getRoutes().get(routeNumber);
    }

    public Route doCase1(Chromosome chromosome, Customer customer){
        // add customer to the route to which it adds the minimal distance
        Route bestRoute = chromosome.getRoutes().get(0);
        double minimalAddedDistance = bestRoute.getAddedDistance(customer, this.getClosestDepot(customer));
        for (Route route : chromosome.getRoutes()) {
            if (route.getAddedDistance(customer, this.getClosestDepot(customer)) < minimalAddedDistance) {
                bestRoute = route;
                minimalAddedDistance = route.getAddedDistance(customer, this.getClosestDepot(customer));
            }
        }
        return bestRoute;
    }

    /*
    public Route doCase2(Chromosome chromosome, Customer customer){
    }

    public Route doCase3(Chromosome chromosome, Customer customer){
    }
    */

    public Depot getClosestDepot(Customer customer){
        Coordinate customerCoordinate = customer.getCoordinate();
        Depot closestDepot = this.readFromFile.getDepots().get(0);
        double minDistance = customerCoordinate.getEuclidianDistance(this.readFromFile.getDepots().get(0).getCoordinate());
        for (Depot depot : this.readFromFile.getDepots()){
            double distance = customerCoordinate.getEuclidianDistance(depot.getCoordinate());
            if (distance < minDistance){
                closestDepot = depot;
                minDistance = distance;
            }
        }
        return closestDepot;
    }

    public static void main(String[] args) {
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/p01");
        Model model = new Model(readFromFile);
        model.getFirstSolutions(1);
        System.out.println("No of customers: "+readFromFile.getCustomers().size());
        System.out.println("Running Model");
        for (Chromosome c : model.chromosomes) {
            ArrayList<Route> routes = c.getRoutes();
            double total = 0;
            for (Route r : routes) {
                total += r.getTotalDistance();
            }
            System.out.println("Total distance "+ total);
        }
    }

}
