package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.DataRowCreator;
import nyla.solutions.core.patterns.creational.RowObjectCreator;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestDirectorTest {
    private QuestDirector subject;
    @Mock
    private Pagination pagination;


    @Mock
    private DataRowCreator visitor;

    @Mock
    private QuestCriteria criteria;
    @Mock
    private PageCriteria pageCriteria;
    private UserProfile user1;
    private UserProfile user2;

    @BeforeEach
    void setUp() {
        user1 = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        user2 = JavaBeanGeneratorCreator.of(UserProfile.class).create();

        subject = new QuestDirector(pagination);
    }

    @Test
    void constructDataRows() {

        var results = List.of(user1,user2);
        Integer expectSize = 1;
        when(criteria.getPageCriteria()).thenReturn(pageCriteria);
        when(pageCriteria.getSize()).thenReturn(expectSize);


        var actual = subject.constructDataRows(results.iterator(), criteria, visitor);

        assertThat(actual).isNotNull();
        assertThat(actual).isNotEmpty();

//        verify(pagination).constructPaging(ArgumentMatchers.<Iterator>any(Iterator.class),
//                any(PageCriteria.class),
//                any(RowObjectCreator.class));

    }
}