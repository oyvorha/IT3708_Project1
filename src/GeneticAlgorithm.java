import java.util.*;

public class GeneticAlgorithm {

    private double mutationRate;
    private double crossoverRate;
    private ArrayList<Chromosome> rankedChromosomes;
    private Random random;

    public GeneticAlgorithm(double mutationRate, double crossoverRate, ArrayList<Chromosome> chromosomes) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.rankedChromosomes = chromosomes;
        this.rankChromosomes();
        this.random = new Random();
    }

    private void rankChromosomes() {
        System.out.println("Unsorted chromosomes: "+this.rankedChromosomes);
        Collections.sort(this.rankedChromosomes);
    }

    public ArrayList<Chromosome> getRankedChromosomes() {
        return this.rankedChromosomes;
    }

    public Chromosome mutation3(Chromosome possibleMutationChromosome){
        Chromosome mutatedChromosome = new Chromosome(possibleMutationChromosome);
        int routeIndex1 = this.random.nextInt(mutatedChromosome.getRoutes().size()-1);
        Route mutationRoute1 = mutatedChromosome.getRoutes().get(routeIndex1);

        if (!(mutationRoute1.getNodes().size() < 4)) {
            int index1 = this.random.nextInt(mutationRoute1.getNodes().size() - 3) + 1;
            Customer moveCustomer = (Customer) mutationRoute1.getNodes().get(index1);
            mutationRoute1.removeCustomer(moveCustomer);

            for (Route r : mutatedChromosome.getRoutes()) {
                if (mutationRoute1.equals(r)) {
                    continue;
                }
                double smallestDistance = 1000;
                int index = 1;
                Depot bestDepot = r.getEndDepot();
                for (int i = 1; i < r.getNodes().size() - 1; i++) {
                    if (i == r.getNodes().size() - 2) {
                        bestDepot = Model.getClosestDepot(moveCustomer, mutatedChromosome.getChromosomeDepots());
                    }
                    double addedDistance = r.getAddedDistance(moveCustomer, bestDepot, i);
                    boolean feasible = r.checkValidRoute(moveCustomer, bestDepot, i);
                    if (addedDistance < smallestDistance && feasible) {
                        index = i;
                        smallestDistance = addedDistance;
                    }
                }
                if (smallestDistance < 1000) {
                    r.addCustomer(moveCustomer, index);
                    r.setEndDepot(bestDepot);
                    mutatedChromosome.calculateTotalDistance();
                    return mutatedChromosome;
                }
            }
        }
        return possibleMutationChromosome;
    }

    public Chromosome mutation2(Chromosome possibleMutationChromosome){
            Chromosome mutatedChromosome = new Chromosome(possibleMutationChromosome);
            int routeIndex1 = this.random.nextInt(mutatedChromosome.getRoutes().size());
            Route mutationRoute1 = mutatedChromosome.getRoutes().get(routeIndex1);

            if (!(mutationRoute1.getNodes().size() < 4)) {
                int index1 = this.random.nextInt(mutationRoute1.getNodes().size() - 2) + 1;
                Customer moveCustomer = (Customer) mutationRoute1.getNodes().get(index1);
                mutationRoute1.removeCustomer(moveCustomer);
                RouteAndIndex routeAndIndex = Model.doCase1(mutatedChromosome, moveCustomer);
                boolean valid = routeAndIndex.getValid();
                if (valid){
                    Route route = routeAndIndex.getRoute();
                    route.addCustomer(moveCustomer, routeAndIndex.getIndex());
                    route.setEndDepot(routeAndIndex.getClosestDepot());
                    return mutatedChromosome;
                }
            }
        return possibleMutationChromosome;
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
        int maxCrossRoutes = Math.floorDiv(chromosome2.getRoutes().size(), 3);
        ArrayList<Route> crossRoutes = new ArrayList<>();
        for (int i = 0; i<maxCrossRoutes; i++) {
            int randomRoute = random.nextInt(chromosome2.getRoutes().size());
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
            resetroute.setEndDepot(cross.getEndDepot());
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
            RouteAndIndex routeAndIndex = Model.doCase1(offspringChromosome, customer);
            Route route = routeAndIndex.getRoute();
            boolean valid = routeAndIndex.getValid();
            if (valid){
                route.addCustomer(customer, routeAndIndex.getIndex());
                route.setEndDepot(routeAndIndex.getClosestDepot());
            } else {
                return chromosome1;
            }
        }
        offspringChromosome.calculateTotalDistance();
        return offspringChromosome;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }
}
