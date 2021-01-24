package nyla.solutions.core.patterns.machineLearning.apriori;

import nyla.solutions.core.patterns.iteration.Iterate;
import nyla.solutions.core.patterns.iteration.IterateIterator;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AssociationProbabiliesTest
{
    //Apriori Algorithm
    private Double 	support = 0.2;
    private int numOfTransactions = 20;
    private String expectedProduct ="hotdogs";
    private String expectedAssoc1 = "buns";
    private String expectedAssoc2 = "ketchup";
    private String[] expectedAssociations = {expectedAssoc1,expectedAssoc2};
    private ProductAprioriAssociation expectedAssociation;
    private SubjectObserver<ProductAprioriAssociation> output_schema;
    private AssociationProbabilies subject;
    private List<ProductAprioriOrder> orders;
    private double confidence = 0.01;

    @BeforeEach
    void setUp()
    {
        expectedAssociation = new ProductAprioriAssociation(expectedProduct);

        output_schema = mock(SubjectObserver.class);
         subject = new AssociationProbabilies(
                support,
                numOfTransactions,
                 confidence, output_schema
        );

        orders = new ArrayList<>();

        for (int i = 0; i < numOfTransactions; i++) {

            if(i < 4)
            {
                if(i % 2  == 0)
                {
                    orders.add(new ProductAprioriOrder(1,expectedProduct));
                }
                else
                {
                    orders.add(new ProductAprioriOrder(1,expectedAssoc2));
                }
            }
            else if(i > 3)
            {
                if(i % 2  == 0)
                {
                    orders.add(new ProductAprioriOrder(2,expectedAssoc2));
                }
                else
                {
                    orders.add(new ProductAprioriOrder(2,expectedAssoc1));
                }
            }
        }
    }

    /*
        select  *  from  madlib.assoc_rules
    (.035,  .035,  'orderid',  'productname',  'order_items',  NULL,  TRUE, 2);

         */


    @Test
    void whatIsAssocCnt()
    {
        String favorite = "hotdog";
        Set<String> set = Organizer.toSet("buns","ketchup");
        subject.countAssociates(favorite,set);
        assertEquals(1,subject.whatIsAssocCnt(favorite,"buns"));

        assertEquals(0,subject.whatIsAssocCnt(favorite,"none"));
        assertEquals(0,subject.whatIsAssocCnt("none","buns"));
    }

    @Test
    void learn()
    {
        Iterate<ProductAprioriOrder> input_table = new IterateIterator<ProductAprioriOrder>(orders.iterator());
        subject.learn(input_table);
        verify(output_schema,atLeastOnce()).update(anyString(),any());

    }
}