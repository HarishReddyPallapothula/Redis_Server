package org.scratch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) throws IOException {
//        String CRLF = "\r\n";
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket(port);

            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            while(true){
                clientSocket = serverSocket.accept();
                final Socket clientCopy = clientSocket;
                executorService.submit(() -> handleClient(clientCopy)) ;
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

    private static void handleClient(Socket socket){
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream()){
            String command;
            while((command = bufferedReader.readLine()) != null){
                System.out.println("Received command : "+command);
                if(command.contains("PING")) {
                    String myString = "+PONG\r\n";
                    byte[] responseBytes = myString.getBytes(StandardCharsets.UTF_8);
                    output.write(responseBytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}