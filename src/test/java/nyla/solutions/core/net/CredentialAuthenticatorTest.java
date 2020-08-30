package nyla.solutions.core.net;

import nyla.solutions.core.exception.RequiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CredentialAuthenticatorTest
{
    @Test
    void getPasswordAuthentication_nousername()
    {
        assertThrows(RequiredException.class,
                () ->
                new CredentialAuthenticator(null,"world".toCharArray()));
    }

    @Test
    void getPasswordAuthentication_no_password()
    {
        assertThrows(RequiredException.class,
                () ->
                        new CredentialAuthenticator("hello",null));
    }
    @Test
    void getPasswordAuthentication_username()
    {
        assertEquals("hello",
                new CredentialAuthenticator("hello","world".toCharArray())
                        .getPasswordAuthentication().getUserName());
    }
    @Test
    void getPasswordAuthentication_passwword()
    {
        assertEquals("world",
                String.valueOf(new CredentialAuthenticator("hello","world".toCharArray())
                        .getPasswordAuthentication().getPassword()));
    }
}