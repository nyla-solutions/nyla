package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicTest
{
    private String topicName = "topicName";
    private Topic<UserProfile> subject;
    private SubjectObserver observer;
    private UserProfile userProfile;
    private String id;

    @BeforeEach
    void setUp()
    {
        id = Text.generator().generateId();

        userProfile = new UserProfile();
        userProfile.setId(id);
        subject = new Topic(topicName);
        observer = mock(SubjectTimerObserver.class);
        when(observer.getId()).thenReturn(id);
    }

    @Test
    void add()
    {
        subject.add(observer);
        subject.notify(userProfile);

        verify(observer).update(topicName,userProfile);
    }

    @Test
    void add_when_observer_null_Then_ThrowsRequiredException()
    {
        assertThrows(RequiredException.class, ()->  subject.add(null));
    }

    @Test
    void add_when_observer_id_isEmpty_Then_ThrowsRequiredException()
    {
        when(observer.getId()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, ()->  subject.add(observer));

        when(observer.getId()).thenReturn("");
        assertThrows(IllegalArgumentException.class, ()->  subject.add(observer));


        when(observer.getId()).thenReturn(id);
        subject.add(observer);
    }


    @Test
    void remove_null_does_Not()
    {
        subject.add(observer);
        subject.remove(null);
        assertEquals(1,subject.getObserverMap().size());
    }

    @Test
    void remove()
    {
        subject.add(observer);
        subject.notify(userProfile);

        verify(observer,atMostOnce()).update(topicName,userProfile);

        subject.remove(observer);
        subject.notify(userProfile);
        verify(observer,atMostOnce()).update(topicName,userProfile);
    }

    @Test
    void getName()
    {
        assertEquals(topicName,subject.getName());
    }


    @Test
    void compareTo()
    {
        assertTrue(new Topic("A").compareTo(new Topic("B")) < 0);
        assertTrue(new Topic("B").compareTo(new Topic("A")) > 0);
        assertTrue(new Topic("C").compareTo(new Topic("C")) == 0);
    }

    @Test
    void copy()
    {
        Topic<UserProfile> copy = new Topic<UserProfile>(topicName);
        copy.copy(subject);

        assertEquals(subject.getName(),copy.getName());
        assertEquals(subject.getObserverMap(),copy.getObserverMap());


    }

    @Test
    void testHashCode()
    {
        Topic other = new Topic(subject.getName());

        assertEquals(subject.hashCode(),other.hashCode());
        subject.add(observer);
        assertNotEquals(subject.hashCode(),other.hashCode());

        other.add(observer);
        assertEquals(subject.hashCode(),other.hashCode());
    }

    @Test
    void testEquals()
    {
        Topic other = new Topic(subject.getName());

        assertTrue(subject.equals(other));
        subject.add(observer);
        assertFalse(subject.equals(other));

        other.add(observer);
        assertTrue(subject.equals(other));
    }

    @Test
    void testToString()
    {
        String text = subject.toString();
        assertTrue(text.contains(topicName));

    }
}