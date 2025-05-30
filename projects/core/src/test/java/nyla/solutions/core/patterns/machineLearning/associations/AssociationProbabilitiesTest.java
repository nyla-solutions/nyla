package nyla.solutions.core.patterns.machineLearning.associations;

import nyla.solutions.core.patterns.iteration.Iterate;
import nyla.solutions.core.patterns.iteration.IterateIterator;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AssociationProbabilitiesTest
{
    //Apriori Algorithm
    private Double minimum = 0.2;
    private int numOfTransactions = 20;
    private String expectedProduct ="hotdogs";
    private String expectedAssoc1 = "buns";
    private String expectedAssoc2 = "ketchup";
    private String[] expectedAssociations = {expectedAssoc1,expectedAssoc2};
    private ProductAssociation expectedAssociation;
    private SubjectObserver<ProductAssociation> output_schema;
    private AssociationProbabilities subject;
    private List<ProductTransition> orders;
    private double confidence = 0.01;
    private String observerId = "hello";

    @BeforeEach
    void setUp()
    {
        expectedAssociation = new ProductAssociation(expectedProduct);

        output_schema = mock(SubjectObserver.class);
        when(output_schema.getId()).thenReturn(observerId);
         subject = new AssociationProbabilities(
                 minimum,
                numOfTransactions,
                 confidence, output_schema
        );

        orders = new ArrayList<>();

        for (int i = 0; i < numOfTransactions; i++) {

            if(i < 4)
            {
                if(i % 2  == 0)
                {
                    orders.add(new ProductTransition(1,expectedProduct));
                }
                else
                {
                    orders.add(new ProductTransition(1,expectedAssoc2));
                }
            }
            else if(i > 3)
            {
                if(i % 2  == 0)
                {
                    orders.add(new ProductTransition(2,expectedAssoc2));
                }
                else
                {
                    orders.add(new ProductTransition(2,expectedAssoc1));
                }
            }
        }
    }

    /*
        select  *  from  madlib.assoc_rules
    (.035,  .035,  'orderid',  'productname',  'order_items',  NULL,  TRUE, 2);

         */

    @Test
    void observers()
    {
        AssociationProbabilities subject = new AssociationProbabilities(minimum,numOfTransactions,
                confidence);

        assertDoesNotThrow( () -> subject.addObserver(output_schema));

    }

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
    void learnIterateIterator()
    {
        Iterate<ProductTransition> input_table = new IterateIterator<ProductTransition>(orders.iterator());
        subject.learn(input_table);
        verify(output_schema,atLeastOnce()).update(anyString(),any());

    }

    @Test
    void learn()
    {
        for (ProductTransition<Integer> order:orders) {
            subject.learn(order);
        }
        subject.notifyFavoriteAssociations();

        verify(output_schema,atLeastOnce()).update(anyString(),any());

    }
}