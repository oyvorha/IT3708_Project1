import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {

    private double mutationRate;
    private double crossoverRate;
    private int populationSize;
    private ArrayList<Chromosome> rankedChromosomes;
    private Random random;

    public GeneticAlgorithm(double mutationRate, double crossoverRate, int populationSize, ArrayList<Chromosome> chromosomes) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.populationSize = populationSize;
        this.rankedChromosomes = chromosomes;
        this.rankChromosomes();
        this.random = new Random();
    }

    private void rankChromosomes() {
        System.out.println("Unsorted chromosomes: "+this.rankedChromosomes);
        Collections.sort(this.rankedChromosomes);
        if (this.rankedChromosomes.size() > populationSize){
            this.rankedChromosomes.subList(0, populationSize);
        }
    }

    public ArrayList<Chromosome> getRankedChromosomes() {
        return rankedChromosomes;
    }

    public void mutation1(Chromosome possibleMutationChromosome){
        int doMutation = this.random.nextInt(0);
        if (doMutation < mutationRate){
            int routeNumber = this.random.nextInt(possibleMutationChromosome.getRoutes().size()-1);
            Route mutationRoute = possibleMutationChromosome.getRoutes().get(routeNumber);
            if (mutationRoute.getNodes().size() > 4) {
                    int index1 = this.random.nextInt(mutationRoute.getNodes().size()-3)+1;
                    int index2 = this.random.nextInt(mutationRoute.getNodes().size()-3)+1;
                    mutationRoute.swapCustomers(index1, index2);
                }
            }
        }

    public void crossover(Chromosome chromosome1, Chromosome chromosome2) {
        Chromosome offspringChromosome = new Chromosome(chromosome1);
        Random random = new Random();
        int randomIndex = random.nextInt(chromosome2.getRoutes().size());
        Route crossRoute = chromosome2.getRoutes().get(randomIndex);
        Depot startDepotCrossRoute = crossRoute.getStartDepot();
        ArrayList<Depot> chromosomeDepots = chromosome1.getChromosomeDepots();

        ArrayList<Customer> notAllocatedCustomers = new ArrayList<>();
        ArrayList<Route> resetRoutes = new ArrayList<>();

        for (Route route : offspringChromosome.getRoutes()){
            //Reset alle i offspringChromosome som har samme startDepot som crossRoute
            if (route.getStartDepot() == startDepotCrossRoute){
                for (Node node : route.getNodes()) {
                    if (node instanceof Customer) {
                        notAllocatedCustomers.add((Customer) node);
                    }
                }
                route.resetRoute();
                resetRoutes.add(route);
            }
            //Gå gjennom alle customers i crossRoute og fjern dem fra deres current rute
            for (Node node : crossRoute.getNodes()){
                if (route.getNodes().contains(node)){
                    route.getNodes().remove(node);
                }
            }
        }
        //Legg til crossRoute i offspringChromosome til en route som er resatt
        Route crossedRoute = resetRoutes.get(0);
        for (Node node : crossRoute.getNodes()){
            if (node instanceof Customer) {
                crossedRoute.addCustomer((Customer) node, crossedRoute.getNodes().size() - 1);
            }
        }
        // Alloker notAllocatedCustomers til ruter
        for (Customer customer : notAllocatedCustomers){
            double minimalAddedDistance = 100000;
            int bestIndex = 1;
            Route bestRoute = offspringChromosome.getRoutes().get(0);
            Depot closestDepot = getClosestDepot(customer, chromosomeDepots);
            //Sjekk added distance på alle ruter på alle indexer
            for (Route route : offspringChromosome.getRoutes()) {
                for (int j = 0; j<route.getNodes().size(); j++){
                    double addedDistance = route.getAddedDistance(customer, closestDepot, j);
                    if (addedDistance < minimalAddedDistance){
                        minimalAddedDistance = addedDistance;
                        bestRoute = route;
                        bestIndex = j;
                    }
                }
            }
            bestRoute.addCustomer(customer, bestIndex);
        }
    }

    public Depot getClosestDepot(Customer customer, ArrayList<Depot> depots){
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

}
