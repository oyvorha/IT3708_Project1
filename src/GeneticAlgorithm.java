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

    public static HashMap<Depot, ArrayList<Route>> createDepotMap (ArrayList<Route> routes) {
        HashMap<Depot, ArrayList<Route>> depotMap = new HashMap<>();
        for (Route r : routes) {
            Depot startDepot = r.getStartDepot();
            if (depotMap.containsKey(startDepot)) {
                depotMap.get(startDepot).add(r);
            } else {
                ArrayList<Route> routeList = new ArrayList<>();
                routeList.add(r);
                depotMap.put(startDepot, routeList);
            }
        } return depotMap;
    }

    public Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        Chromosome offspringChromosome = new Chromosome(chromosome1);
        int maxCrossRoutes = Math.floorDiv(chromosome2.getRoutes().size(), 2);
        ArrayList<Route> crossRoutes = new ArrayList<>();
        for (int i = 0; i<maxCrossRoutes; i++) {
            int randomRoute = random.nextInt(maxCrossRoutes);
            Route possCrossRoute = chromosome2.getRoutes().get(randomRoute);
            if (!crossRoutes.contains(possCrossRoute)) {
                crossRoutes.add(possCrossRoute);
            }
        }

        ArrayList<Customer> notAllocatedCustomers = new ArrayList<>();
        HashMap<Route, Route> resetRoutes = new HashMap<>();
        HashMap<Depot, ArrayList<Route>> depotMap = GeneticAlgorithm.createDepotMap(offspringChromosome.getRoutes());

        for (Route crossRoute : crossRoutes) {
            Depot startDepotCrossRoute = crossRoute.getStartDepot();
            int customers = 100;
            Route reset = null;
            for (Route r : depotMap.get(startDepotCrossRoute)) {
                if (r.getNodes().size()<customers && !resetRoutes.keySet().contains(r)) {
                    customers = r.getNodes().size();
                    reset = r;
                }
            }
            resetRoutes.put(reset, crossRoute);
        }

        for (Route resetroute : resetRoutes.keySet()) {
            //Legg til crossRoute i offspringChromosome til en route som er resatt
            for (Node node : resetroute.getNodes()){
                if (node instanceof Customer) {
                    notAllocatedCustomers.add((Customer) node);
                }
            } resetroute.resetRoute();
            Route cross = resetRoutes.get(resetroute);
            for (Node crossNode : cross.getNodes()) {
                if (crossNode instanceof Customer) {
                    for (Route restRoute : offspringChromosome.getRoutes()){
                        if (restRoute.getNodes().contains(crossNode)) {
                            restRoute.removeCustomer((Customer) crossNode);
                        }
                    }
                    resetroute.addCustomer((Customer) crossNode, resetroute.getNodes().size()-1);
                    if (notAllocatedCustomers.contains(crossNode)) {
                        notAllocatedCustomers.remove(crossNode);
                    }
                }
            }

        }
        // Alloker notAllocatedCustomers til ruter
        for (Customer customer : notAllocatedCustomers){
            RouteAndIndex routeAndIndex = Model.doCase1(offspringChromosome, customer, offspringChromosome.getChromosomeDepots());
            Route route = routeAndIndex.getRoute();
            boolean valid = routeAndIndex.getValid();
            if (valid){
                route.addCustomer(customer, routeAndIndex.getIndex());
                route.setEndDepot(routeAndIndex.getClosestDepot());
            } else {
                System.out.println("NO OFFSPRING FOUND");
                return chromosome1;
            }
        }
        offspringChromosome.calculateTotalDistance();
        return offspringChromosome;
    }
    
}
