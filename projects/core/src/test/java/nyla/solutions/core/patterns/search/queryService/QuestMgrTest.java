package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.PagingCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gregory Green
 */
class QuestMgrTest
{
    private QuestFactory questFactory;
    private QuestFinder finder;
    private QuestMgr subject;
    private QuestCriteria criteria;

    @BeforeEach
    void setUp()
    {
        questFactory = mock(QuestFactory.class);
        finder = mock(QuestFinder.class);

        when(questFactory.createFinder(any(),any())).thenReturn(finder);
        criteria = new QuestCriteria();
        subject = new QuestMgr(questFactory);


    }

    @Test
    void search_throwsRequired_whenNoDataSource_andQuestName()
    {
        assertThrows(RequiredException.class, () -> subject.search(criteria));
    }

    @Test
    void search_throwsRequired_when_Questname()
    {
        assertThrows(RequiredException.class, () -> subject.search(criteria));
        criteria.setDataSources("test");
        assertThrows(RequiredException.class, () -> subject.search(criteria));
    }

    @Test
    void search() throws Exception
    {
        criteria.setDataSources("test");
        criteria.setQuestName("questName");

        PageCriteria pageCriteria = new PageCriteria();
        pageCriteria.setSize(1);
        criteria.setPageCriteria(pageCriteria);

        DataRow expected = new DataRow();
        Paging<DataRow> expectedResults = mock(Paging.class);

        when(finder.call()).thenReturn(expectedResults);
        Paging<DataRow> actual = subject.search(criteria);
        assertNotNull(actual);
    }

    @Test
    void count()
    {
        final long expected = 3;
        Pagination pagination = mock(Pagination.class);
        when(pagination.count(any())).thenReturn(expected);
        when(questFactory.getPagination(any())).thenReturn(pagination);
        PageCriteria pageCriteria = new PageCriteria();
        long actual = subject.count(pageCriteria);
        assertEquals(expected,actual);

    }

    @Test
    void getPaging()
    {

        PageCriteria pageCriteria = new PageCriteria();
        String expectedValue = "e";
        Collection<String> collection = Collections.singleton(expectedValue);
        Paging expected = new PagingCollection(collection,pageCriteria);
        Pagination expectedPagination = mock(Pagination.class);
        when(questFactory.getPagination(any())).thenReturn(expectedPagination);
        when(expectedPagination.getPaging(pageCriteria)).thenReturn(expected);
        Paging actual = subject.getPaging(pageCriteria);

        assertEquals(expected,actual);

    }
}