package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class QuestFactoryTest
{
    private QuestFactory subject;
    private ServiceFactory serviceFactory;
    private Pagination pagination;
    @BeforeEach
    void setUp()
    {
        pagination= mock(Pagination.class);
        serviceFactory = mock(ServiceFactory.class);
        when(serviceFactory.create(any(Class.class))).thenReturn(pagination);

        subject = new QuestFactory(serviceFactory);

    }

    @Test
    void createExecutorBoss()
    {
        assertNotNull(QuestFactory.createExecutorBoss());
    }

    @Test
    void getInstance()
    {
        assertNotNull(subject);
    }

    @Test
    void createFinder()
    {
        QuestCriteria criteria = new JavaBeanGeneratorCreator<QuestCriteria>(QuestCriteria.class)
                .randomizeAll().create();

        QuestFinder finder = mock(QuestFinder.class);
        when(serviceFactory.create(anyString())).thenReturn(finder);

        String dataSource = "datasource";
        assertNotNull(subject.createFinder(criteria,dataSource));
    }

    @Test
    void createQuestService()
    {
        assertNotNull(subject.createQuestService());
    }

    @Test
    void createComparator()
    {
        Comparator<DataRow> comparable = mock(Comparator.class);
        when(serviceFactory.create(anyString())).thenReturn(comparable);

        assertNotNull(subject.createComparator(anyString()));
    }

    @Test
    void createBooleanExpression()
    {
        BooleanExpression<DataRow> boolExp = mock(BooleanExpression.class);
        when(serviceFactory.create(anyString())).thenReturn(boolExp);
        assertNotNull(subject.createBooleanExpression(anyString()));
    }

    @Test
    void getPagination()
    {
        when(pagination.getPaginationById(anyString())).thenReturn(pagination);
        PageCriteria pageCriteria = new PageCriteria();
        pageCriteria.setId("id");
        assertNotNull(subject.getPagination(pageCriteria));
    }
}