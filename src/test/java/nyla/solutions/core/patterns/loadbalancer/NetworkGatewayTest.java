package nyla.solutions.core.patterns.loadbalancer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NetworkGatewayTest {

    private Socket targetSocket;
    private Socket sourceSocket;
    private int bufferSize = 124;

    private String host = null;
    private int port = 0;

    @BeforeEach
    void setUp() {
        targetSocket = mock(Socket.class);
        sourceSocket = mock(Socket.class);
    }

    @Test
    void given_host_and_port_When_run_Then_inputSendToOutputs() throws IOException {

        byte[] expected = "HelloWorld".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream sourceInput = new ByteArrayInputStream(expected);
        ByteArrayOutputStream sourceOutput = new ByteArrayOutputStream();

        ByteArrayInputStream targetInput = new ByteArrayInputStream(expected);
        ByteArrayOutputStream targetOutput = new ByteArrayOutputStream();

        when(sourceSocket.getInputStream()).thenReturn(sourceInput);
        when(sourceSocket.getOutputStream()).thenReturn(sourceOutput);
        when(targetSocket.getOutputStream()).thenReturn(targetOutput);
        when(targetSocket.getInputStream()).thenReturn(targetInput);

        var subject = new NetworkGateway(targetSocket,bufferSize);

        subject.join(sourceSocket);


        verify(sourceSocket).getInputStream();
        verify(sourceSocket).getOutputStream();

        verify(targetSocket).getInputStream();
        verify(targetSocket).getOutputStream();

    }

    @Test
    void builder() throws IOException {
        NetworkGateway[] gateways = NetworkGateway.builder().commaSeparatedHostPort("TheRevelationSquad.com:80,cnn.com:80",1024).buildArray();

        assertThat(gateways).isNotNull().hasSize(2);

    }
}