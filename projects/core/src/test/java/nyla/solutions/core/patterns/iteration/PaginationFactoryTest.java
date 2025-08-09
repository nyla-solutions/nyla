package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaginationFactoryTest
{
    private PaginationFactory subject;
    private ServiceFactory serviceFactory = mock(ServiceFactory.class);
    private Pagination pagination;

    @BeforeEach
    void setUp()
    {

        serviceFactory = mock(ServiceFactory.class);
        pagination = mock(Pagination.class);
        when(serviceFactory.create(any(Class.class))).thenReturn(pagination);
        subject = new PaginationFactory(serviceFactory);

    }

    @Test
    void createPagination()
    {

        Pagination actual = subject.createPagination();
        assertNotNull(actual);
    }
}