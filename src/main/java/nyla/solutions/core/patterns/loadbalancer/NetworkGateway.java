package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.util.Debugger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkGateway {

    private final Socket targetSocket;

    private final byte[] buffer;

    public NetworkGateway(String host, int port, int bufferSize) throws IOException {
        this(new Socket(host,port),bufferSize);

        Debugger.println(this,"HOST="+host+" port:"+80+" bufferSize:"+bufferSize);
    }

    public NetworkGateway(Socket targetSocket, int bufferSize) throws IOException {
        this.targetSocket = targetSocket;
        buffer = new byte[bufferSize];
    }


    public void join( Socket sourceSocket) {
        //request
        try(sourceSocket)
        {
            Thread requestThread = new Thread(() -> {
                try {
                    write(targetSocket.getOutputStream(),sourceSocket.getInputStream());
                } catch (IOException e) {
                    Debugger.printError(e);
                    throw new RuntimeException(e);
                }
            });


            //reply
            var replyThread = new Thread(() -> {
                try {
                    write(sourceSocket.getOutputStream(), targetSocket.getInputStream());
                } catch (IOException e) {
                    Debugger.printError(e);
                    throw new RuntimeException(e);
                }
            });

           requestThread.start();
           replyThread.start();

           requestThread.join();
           replyThread.join();

        } catch (IOException e) {
            Debugger.printError(e);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(OutputStream outputStream, InputStream inputStream)
            throws IOException
    {
        int count = 0;
        while (true) {
            count = inputStream.read(buffer);

            if(count <= 0)
            {
                break;
            }

           try{ outputStream.write(buffer,0,count);}
           catch (IndexOutOfBoundsException e)
           {
               Debugger.printError("count:"+count);
           }
        }
    }

    public static NetworkGatewayBuilder builder() {
        return new NetworkGatewayBuilder();
    }


    public static class NetworkGatewayBuilder
    {
        private NetworkGateway[] outputArray;

        private NetworkGatewayBuilder(){}

        public NetworkGatewayBuilder commaSeparatedHostPort(String hostsPorts,int bufferSize) throws IOException {

            Debugger.println(this,"hostsPorts:"+hostsPorts);
            System.out.flush();
            String [] hostPortsArray = hostsPorts.split(",");
            this.outputArray = new NetworkGateway[hostPortsArray.length];

            for (int i =0; i< hostPortsArray.length;i++) {

                String[] hostPortArray = hostPortsArray[i].split(":");
                outputArray[i] = new NetworkGateway(hostPortArray[0],Integer.valueOf(hostPortArray[1]),bufferSize);
            }

            return this;
        }

        public NetworkGateway[] buildArray() {
            return outputArray;
        }
    }
}
