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

    public void crossover() {
        System.out.println("Do crossover");
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
    }