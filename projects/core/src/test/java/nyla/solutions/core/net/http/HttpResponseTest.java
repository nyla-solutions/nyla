package nyla.solutions.core.net.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest
{
    @Test
    void isOk_When_200_Then_True()
    {
        int expectedCode = 200;
        String expectedResponse = "{}";

        assertTrue(new HttpResponse(expectedCode,expectedResponse).isOk());
    }
    @Test
    void isOk_When_not_200_Then_False()
    {
        int expectedCode = 500;
        String expectedResponse = "{}";

        assertFalse(new HttpResponse(expectedCode,expectedResponse).isOk());
    }

    @Test
    void getStatusCode()
    {
        int expectedCode = 200;
        String expectedResponse = "{}";

        assertEquals(expectedCode, new HttpResponse(expectedCode,expectedResponse).getStatusCode());
    }

    @Test
    void getBody()
    {
        int expectedCode = 200;
        String expectedResponse = "{}";

        assertEquals(expectedResponse, new HttpResponse(expectedCode,expectedResponse).getBody());
    }

    @Test
    void test_toString()
    {
        int expectedCode = 200;
        String expectedResponse = "{}";
        HttpResponse subject = new HttpResponse(expectedCode,expectedResponse);

        assertThat(subject.toString()).contains(String.valueOf(expectedCode));
        assertThat(subject.toString()).contains(expectedResponse);

    }
}