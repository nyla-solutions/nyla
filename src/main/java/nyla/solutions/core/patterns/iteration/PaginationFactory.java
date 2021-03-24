package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;

public class PaginationFactory
{
    private final ServiceFactory serviceFactory;

    public PaginationFactory(ServiceFactory serviceFactory)
    {
        this.serviceFactory = serviceFactory;
    }

    public Pagination createPagination()
    {
        return serviceFactory.create(Pagination.class);
    }
}
