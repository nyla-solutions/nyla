package nyla.solutions.core.net.http;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class HttpTest
{

    @Test
    void delete() throws IOException
    {
        Http http = new Http();
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        assertThrows(IOException.class, () -> http.delete(url));

    }

    @Test
    void put() throws IOException
    {
        Http http = new Http();
        String location = "http://www.TheRevelationSquad.com";
        URL url = new URL(location);

        String body = "{}";
        assertThrows(IOException.class, () -> http.put(url,body));

    }

    @Nested
    class WhenGet
    {

        @Test
        void get() throws IOException
        {

            Http http = new Http();
            String location = "http://www.TheRevelationSquad.com";
            URL url = new URL(location);

            HttpResponse response = http.get(url);
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
            Http http = new Http();
            String location = "http://www.TheRevelationSquad.com";
            URL url = new URL(location);
            String body = "{}";
            HttpResponse response =  http.post(url,body);
            System.out.println(response);
            assertNotNull(response);
            assertTrue(response.isOk());
        }

        @Test
        void post_throwsExcetion() throws IOException
        {
            Http http = new Http();
            String location = "http://localhost:23232";
            URL url = new URL(location);
            String body = "{}";
            assertThrows(IOException.class, () -> http.post(url,body));

        }
    }

}