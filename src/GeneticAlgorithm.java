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

    public Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        Chromosome offspringChromosome = new Chromosome(chromosome1);
        Random random = new Random();
        int randomIndex = random.nextInt(chromosome2.getRoutes().size());
        Route crossRoute = chromosome2.getRoutes().get(randomIndex);
        Depot startDepotCrossRoute = crossRoute.getStartDepot();
        ArrayList<Depot> chromosomeDepots = chromosome1.getChromosomeDepots();

        ArrayList<Customer> notAllocatedCustomers = new ArrayList<>();
        ArrayList<Route> resetRoutes = new ArrayList<>();

        //offspringChromosome.getRoutes().stream().forEach(c-> System.out.println(c.getNodes()));

        for (Route r : offspringChromosome.getRoutes()){
            //Reset alle i offspringChromosome som har samme startDepot som crossRoute
            if (r.getStartDepot() == startDepotCrossRoute){
                for (Node node : r.getNodes()) {
                    if (node instanceof Customer) {
                        notAllocatedCustomers.add((Customer) node);
                    }
                }
                r.resetRoute();
                resetRoutes.add(r);
            }
            //Gå gjennom alle customers i crossRoute og fjern dem fra deres current rute
            for (Node node : crossRoute.getNodes()){
                if (r.getNodes().contains(node) && !(node instanceof Depot)){
                    r.getNodes().remove(node);
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
        long timeStart = System.currentTimeMillis();
        for (Customer customer : notAllocatedCustomers){
            RouteAndIndex routeAndIndex = Model.doCase1(offspringChromosome, customer, offspringChromosome.getChromosomeDepots());
            Route route = routeAndIndex.getRoute();
            boolean valid = routeAndIndex.getValid();
            if (valid){
                route.addCustomer(customer, routeAndIndex.getIndex());
                route.setEndDepot(routeAndIndex.getClosestDepot());
            }
            if ((System.currentTimeMillis()-timeStart) > 10000){
                offspringChromosome.addRestCustomer(customer);
                System.out.println("CANNOT FIND SOLUTION");
                valid = true;
                timeStart = System.currentTimeMillis();
            }
        }
        return offspringChromosome;
    }

}
