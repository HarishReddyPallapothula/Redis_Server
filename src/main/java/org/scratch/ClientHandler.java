package org.scratch;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ClientHandler {
    public static void handleClient(Socket socket){
        try (
                Socket clientSocket = socket;
                OutputStream output = clientSocket.getOutputStream();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream())
        ) {
            while (true) {
                try {
                    CommandExecutor commandExecutor = RESPParser.parse(in);
                    byte[] response = commandExecutor.getRedisCommand().execute();
                    output.write(response);
                    output.flush();
                } catch (IOException e) {
                    // Client disconnected or stream closed
                    break;
                } catch (Exception e) {
                    // Respond with RESP error if command parsing/execution fails
                    String errorMsg = "-ERR " + e.getMessage() + "\r\n";
                    output.write(errorMsg.getBytes(StandardCharsets.UTF_8));
                    output.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
