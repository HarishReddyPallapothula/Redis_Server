package org.scratch.command;

public class UnknownCommand implements RedisCommand{

    public byte[] execute() {
        return new byte[0];
    }
}
