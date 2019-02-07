import java.util.ArrayList;
import java.util.Collections;

public class GeneticAlgorithm {

    private double mutationRate;
    private double crossoverRate;
    private int populationSize;
    private ArrayList<Chromosome> rankedChromosomes;

    public GeneticAlgorithm(double mutationRate, double crossoverRate, int populationSize, ArrayList<Chromosome> chromosomes) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.populationSize = populationSize;
        this.rankedChromosomes = chromosomes;
        this.rankChromosomes();
    }

    private void rankChromosomes() {
        System.out.println("Unsorted chromosomes: "+this.rankedChromosomes);
        Collections.sort(this.rankedChromosomes);
    }

    public ArrayList<Chromosome> getRankedChromosomes() {
        return rankedChromosomes;
    }

    public void crossover() {

    }

    public void mutation(){

    }




}