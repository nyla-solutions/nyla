package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.decorator.TimeIntervalDecorator;
import nyla.solutions.core.security.user.data.User;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubjectTimerObserverTest
{
    private SubjectTimerObserver subject;

    @BeforeEach
    void setUp()
    {
        subject = new SubjectTimerObserver();
    }

    @Test
    void update_ifisStart_subjectName_Then_SetStart()
    {
        UserProfile data = new UserProfile();
        String expectedSubject = "hi";
        subject.setStartSubjectNamePattern("hi");
        subject.update(expectedSubject,data);
        assertEquals(data,subject.getStartData());
        assertNotNull(subject.getStartDate());
    }

    @Test
    void update_isEnd()
    {
        UserProfile data = new UserProfile();
        String expectedSubject = "hi";
        subject.setEndSubjectNamePattern("hi");
        subject.update(expectedSubject,data);
        assertEquals(data,subject.getEndData());
        assertNotNull(subject.getEndDate());
    }

    @Test
    void update_throwsSystemException()
    {
        UserProfile data = new UserProfile();
        String expectedSubject = "hi";
        assertThrows(SystemException.class,() -> subject.update(expectedSubject,data));

    }
    @Test
    void isStart()
    {
        subject.setStartSubjectNamePattern(".*");
        assertTrue(subject.isStart("ANy"));
    }
    @Test
    void isStart_false()
    {
        assertFalse(subject.isStart("ANy"));
        assertFalse(subject.isStart(null));
    }

    @Test
    void isEnd()
    {
        subject.setEndSubjectNamePattern(".*");
        assertTrue(subject.isEnd("ANy"));
    }
    @Test
    void isEnd_false()
    {
        assertFalse(subject.isEnd("ANy"));
        assertFalse(subject.isEnd(null));
    }

    @Test
    void when_set_Id_then_getId_Equals()
    {
        assertNotNull(subject.getId());
    }


    @Test
    void getStartSubjectNamePattern()
    {
        String expected = "hi";
        subject.setStartSubjectNamePattern(expected);
        assertEquals(expected,subject.getStartSubjectNamePattern());
    }

    @Test
    void getEndSubjectNamePattern()
    {
        String expected = "hi";
        subject.setEndSubjectNamePattern(expected);
        assertEquals(expected,subject.getEndSubjectNamePattern());
    }



    @Test
    void setDecorator_then_getDecorator_Matches()
    {
        TimeIntervalDecorator expected = new TimeIntervalDecorator()
        {
            @Override
            public void decorator(TimeInterval timeInterval)
            {

            }
        };
        subject.setDecorator(expected);
        assertEquals(expected,subject.getDecorator());
    }



    @Test
    void setStartDate()
    {
        LocalDateTime expected = LocalDateTime.now();
        subject.setStartDate(expected);
        assertEquals(expected,subject.getStartDate());
    }

    @Test
    void getEndDate()
    {
        LocalDateTime expected = LocalDateTime.now();
        subject.setEndDate(expected);
        assertEquals(expected,subject.getEndDate());
    }


    @Test
    void setStartData()
    {
        LocalDateTime expected = LocalDateTime.now();
        subject.setStartData(expected);
        assertEquals(expected,subject.getStartData());
    }

    @Test
    void getEndData()
    {
    }

    @Test
    void setEndData()
    {
        LocalDateTime expected = LocalDateTime.now();
        subject.setEndData(expected);
        assertEquals(expected,subject.getEndData());
    }
}