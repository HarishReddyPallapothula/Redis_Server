import org.scratch.ClientHandler;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            while(true){
                clientSocket = serverSocket.accept();
                final Socket clientCopy = clientSocket;
                executorService.submit(() -> ClientHandler.handleClient(clientCopy)) ;
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}