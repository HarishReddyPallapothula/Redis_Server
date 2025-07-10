package org.scratch.command;

import java.nio.charset.StandardCharsets;

public class SetCommand implements RedisCommand {
    private final String key;
    private final String value;
    private final Long expiryMillis; // nullable

    public SetCommand(String key, String value, Long expiryMillis) {
        this.key = key;
        this.value = value;
        this.expiryMillis = expiryMillis;
    }

    @Override
    public byte[] execute() {
        InMemoryDatabase.set(key, value, expiryMillis);
        return "+OK\r\n".getBytes(StandardCharsets.UTF_8);
    }
}
