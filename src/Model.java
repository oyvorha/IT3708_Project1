import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        long timeStart = System.currentTimeMillis();
        boolean timeout = false;
        for (Customer customer : this.readFromFile.getCustomers()){
            Boolean valid = false;
            Depot closestDepot = this.getClosestDepot(customer);
            while (!valid) {
                int caseNumber = this.random.nextInt(100);
                if (caseNumber <= 5 && !timeout) {
                    //System.out.println("Case 0");
                    Route route = this.doCase0(chromosome, customer);
                    int index = route.getNodes().size()-2;
                    valid = route.checkValidRoute(customer, closestDepot, index);
                    if (valid){
                        route.addCustomer(customer, index);
                        route.setEndDepot(closestDepot);
                        System.out.println("allocated "+this.readFromFile.getCustomers().indexOf(customer));
                    }
                } else if (caseNumber >= 6 && !timeout) {
                    //System.out.println("Case 1");
                    RouteAndIndex routeAndIndex = this.doCase1(chromosome, customer);
                    Route route = routeAndIndex.getRoute();
                    int index = routeAndIndex.getIndex();
                    valid = route.checkValidRoute(customer, closestDepot, index);
                    if (valid){
                        route.addCustomer(customer, index);
                        route.setEndDepot(closestDepot);
                        System.out.println("allocated."+this.readFromFile.getCustomers().indexOf(customer));
                    }
                }
                if ((System.currentTimeMillis()-timeStart) > 20000){
                    timeout = true;
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
        chromosomes.add(chromosome);
    }

    public Route doCase0(Chromosome chromosome, Customer customer){
    // add customer to random route
        int routeNumber = this.random.nextInt(chromosome.getRoutes().size());
        return chromosome.getRoutes().get(routeNumber);
    }

    public RouteAndIndex doCase1(Chromosome chromosome, Customer customer){
        // add customer to the route to which it adds the minimal distance
        Route bestRoute = chromosome.getRoutes().get(0);
        double minimalAddedDistance = 1000;
        int index = chromosome.getRoutes().get(0).getNodes().size()-1;
        for (Route route : chromosome.getRoutes()) {
            for (int i = 1; i < chromosome.getRoutes().size()-1; i++){
                double addedDistance = route.getAddedDistance(customer, this.getClosestDepot(customer), i);
                if (addedDistance < minimalAddedDistance) {
                    bestRoute = route;
                    minimalAddedDistance = addedDistance;
                    index = i;
                }
            }
        }
        System.out.println("Route "+bestRoute.getVehicle().getVehicleID()+" has min added distance "+ minimalAddedDistance);
        RouteAndIndex routeAndIndex = new RouteAndIndex(bestRoute, index);
        return routeAndIndex;
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
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/p22");
        System.out.println("No of customers: "+readFromFile.getCustomers().size());
        Model model = new Model(readFromFile);
        model.getFirstSolutions(1);
        double total = 0;
        ArrayList<Route> cRoutes = model.chromosomes.get(0).getRoutes();
        HashMap<Route, List<Node>> visual = new HashMap<>();
        for (Route r : cRoutes) {
            total += r.getTotalDistance();
            ArrayList<Node> nodes = r.getNodes();
            visual.put(r, nodes);
        }
        Visualization visualization = new Visualization();
        visualization.visualize(visual, total, 0);

    }

}
