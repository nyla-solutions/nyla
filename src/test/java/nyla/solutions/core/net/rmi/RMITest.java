package nyla.solutions.core.net.rmi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RMITest
{
    Registry registry;
    RMI subject;

    @BeforeEach
    public void setUp()
    {
        registry = mock(Registry.class);
        subject = new RMI("",2020,registry);
    }

    @Test
    void lookup()
    throws RemoteException, NotBoundException
    {
        subject.lookup("test");
        verify(registry).lookup(anyString());
    }



    @Test
    void list()
    throws RemoteException
    {
        subject.list();
        verify(registry).list();
    }

}