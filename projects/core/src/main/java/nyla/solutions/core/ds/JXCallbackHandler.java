package nyla.solutions.core.ds;

import nyla.solutions.core.util.Config;

import javax.security.auth.callback.*;
import java.awt.*;
import java.io.IOException;

public class JXCallbackHandler implements CallbackHandler
{
    private static Frame owner = null;
    private static String promptHeader = "Require Kerberos Credentials";


    public JXCallbackHandler()
    {
    }

    public void handle(Callback callbacks[])
        throws IOException, UnsupportedCallbackException
    {

        for(int i = 0; i < callbacks.length; i++)
            if(callbacks[i] instanceof NameCallback)
            {
                NameCallback cb = (NameCallback)callbacks[i];

                cb.setName(getUserName(cb.getPrompt()));

            } else  if(callbacks[i] instanceof PasswordCallback)
            {
                PasswordCallback cb = (PasswordCallback)callbacks[i];

                cb.setPassword(getPassword(cb.getPrompt()));

            } else
            {

                throw new UnsupportedCallbackException(callbacks[i]);
            }
    }

    private String getUserName(String prompt)
    {
        return Config.settings().getProperty(this.getClass(),"userName");
    }


    private char[] getPassword(String prompt)
    {
        return Config.settings().getPropertyPassword(this.getClass(),"password");
    }

}

