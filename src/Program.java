import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program {

    private static final int initialSolutions = 200;
    private static final String dataset = "p01";
    private static final int visualSol = 0;


    public static void main(String[] args) {
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/"+dataset);
        Model model = new Model(readFromFile);
        System.out.println("No of customers: "+readFromFile.getCustomers().size());
        System.out.println("No of depots "+readFromFile.getDepots().size());
        System.out.println("No of cars "+readFromFile.getVehicles().size());
        model.getFirstSolutions(initialSolutions);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(0, 0, initialSolutions,
                model.getChromosomes());

        Chromosome visualChromosome = geneticAlgorithm.getRankedChromosomes().get(visualSol);
        Program.visualizeChromosome(visualChromosome, readFromFile);
    }

    public static void visualizeChromosome(Chromosome chromosome, ReadFromFile rff) {
        double total = 0;
        ArrayList<Route> routes = chromosome.getRoutes();
        HashMap<Route, List<Node>> visual = new HashMap<>();
        for (Route r : routes) {
            total += r.getTotalDistance();
            ArrayList<Node> nodes = r.getNodes();
            visual.put(r, nodes);
        }
        Visualization visualization = new Visualization();
        visualization.visualize(visual, chromosome.getRestCustomers() , total, rff.getBenchmark(dataset));
    }

}
