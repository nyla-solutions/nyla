package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginCredentialTest
{

    @Test
    void password()
    {
        LoginCredential subject = new LoginCredential();
        String expected = "secret";
        subject.setPassword(expected);

        assertEquals(expected,String.valueOf(subject.getPassword()));

    }

    @Test
    void password_charArray()
    {
        LoginCredential subject = new LoginCredential();
        char[] expected = "secretArray".toCharArray();
        subject.setPassword(expected);

        assertEquals(String.valueOf(expected),String.valueOf(subject.getPassword()));
    }

    @Test
    void name()
    {

        LoginCredential subject = new LoginCredential();
        String expected = "myName";
        subject.setLoginID(expected);

        assertEquals(expected,subject.getName());
    }

    @Test
    void loginId()
    {

        LoginCredential subject = new LoginCredential();
        String expected = "loginId";
        subject.setLoginID(expected);

        assertEquals(expected,subject.getName());
    }

    @Test
    void domain()
    {

        LoginCredential subject = new LoginCredential();
        String expected = "domain";
        subject.setDomain(expected);

        assertEquals(expected,subject.getDomain());
    }
}