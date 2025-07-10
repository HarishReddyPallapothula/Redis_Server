package org.scratch.command;

import java.nio.charset.StandardCharsets;

public class GetCommand implements RedisCommand {
    private final String key;

    public GetCommand(String key) {
        this.key = key;
    }

    @Override
    public byte[] execute() {
        String value = InMemoryDatabase.get(key);
        if (value == null) {
            // Return RESP null bulk string
            return "$-1\r\n".getBytes(StandardCharsets.UTF_8);
        } else {
            return bulkString(value);
        }
    }

    private byte[] bulkString(String value) {
        return ("$" + value.length() + "\r\n" + value + "\r\n").getBytes(StandardCharsets.UTF_8);
    }
}
