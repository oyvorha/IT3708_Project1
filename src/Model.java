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
        /*
        make new chromosome with the fields from
        a chromosome consists of a list of routes
        route has a list of customers,  a vehicle, startdepot, enddepot

        ___HEURISTICS___
        X minst mulig ekstra distanse
        X random
        rute fra nærmeste depot i avstand (euclidean)
        HUSK: Check feasibility
        */

        Chromosome chromosome = new Chromosome();

        //initialize routs (# = number of vehicles)
        for (int j = 0; j<this.readFromFile.getVehicles().size(); j++) {
            Route route = new Route();
            chromosome.addRoute(route);
            route.setStartDepot(this.readFromFile.getDepots().get(j/this.readFromFile.getDepots().size()));

            //set end depot til hver route. hvordan?? nærmeste depot til siste customer? eller samme som startDepot initiert
            route.setStartDepot(this.readFromFile.getDepots().get(j/this.readFromFile.getDepots().size()));
        }

        // for each customer, do:
        // hva med å shuffle customers her
        for (Customer customer : this.readFromFile.getCustomers()){

            int caseNumber = this.random.nextInt(2);

            if (caseNumber == 0){
                this.doCase0(chromosome, customer);
            }
            else if (caseNumber == 1){
                this.doCase1(chromosome, customer);
            }
            else if (caseNumber == 2){
                this.doCase2(chromosome, customer);
            }
            else if (caseNumber == 3){
                this.doCase3(chromosome, customer);
            }
        }
    }

    /*
    for (int i=0; i<this.readFromFile.getDepots().size(); i++){
        Depot depot = this.readFromFile.getDepots().get(i);
        int vehiclesPerDepot = this.readFromFile.getVehiclesPerDepot();
        for (int j=0; j<vehiclesPerDepot; j++){
            Vehicle vehicle = this.readFromFile.getVehicles().get(i*this.readFromFile.getVehiclesPerDepot()+j);
            depot.addVehicle(vehicle);
            vehicle.setStartDepot(depot);
        }
        // assign each customer to a vehicle
        for (int c=0; c<this.readFromFile.getCustomers().size(); c++){
            int index = this.random.nextInt(readFromFile.getVehicles().size());
            this.readFromFile.getVehicles().get(index).addCustomer(this.readFromFile.getCustomers().get(c));
            this.readFromFile.getCustomers().get(c).setVisitingVehicle(this.readFromFile.getVehicles().get(index));
        }
    }
    */
    //bil må kjøre og oppfylle demand. Ikke kjøre mer enn max eller ha mer bagasje enn max. Dette sjekkes i checkValidRoute() i ROute.

    //Husk å assigne en vehicle til hver rute - ok kan gjøres til slutt.

    public void doCase0(Chromosome chromosome, Customer customer){
    // add customer to random route
        int routeNumber = this.random.nextInt(chromosome.getRoutes().size());
        chromosome.getRoutes().get(routeNumber).addCustomer(customer); //check feasibility
    }

    public void doCase1(Chromosome chromosome, Customer customer){
        Route bestRoute = chromosome.getRoutes().get(0);
        float minimalAddedDistance = bestRoute.getAddedDistance(customer);
        for (Route route : chromosome.getRoutes()){
            if (route.getAddedDistance(customer) < minimalAddedDistance){
                    bestRoute = route;
                    minimalAddedDistance = route.getAddedDistance(customer);
            }
        }
        bestRoute.addCustomer(customer); //check feasibility
    }

    public void doCase2(Chromosome chromosome, Customer customer){

    }

    public void doCase3(Chromosome chromosome, Customer customer){

    }

    public Depot getClosestDepot(Customer customer){
        Coordinate customerCoordinate = customer.getCoordinate();
        Depot bestDepot = this.readFromFile.getDepots().get(0);
        double minDistance = customerCoordinate.getEuclidianDistance(this.readFromFile.getDepots().get(0).getCoordinate());
        for (Depot depot : this.readFromFile.getDepots()){
            double distance = customerCoordinate.getEuclidianDistance(depot.getCoordinate());
            if (distance < minDistance){
                bestDepot = depot;
                minDistance = distance;
            }
        }
        return bestDepot;
    }

    public static void main(String[] args) {
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/p01");
        Model model = new Model(readFromFile);
        model.getFirstSolutions(100);
        System.out.println("No of customers: "+readFromFile.getCustomers().size());
        System.out.println("Running Model");
    }

}
