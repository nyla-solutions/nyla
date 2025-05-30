package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

import java.io.IOException;
import java.net.ServerSocket;

import static nyla.solutions.core.util.Config.settings;

/**
 *  java -classpath build/libs/nyla.solutions.core-2.0.1-SNAPSHOT.jar nyla.solutions.core.patterns.loadbalancer.NetworkLoadBalancer --NETWORK_GATEWAYS=www.g-solutions.net:80 --SERVER_PORT=80
 */
public class NetworkLoadBalancer implements Runnable{
    private final int port;
    private final RoundRobin<NetworkGateway> roundRobin;

    public NetworkLoadBalancer(int port, RoundRobin<NetworkGateway> roundRobin) {
        this.port = port;
        this.roundRobin = roundRobin;
    }

    public void run() {
        Debugger.println(this,"Starting to listen on port: "+port);

        while(true)
        {
            try(ServerSocket socket = new ServerSocket(port))
            {
                roundRobin.next().join(socket.accept());

            } catch (IOException e) {
                Debugger.printError(e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Config.loadArgs(args);

        RoundRobin<NetworkGateway> rr = new RoundRobin<>(
                NetworkGateway.builder().commaSeparatedHostPort(settings().getProperty("NETWORK_GATEWAYS"),
                        settings().getPropertyInteger("SOCKET_BUFFER_SIZE",32000)).buildArray());

        int serverPort = settings().getPropertyInteger("SERVER_PORT");

        NetworkLoadBalancer lb = new NetworkLoadBalancer(serverPort,rr);

        lb.run();
    }
}
