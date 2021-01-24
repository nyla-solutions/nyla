package nyla.solutions.core.patterns.machineLearning.apriori;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductAprioriAssociationTest
{
    @Test
    void add()
    {
        String expected = "hello";
        ProductAprioriAssociation subject = new ProductAprioriAssociation(expected);
        subject.addAssociate("hotdog");

        assertEquals(1,subject.countAssociate("hotdog"));
        subject.addAssociate("hotdog");
        assertEquals(2,subject.countAssociate("hotdog"));

    }

    @Test
    void getProductName()
    {
        String expected = "hi";
        assertEquals(expected,new ProductAprioriAssociation(expected).getProductName());
    }

    @Test
    void filterAssociate()
    {
        String expected = "hello";
        ProductAprioriAssociation subject = new ProductAprioriAssociation(expected);
        String expectedAssociate = "world";
        ProductAprioriAssociation actual = subject.filterAssociate(expectedAssociate);
        assertNotNull(actual);
        assertEquals(expected,actual.getProductName());
        assertEquals(0,actual.countAssociate(expectedAssociate));
        subject.addAssociate(expectedAssociate);
        actual = subject.filterAssociate(expectedAssociate);
        assertEquals(1,actual.countAssociate(expectedAssociate));
    }
}