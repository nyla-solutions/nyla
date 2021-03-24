// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.geocities.com/kpdus/jad.html

// Decompiler options: packimports(3) 

// Source File Name:   JXCallbackHandler.java



package nyla.solutions.core.ds;


import javax.security.auth.callback.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;



public class JXCallbackHandler

    implements CallbackHandler

{



    public static void setupGUI(Frame ownerFrame, String userNamePromptString, String pwdPromptString, String promptHeaderString)

    {

        owner = ownerFrame;

        promptHeader = promptHeaderString;

    }



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

            } else

            if(callbacks[i] instanceof PasswordCallback)

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

        String result = JOptionPane.showInputDialog(owner, prompt, promptHeader, 3);

        return result;

    }



    private char[] getPassword(String prompt)

    {

        JPasswordField pwd = new JPasswordField();

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(prompt), "North");

        panel.add(pwd);

        JOptionPane.showMessageDialog(owner, panel, promptHeader, 3);

        return pwd.getPassword();

    }



    public static void main(String args[])

    {

        JXCallbackHandler test = new JXCallbackHandler();

        System.out.println("user name = " + test.getUserName("a nice shiny user name"));

        System.out.println("pwd = " + new String(test.getPassword("a nice shiny password")));

        System.exit(0);

    }



    private static Frame owner = null;

    private static String promptHeader = "Require Kerberos Credentials";



}

