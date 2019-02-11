import java.util.*;

public class Program {

    private static final int initialSolutions = 1000;
    private static final String dataset = "p21";
    private static final int visualSol = 0;
    private static final int generations = 1000;
    private static final int POPULATION_SIZE = 200;
    private static final int mutationRate = 5;
    private static final int crossoverRate = 50;


    public static void main(String[] args) {
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/" + dataset);
        Model model = new Model(readFromFile);
        System.out.println("No of customers: " + readFromFile.getCustomers().size());
        System.out.println("No of depots " + readFromFile.getDepots().size());
        System.out.println("No of cars " + readFromFile.getVehicles().size());
        model.getFirstSolutions(initialSolutions);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(mutationRate, crossoverRate, initialSolutions,
                model.getChromosomes());

        Program.visualizeChromosome(geneticAlgorithm.getRankedChromosomes().get(0), readFromFile, "start");

        ArrayList<Chromosome> population1 = new ArrayList<>(geneticAlgorithm.getRankedChromosomes());
        if (population1.size() <= POPULATION_SIZE) {
            for (int i=0; i<5; i++){
                population1.add(geneticAlgorithm.getRankedChromosomes().get(0));
            }}

        ArrayList<Chromosome> population = new ArrayList<>(population1.subList(visualSol, POPULATION_SIZE));

        System.out.println("Initial population: "+population);
        Program.visualizeChromosome(population.get(0), readFromFile, "Start chromosome");

        Random random = new Random();
        for (int n = 0; n<generations; n++) {
            ArrayList<Chromosome> tempPop = new ArrayList<>();
            tempPop.add(population.get(0));
            for (Chromosome mother : population) {
                int crossoverLikelihood = random.nextInt(100);
                if (crossoverLikelihood < geneticAlgorithm.getCrossoverRate()) {
                    int fatherIndex = random.nextInt(4);
                    Chromosome father = population.get(fatherIndex);
                    Chromosome offspring = geneticAlgorithm.crossover(mother, father);
                    if (!mother.equals(offspring) && mother.getTotalDistance() != offspring.getTotalDistance()) {
                        tempPop.add(offspring);
                    } else {
                        Chromosome newC = geneticAlgorithm.mutation3(mother);
                        if (mother.getTotalDistance() != offspring.getTotalDistance()) {
                            tempPop.add(newC);
                        }
                    }
                }   int mutation = random.nextInt(100);
                    if (mutation < geneticAlgorithm.getMutationRate()) {
                        int noOfMutations = random.nextInt(7);
                        for (int i=0; i<noOfMutations; i++) {
                            Chromosome mutated = Program.doMutation(mother, geneticAlgorithm);
                            if (mother.getTotalDistance() != mutated.getTotalDistance()) {
                                tempPop.add(mutated);
                            }
                        }
                    } else {
                        tempPop.add(mother);
                    }
                }
                if (tempPop.size()<POPULATION_SIZE) {
                    System.out.println("-------------------------------------------------------");
                for (int f=1; f<POPULATION_SIZE; f++) {
                    int rand = random.nextInt(population.size());
                    tempPop.add(population.get(rand));
                    }
                }
                population = tempPop;
                Collections.sort(population);
                population = new ArrayList<>(population.subList(visualSol, POPULATION_SIZE));
                System.out.println(population);
            }
        Chromosome visChrom = population.get(0);
        visChrom.perfectSwap();
        Program.printSolutions(visChrom);
        Program.visualizeChromosome(visChrom, readFromFile, "Final chromosome ");

        }

        public static void printSolutions(Chromosome chromosome){
            System.out.println("-------------------Solution table----------------------");
            String routeString = chromosome.getTotalDistance()+"\n";
            for (Route route : chromosome.getRoutes()){
                routeString += route.getStartDepot().getDepotID()+"\t";
                routeString += route.getVehicle().getVehicleID()+"\t";
                routeString += route.getTotalDistance()+"\t";
                routeString += route.getCurrentDemand()+ "\t";
                routeString += route.getEndDepot().getDepotID()+"\t";
                for (Node n : route.getNodes()){
                    if (n instanceof Customer){
                        routeString += ((Customer) n).getCustomerNo()+" ";
                    }
                }
                routeString += "\n";
            }
            System.out.println(routeString);
        }

        public static Chromosome doMutation(Chromosome mother, GeneticAlgorithm geneticAlgorithm) {
            Random mutationRandom = new Random();
            int mutationCase = mutationRandom.nextInt(3);
            if (mutationCase == 0) {
                Chromosome newC = geneticAlgorithm.mutation2(mother);
                int select = mutationRandom.nextInt(10);
                for (int k = 0; k < select; k++) {
                    newC = geneticAlgorithm.mutation2(newC);
                }
                return newC;
            }
            if (mutationCase == 1) {
                Chromosome newC = geneticAlgorithm.mutation3(mother);
                int select = mutationRandom.nextInt(3);
                for (int k = 0; k < select; k++) {
                    newC = geneticAlgorithm.mutation3(newC);
                }
            } else {
                Chromosome newC = geneticAlgorithm.mutation2(mother);
                newC.perfectSwap();
                return newC;
            }
            return mother;
        }

    public static void visualizeChromosome(Chromosome chromosome, ReadFromFile rff, String header) {
        ArrayList<Route> routes = chromosome.getRoutes();
        HashMap<Route, List<Node>> visual = new HashMap<>();
        for (Route r : routes) {
            ArrayList<Node> nodes = r.getNodes();
            visual.put(r, nodes);
        }
        Visualization visualization = new Visualization();
        visualization.visualize(visual, chromosome.getRestCustomers() , chromosome.getTotalDistance(), rff.getBenchmark(dataset), header);
    }

}
