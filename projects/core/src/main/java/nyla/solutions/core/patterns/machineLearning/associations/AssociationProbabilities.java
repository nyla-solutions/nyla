package nyla.solutions.core.patterns.machineLearning.associations;


//import nyla.solutions.core.patterns.iteration.Iterate;

import nyla.solutions.core.patterns.iteration.Iterate;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.patterns.observer.Topic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AssociationProbabilities<K>
{
    private final double minSup;
    private final long numTransactions;
    private final double confidence;
    private final Topic<ProductAssociation> topic = new Topic<>("associations");
    private Map<K, Set<String>> pivot = new HashMap<>();
    private Map<String, ProductAssociation> associateMap = new HashMap<>();
    private Map<String, Integer> count = new HashMap<>();
    private Set<String> frequentCandidates = new HashSet<>();
    private Set<String> set = null;

    public AssociationProbabilities(double minimum,
                                    long numTransactions,
                                    double confidence,
                                    SubjectObserver<ProductAssociation> output_schema)
    {
        this(minimum, numTransactions,confidence);
        addObserver(output_schema);

    }

    public void addObserver(SubjectObserver<ProductAssociation> output_schema)
    {
        this.topic.add(output_schema);
    }

    public AssociationProbabilities(Double minimum, long numOfTransactions, double confidence)
    {
        this.minSup = minimum;
        this.numTransactions = numOfTransactions;
        this.confidence = confidence;
    }

    public void learn(ProductTransition<K> order)
    {
        set = pivot.get(order.getTransitionId());
        if (set == null)
            set = new HashSet<>();
        set.add(order.getProductName());

        pivot.put(order.getTransitionId(), set);

        Integer productCount = count.get(order.getProductName());
        if (productCount == null) {
            productCount = 0;
        }

        // if the count% is larger than the minSup%, add to the candidate to
        // the frequent candidates

        double productProbaliity = (productCount / (double) (numTransactions));
        if (productProbaliity >= minSup) {
            frequentCandidates.add(order.getProductName());
        }

        count.put(order.getProductName(), productCount + 1);
    }

    public void learn(Iterate<ProductTransition<K>> input_table)
    {
        ProductTransition<K> order = null;

        while ((order = input_table.next()) != null) {
            learn(order);
        }

        notifyFavoriteAssociations();
    }

    public void notifyFavoriteAssociations()
    {
        pivot.entrySet().parallelStream()
             .forEach( entry -> {
                 frequentCandidates.parallelStream().forEach( favorite -> {
                     if (entry.getValue().contains(favorite)) {
                         countAssociates(favorite, entry.getValue());
                     }
                 });
             });

    }

    protected void countAssociates(String favorite, Set<String> value)
    {
        ProductAssociation productAprioriAssociation = associateMap.get(favorite);

        if (productAprioriAssociation == null)
            productAprioriAssociation = new ProductAssociation(favorite);

        associateMap.put(favorite, productAprioriAssociation);

        for (String associate : value) {
            productAprioriAssociation.addAssociate(associate);


            int cnt = whatIsAssocCnt(favorite, associate);

            double associatePercent = cnt / ((double) numTransactions);

            if (associatePercent >= confidence)
                notifyFavoriteAssociations(productAprioriAssociation.filterAssociate(associate));

        }

        associateMap.put(favorite, productAprioriAssociation);
    }

    private void notifyFavoriteAssociations(ProductAssociation order)
    {

        this.topic.notify(order);
    }


    protected int whatIsAssocCnt(String favorite, String associated)
    {
        ProductAssociation out = associateMap.get(favorite);
        if (out == null)
            return 0;

        Integer cnt = out.countAssociate(associated);
        return cnt != null ? cnt : 0;
    }
}
