package nyla.solutions.core.net.http;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import static org.junit.jupiter.api.Assertions.*;

class HttpTest
{
    private Http subject = new Http();

    @Test
    void delete() throws IOException
    {
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        assertThrows(IOException.class, () -> subject.delete(url));

    }

    @Test
    void put() throws IOException
    {
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        String body = "{}";
        assertThrows(IOException.class, () -> subject.put(url,body));

    }

    @Test
    void setHeader()
    {
        subject.setHeader("Authorization","TEST");
        assertEquals("TEST",subject.getHeader("Authorization"));
    }

    void exampleGettingDataFromTwitter() throws IOException
    {
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
        void get() throws IOException
        {

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
        void post() throws IOException
        {
            String location = "http://www.TheRevelationSquad.com";
            URL url = new URL(location);
            String body = "{}";
            HttpResponse response =  subject.post(url,body);
            System.out.println(response);
            assertNotNull(response);
            assertTrue(response.isOk());
        }

        @Test
        void post_throwsExcetion() throws IOException
        {
            String location = "http://localhost:23232";
            URL url = new URL(location);
            String body = "{}";
            assertThrows(IOException.class, () -> subject.post(url,body));

        }
    }

}