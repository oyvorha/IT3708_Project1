import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GeneticAlgorithm {

    private float mutationRate;
    private float crossoverRate;
    private int populationSize;
    private ArrayList<Chromosome> rankedChromosomes;

    public GeneticAlgorithm(float mutationRate, float crossoverRate, int populationSize, ArrayList<Chromosome> chromosomes) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.populationSize = populationSize;
        this.rankedChromosomes = rankChromosomes(chromosomes);
    }

    private ArrayList<Chromosome> rankChromosomes(ArrayList<Chromosome> chromosomes) {
        Collections.sort(chromosomes, new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                if (o1.getTotalDistance() == o2.getTotalDistance()) {
                    return 0;
                }
                else {
                    return (o1.getTotalDistance() <= o2.getTotalDistance()) ? -1 : 1;
                }
            }
        });
        chromosomes.subList(this.populationSize, chromosomes.size()-1).clear();
        return chromosomes;
    }

    public void crossover() {

    }




}
