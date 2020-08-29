package nyla.solutions.core.net.http;

import nyla.solutions.core.exception.CommunicationException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Http
{
    private String contentType = "text/plain";
    private int timeoutSecs = 30;
    private String user;
    private char[] password;

    public HttpResponse post(final URL url, String body) throws IOException
    {
        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
        urlConn.setRequestMethod("POST");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", contentType);

        constructCredentials(urlConn);

        try {
            DataOutputStream printout = null;

            printout = new DataOutputStream(urlConn.getOutputStream());

            printout.write(body.getBytes(IO.CHARSET));
            printout.flush();

            printout.close();


            TimeoutThread timeoutThread = new TimeoutThread( urlConn);

            timeoutThread.start();

            timeoutThread.join(timeoutSecs * 1000);
            timeoutThread.interrupt();


            return timeoutThread.response;
        }
        catch (InterruptedException e) {
           throw new CommunicationException(e);
        }


    }

    private void constructCredentials(HttpURLConnection urlConn)
    {
        if (user != null && user.length() > 0) {
            urlConn.setRequestProperty("Authorization", "Basic " + Text.encodeBase64(
                    new StringBuilder().append(user).append(":").append(
                            password).toString()));
        }
    }

    public HttpResponse get(URL url) throws IOException
    {

        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("GET");
        constructCredentials(urlConn);
        int status = urlConn.getResponseCode();
        final String  body;

        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream())))
        {
            body = IO.readText(in);
        }
        return new HttpResponse(status,body);
    }

    class TimeoutThread extends Thread
    {
        private HttpURLConnection urlConn = null;
        private HttpResponse response;

        public TimeoutThread(HttpURLConnection urlConn)
        {
            this.urlConn = urlConn;
        }

        public void run()
        {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream(), IO.CHARSET))) {
                String body = IO.readText(reader);

                int response_code = urlConn.getResponseCode();

                response = new HttpResponse(response_code, body);
            }
            catch (Exception e) {

                try {
                    response = processError(e);
                }
                catch (IOException ioException) {
                    throw new CommunicationException(ioException);
                }
            }

        }

        HttpResponse processError(Exception e) throws IOException
        {
            int response_code = 500;
            String errorBody = null;

            response_code = urlConn.getResponseCode();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConn.getErrorStream(), IO.CHARSET)))
            {
                errorBody = IO.readText(reader);
            }


            return new HttpResponse(response_code, errorBody);
        }
    }//--------------------------------------------

}
