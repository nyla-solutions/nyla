package nyla.solutions.core.net.http;

import nyla.solutions.core.exception.IoException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class HttpTestIoException
{
    private Http subject = new Http();

    @Test
    void delete() throws MalformedURLException {
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        assertThrows(IoException.class, () -> subject.delete(url));

    }

    @Test
    void put() throws MalformedURLException {
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        String body = "{}";
        assertThrows(IoException.class, () -> subject.put(url,body));

    }

    @Test
    void setHeader()
    {
        subject.setHeader("Authorization","TEST");
        assertEquals("TEST",subject.getHeader("Authorization"));
    }

    void exampleGettingDataFromTwitter() throws IOException {
        subject.setHeader("Authorization","Bearer TODO");
        int cnt = 2;
        try(BufferedReader reader = subject.getWithReader(new URL("https://api.twitter.com/2/tweets/search/stream")))
        {
            for (int i = 0; i < cnt; i++) {
                System.out.println(reader.readLine());
            }
        }
    }

    @Nested
    class WhenGet
    {

        @Test
        void get() throws MalformedURLException {

            String location = "http://www.TheRevelationSquad.com";
            URL url = new URL(location);

            HttpResponse response = subject.get(url);
            System.out.println(response);
            assertNotNull(response);

        }
    }

    @Nested
    class WhenPost
    {
        @Test
        void post() throws MalformedURLException {
            String location = "http://www.TheRevelationSquad.com";
            URL url = new URL(location);
            String body = "{}";
            HttpResponse response =  subject.post(url,body);
            System.out.println(response);
            assertNotNull(response);
            assertTrue(response.isOk());
        }

        @Test
        void post_throwsException() throws MalformedURLException {
            String location = "http://localhost:23232";
            URL url = new URL(location);
            String body = "{}";
            assertThrows(IoException.class, () -> subject.post(url,body));

        }
    }

}