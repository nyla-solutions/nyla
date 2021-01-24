package nyla.solutions.core.patterns.machineLearning.apriori;


//import nyla.solutions.core.patterns.iteration.Iterate;
import nyla.solutions.core.patterns.iteration.Iterate;
import nyla.solutions.core.patterns.observer.SubjectObserver;

import java.util.*;

public class AssociationProbabilies
{
    private final double minSup;
    private final int numTransactions;
    private final double confidence;
    private List<int[]> itemsets ;
    private final SubjectObserver<ProductAprioriAssociation> observer;
    private Map<Integer,Set<String>> pivot = new HashMap<>();
    private Map<String,ProductAprioriAssociation> associateMap = new HashMap<>();

    public AssociationProbabilies(double minimum,
                                  int numTransactions,
                                  double confidence,
                                  SubjectObserver<ProductAprioriAssociation> output_schema)
    {
        this.minSup = minimum;
        this.numTransactions = numTransactions;
        this.confidence = confidence;
        this.observer = output_schema;

    }

    public void learn(Iterate<ProductAprioriOrder> input_table)
    {
        Map<String,Integer> count = new HashMap<>();
        Set<String> frequentCandidates = new HashSet<>();
        ProductAprioriOrder order = null;


        Set<String> set = null;
        while ((order = input_table.next()) != null)
        {
            set = pivot.get(order.getOrderid());
            if(set == null)
                set = new HashSet<>();
            set.add(order.getProductname());

            pivot.put(order.getOrderid(),set);

            Integer productCount = count.get(order.getProductname());
            if(productCount == null) {
                   productCount = 0;
            }

            // if the count% is larger than the minSup%, add to the candidate to
            // the frequent candidates

            double productProbaliity = (productCount / (double) (numTransactions));
            if (productProbaliity >= minSup) {
                frequentCandidates.add(order.getProductname());
            }

            count.put(order.getProductname(),productCount + 1);
        }

        for (String favorite: frequentCandidates)
        {
            //loop pivot
            for(Map.Entry<Integer,Set<String>> entry: pivot.entrySet())
            {
                if(entry.getValue().contains(favorite))
                {
                    countAssociates(favorite,entry.getValue());
                }
            }
        }
    }

    protected void countAssociates(String favorite, Set<String> value)
    {
        ProductAprioriAssociation productAprioriAssociation = associateMap.get(favorite);

        if(productAprioriAssociation == null)
            productAprioriAssociation = new ProductAprioriAssociation(favorite);

        associateMap.put(favorite,productAprioriAssociation);

        for (String associate: value) {
            productAprioriAssociation.addAssociate(associate);


            int cnt = whatIsAssocCnt(favorite,associate);

            double associatePercent= cnt/((double)numTransactions);

            if(associatePercent >= confidence)
                foundFrequentItemSet(productAprioriAssociation.filterAssociate(associate));

        }

        associateMap.put(favorite,productAprioriAssociation);
    }

    private void foundFrequentItemSet(ProductAprioriAssociation order) {

        this.observer.update(order.getProductName(), order);
    }


    protected int whatIsAssocCnt(String favorite, String associated)
    {
        ProductAprioriAssociation out = associateMap.get(favorite);
        if(out == null)
            return 0;

        Integer cnt = out.countAssociate(associated);
        return cnt != null? cnt : 0;
    }
}
