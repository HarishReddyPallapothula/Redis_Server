package org.scratch;

import org.scratch.command.RedisCommand;
public class CommandExecutor {
    private RedisCommand redisCommand;
    private String content;

    public RedisCommand getRedisCommand() {
        return redisCommand;
    }

    public void setRedisCommand(RedisCommand redisCommand) {
        this.redisCommand = redisCommand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private CommandExecutor(RedisCommand redisCommand, String content) {
        this.redisCommand = redisCommand;
        this.content = content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RedisCommand redisCommand;
        private String content = "";

        public Builder redisCommand(RedisCommand redisCommand) {
            this.redisCommand = redisCommand;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public CommandExecutor build() {
            return new CommandExecutor(this.redisCommand, this.content);
        }
    }
}

