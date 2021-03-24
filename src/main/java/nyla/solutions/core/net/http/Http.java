package nyla.solutions.core.net.http;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Gregory Green
 */
public class Http
{
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    public static final String DELETE_METHOD = "DELETE";
    private static final String PUT_METHOD = "PUT";

    private String contentType = "text/plain";
    private String user;
    private char[] password;
    private int readTimeoutSecs = 60*3;
    private int connectTimeoutSecs = 60*3;


    public HttpResponse put(URL url, String body) throws IOException
    {
        HttpURLConnection urlConn = constructRequest(PUT_METHOD,url, body);
        return readResponse(urlConn);
    }
    public HttpResponse post(final URL url, String body) throws IOException
    {
        HttpURLConnection urlConn = constructRequest(POST_METHOD,url, body);

        return readResponse(urlConn);
    }

    private HttpURLConnection constructRequest(String method, URL url, String body) throws IOException
    {
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setReadTimeout(readTimeoutSecs*1000);
        urlConn.setConnectTimeout(connectTimeoutSecs*1000);
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestMethod(method);
        urlConn.setRequestProperty("Content-Type", contentType);

        try(DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream())) {
            printout.write(body.getBytes(IO.CHARSET));
            printout.flush();
        }
        return urlConn;
    }

    public HttpResponse delete(URL url) throws IOException
    {
        return readResponse(DELETE_METHOD, url);
    }

    public HttpResponse get(URL url) throws IOException
    {
        return readResponse(GET_METHOD, url);
    }

    private HttpResponse readResponse(String method, URL url) throws IOException
    {
        return readResponse(method, (HttpURLConnection) url.openConnection());

    }

    private HttpResponse readResponse(String method, HttpURLConnection urlConn) throws IOException
    {
        urlConn.setRequestMethod(method);
        return readResponse(urlConn);
    }

    private HttpResponse readResponse(HttpURLConnection urlConn) throws IOException
    {
        constructCredentials(urlConn);

        int status = urlConn.getResponseCode();
        final String body;

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream()))) {
            body = IO.readText(in);
        }
        return new HttpResponse(status, body);
    }


    private void constructCredentials(HttpURLConnection urlConn)
    {
        if (user != null && user.length() > 0) {
            urlConn.setRequestProperty("Authorization", "Basic " + Text.encodeBase64(
                    new StringBuilder().append(user).append(":").append(
                            password).toString()));
        }
    }

}
