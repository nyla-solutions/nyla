package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.patterns.iteration.Pagination;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class QuestDirectorTest
{
    @Test
    void paginations()
    {
        Pagination pagination = mock(Pagination.class);
        QuestDirector subject = new QuestDirector(pagination);

    }
}