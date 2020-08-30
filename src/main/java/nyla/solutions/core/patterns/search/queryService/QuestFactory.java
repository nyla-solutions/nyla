package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.patterns.creational.servicefactory.ConfigServiceFactory;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.patterns.workthread.ExecutorBoss;
import nyla.solutions.core.util.Config;

import java.util.Comparator;

/**
 * Quest factory useding 
 * @author Gregory Green
 *
 */
public class QuestFactory
{
	private QuestFactory()
	{}
	
	/**
	 * 
	 * @return singleton executor boss (thread pool)
	 */
	public static ExecutorBoss createExecutorBoss()
	{
		synchronized(QuestFactory.class)
		{
			if(executorBoss == null)
				executorBoss = new ExecutorBoss(threadCount);
		}
		return executorBoss;
	}// --------------------------------------------------------
	/**
	 *
	 * @return singleton instance of the factory
	 */
	public static QuestFactory getInstance()
	{
		return questFactory;
	}// --------------------------------------------------------
	
	/**
	 * @param criteria the criteria
	 * @param dataSource data source
	 * @return quest finder instance
	 */
	public QuestFinder createFinder(QuestCriteria criteria, String dataSource)
	{
		try
		{
			QuestFinder finder =  ConfigServiceFactory.getConfigServiceFactoryInstance().create(dataSource);
			finder.assignCriteria(criteria,dataSource);
			
			return finder;
		}
		catch(ConfigException e)
		{
			throw new ConfigException("Connect create new instance "+QuestFinder.class.getName()+" "+e.getMessage(),e);
		}
	}// --------------------------------------------------------
	/**
	 * @param <T> the type class
	 * @param name the instance to compare
	 * @return the comparator by name
	 */
	public <T> Comparator<T> createComparator(String name)
	{
		Comparator<T> comparator = ConfigServiceFactory.getConfigServiceFactoryInstance().create(name);
		
		return comparator;
	}// --------------------------------------------------------
	/**
	 * @param <T> the class type
	 * @param name the boolean expression name (can  be the full classpath name)
	 * @return the created instance
	 */
	public <T> BooleanExpression<T> createBooleanExpression(String name)
	{
		BooleanExpression<T> expression = ConfigServiceFactory.getConfigServiceFactoryInstance().create(name);
		return expression;
	}// --------------------------------------------------------
	/**
	 * 
	 * @return new QuestMgr();
	 */
	public QuestService createQuestService()
	{
		return new QuestMgr();
	}
	private static final QuestFactory questFactory = new QuestFactory();
	private static ExecutorBoss executorBoss = null;
	private static int threadCount = Config.getPropertyInteger(QuestFactory.class,"threadCount");
}
