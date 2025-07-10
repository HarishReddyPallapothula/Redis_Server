package org.scratch.command;

import java.nio.charset.StandardCharsets;

public class PingCommand implements RedisCommand{
    private final String message;

    public PingCommand(String message) {
        this.message = message;
    }

    @Override
    public byte[] execute() {
        String reply = (message == null) ? "PONG" : message;
        return bulkString(reply);
    }

    private byte[] bulkString(String value) {
        return ("$" + value.length() + "\r\n" + value + "\r\n").getBytes(StandardCharsets.UTF_8);
    }
}
