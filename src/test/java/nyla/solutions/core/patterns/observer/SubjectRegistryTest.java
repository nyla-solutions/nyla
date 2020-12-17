package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectRegistryTest
{
    private String subjectName = "hello";
    private String observerId = "id";
    private SubjectObserver<UserProfile> observer;
    private SubjectRegistry subject;

    @BeforeEach
    void setUp()
    {
        observer = mock(SubjectObserver.class);
        when(observer.getId()).thenReturn(observerId);
        subject = new SubjectRegistry();
    }

    @Test
    void when_register_then_Notify_observerUpdate_Called()
    {
        subject.register(subjectName,observer);

        UserProfile userProfile = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class)
                .randomizeAll().create();

        subject.notify(subjectName,userProfile);
        verify(observer).update(subjectName,userProfile);
    }


    @Test
    void removeRegistration()
    {
        subject.register(subjectName,observer);
        assertTrue(subject.getRegistry().containsKey(subjectName));

        UserProfile userProfile = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class)
                .randomizeAll().create();

        subject.notify(subjectName,userProfile);
        verify(observer,atMostOnce()).update(subjectName,userProfile);

        subject.removeRegistration(subjectName,observer);


        subject.notify(subjectName,userProfile);
        verify(observer,atMostOnce()).update(subjectName,userProfile);

    }

    @Test
    void getRegistry()
    {
        assertFalse(subject.getRegistry().containsKey(subjectName));
        subject.register(subjectName,observer);
        assertTrue(subject.getRegistry().containsKey(subjectName));
    }

    @Test
    void setSubjectObservers()
    {
        Map<Object, SubjectObserver> map = new HashMap<>();
        subject.setSubjectObservers(map);
        assertEquals(map,subject.getRegistry());
    }
}