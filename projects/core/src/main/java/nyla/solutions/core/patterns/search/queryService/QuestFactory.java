package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.patterns.workthread.ExecutorBoss;

import java.util.Comparator;

import static nyla.solutions.core.util.Config.settings;

/**
 * Quest factory useding
 *
 * @author Gregory Green
 */
public class QuestFactory
{
    private static ExecutorBoss executorBoss = null;
    private static int threadCount = settings().getPropertyInteger("QUESTFACTORY_THREAD_CNT", 10);
    private final ServiceFactory serviceFactory;
    private final Pagination pagination;

    public QuestFactory(ServiceFactory serviceFactory)
    {
        this.serviceFactory = serviceFactory;
        this.pagination = this.serviceFactory.create(Pagination.class);

    }

    /**
     * @return singleton executor boss (thread pool)
     */
    public static ExecutorBoss createExecutorBoss()
    {
        synchronized (QuestFactory.class) {
            if (executorBoss == null)
                executorBoss = new ExecutorBoss(threadCount);
        }
        return executorBoss;
    }// --------------------------------------------------------

    /**
     * @return singleton instance of the factory
     */
    public static QuestFactory getInstance()
    {
        return new QuestFactory(ServiceFactory.getInstance());
    }// --------------------------------------------------------

    /**
     * @param criteria   the criteria
     * @param dataSource data source
     * @return quest finder instance
     */
    public QuestFinder createFinder(QuestCriteria criteria, String dataSource)
    {
        try {
            QuestFinder finder = serviceFactory.create(dataSource);
            finder.assignCriteria(criteria, dataSource);

            return finder;
        }
        catch (ConfigException e) {
            throw new ConfigException("Connect create new instance " + QuestFinder.class.getName() + " " + e.getMessage(), e);
        }
    }// --------------------------------------------------------


    /**
     * @return new QuestMgr();
     */
    public QuestService createQuestService()
    {
        return new QuestMgr(this);
    }

    public Comparator<DataRow> createComparator(String sorter)
    {
        return this.serviceFactory.create(sorter);
    }

    public BooleanExpression<DataRow> createBooleanExpression(String filter)
    {
        return this.serviceFactory.create(filter);
    }

    public Pagination getPagination(PageCriteria pageCriteria)
    {
        return this.pagination.getPaginationById(pageCriteria.getId());
    }
}
