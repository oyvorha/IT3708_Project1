import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program {

    private static final int initialSolutions = 1;
    private static final String dataset = "p18";
    private static final int visualSol = 0;

    public static void main(String[] args) {
        ReadFromFile readFromFile = new ReadFromFile("./Files/DataFiles/"+dataset);
        System.out.println("No of customers: "+readFromFile.getCustomers().size());
        System.out.println("No of depots "+readFromFile.getDepots().size());
        System.out.println("No of cars "+readFromFile.getVehicles().size());
        Model model = new Model(readFromFile);
        model.getFirstSolutions(initialSolutions);
        Chromosome visualChromosome = model.getChromosomes().get(visualSol);
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
