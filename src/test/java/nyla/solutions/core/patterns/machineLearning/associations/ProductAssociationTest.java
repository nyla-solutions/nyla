package nyla.solutions.core.patterns.machineLearning.associations;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductAssociationTest
{
    private String expected = "hello";
    private ProductAssociation subject;

    @BeforeEach
    void setUp()
    {
        subject = new ProductAssociation(expected);
    }

    @Test
    void add()
    {
        subject.addAssociate("hotdog");

        assertEquals(1,subject.countAssociate("hotdog"));
        subject.addAssociate("hotdog");
        assertEquals(2,subject.countAssociate("hotdog"));

    }

    @Test
    void getProductName()
    {
        assertEquals(expected,new ProductAssociation(expected).getProductName());
    }

    @Test
    void getAssociateNames()
    {
        String expectedAssociate = "yo";
        subject.addAssociate(expectedAssociate);
        Set<String> expected = Organizer.toSet(expectedAssociate);
        Set<String> actual = subject.getAssociateNames();
        assertEquals(expected,actual);
    }

    @Test
    void filterAssociate()
    {
        String expectedAssociate = "world";
        ProductAssociation actual = subject.filterAssociate(expectedAssociate);
        assertNotNull(actual);
        assertEquals(expected,actual.getProductName());
        assertEquals(0,actual.countAssociate(expectedAssociate));
        subject.addAssociate(expectedAssociate);
        actual = subject.filterAssociate(expectedAssociate);
        assertEquals(1,actual.countAssociate(expectedAssociate));
    }
}