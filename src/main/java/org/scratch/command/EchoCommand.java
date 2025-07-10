package org.scratch.command;

import java.nio.charset.StandardCharsets;

public class EchoCommand implements  RedisCommand{
    private final String message;

    public EchoCommand(String message) {
        this.message = message;
    }

    @Override
    public byte[] execute() {
        return bulkString(message);
    }

    private byte[] bulkString(String value) {
        return ("$" + value.length() + "\r\n" + value + "\r\n").getBytes(StandardCharsets.UTF_8);
    }
}
