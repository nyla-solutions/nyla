package nyla.solutions.core.net.http;

public class HttpResponse
{
    private final int statusCode;
    private final String body;

    public HttpResponse(int statusCode, String body)
    {
        this.statusCode = statusCode;
        this.body = body;
    }

    public boolean isOk()
    {
        return 200 == statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getBody()
    {
        return body;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("HttpResponse{");
        sb.append("statusCode=").append(statusCode);
        sb.append(", body='").append(body).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
