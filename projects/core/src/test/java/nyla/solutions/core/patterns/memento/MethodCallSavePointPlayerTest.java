package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.data.MethodCallFact;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MethodCallSavePointPlayerTest
{

    @Test
    void playMethodCalls()
    {
        UserProfile userProfile = new UserProfile();
        MethodCallSavePointPlayer<UserProfile> player = new MethodCallSavePointPlayer<>(userProfile);

        MethodCallFact methodCallFact = new MethodCallFact();
        String expected = "hello";

        methodCallFact.setArguments(expected);
        methodCallFact.setCallerName(this.getClass().getSimpleName());
        methodCallFact.setMethodName("setEmail");


        Memento memento = mock(Memento.class);
        when(memento.restore(anyString(),any(Class.class))).thenReturn(methodCallFact);
        String savePoint ="start";
        player.record(savePoint,methodCallFact);
        player.playMethodCalls(memento,savePoint);


        UserProfile actual = player.getTarget();
        assertEquals(userProfile,actual);
        System.out.println(actual);
        assertEquals(expected,actual.getEmail());

    }

    @Test
    void getTarget()
    {
        UserProfile userProfile = new UserProfile();
        MethodCallSavePointPlayer player = new MethodCallSavePointPlayer(userProfile);
        assertEquals(userProfile,player.getTarget());
    }

    @Test
    void getMethodCallObjectPreparer()
    {
        UserProfile userProfile = new UserProfile();
        MethodCallSavePointPlayer player = new MethodCallSavePointPlayer(userProfile);

        MethodCallObjectPreparer preparer = mock(MethodCallObjectPreparer.class);
        player.setMethodCallObjectPreparer(preparer);
        assertNotNull(player.getTarget());

    }

}