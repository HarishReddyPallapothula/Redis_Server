package org.scratch;

import org.scratch.command.EchoCommand;
import org.scratch.command.GetCommand;
import org.scratch.command.PingCommand;
import org.scratch.command.SetCommand;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RESPParser {
    public static CommandExecutor parse(DataInputStream in)  throws IOException {
        try {
            byte type = in.readByte();
            if (type != '*') throw new IllegalArgumentException("Expected array '*'");

            int argCount = Integer.parseInt(readLine(in));
            String[] args = new String[argCount];

            for (int i = 0; i < argCount; i++) {
                byte dollar = in.readByte();
                if (dollar != '$') throw new IllegalArgumentException("Expected bulk string '$'");
                int len = Integer.parseInt(readLine(in));
                byte[] buf = new byte[len];
                in.readFully(buf);
                in.readByte(); in.readByte(); // consume CR LF
                args[i] = new String(buf, StandardCharsets.UTF_8);
            }

            String cmd = args[0].toUpperCase();
            CommandExecutor.Builder b = CommandExecutor.builder();

            switch (cmd) {
                case "ECHO":
                    if (args.length != 2) throw new IllegalArgumentException("ECHO needs 1 arg");
                    return b.redisCommand(new EchoCommand(args[1]))
                            .content("ECHO " + args[1]).build();

                case "PING":
                    String msg = args.length > 1 ? args[1] : null;
                    return b.redisCommand(new PingCommand(msg))
                            .content(msg == null ? "PING" : "PING " + msg).build();

                case "SET":
                    if (args.length < 3) throw new IllegalArgumentException("SET needs at least key and value");
                    String key = args[1];
                    String value = args[2];
                    Long px = null;

                    if (args.length == 5 && args[3].equalsIgnoreCase("PX")) {
                        try {
                            px = Long.parseLong(args[4]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid PX value");
                        }
                    } else if (args.length > 3) {
                        throw new IllegalArgumentException("Unsupported SET options");
                    }

                    return b.redisCommand(new SetCommand(key, value, px))
                            .content("SET " + key + " " + value + (px != null ? " PX " + px : "")).build();


                case "GET":
                    if (args.length != 2) throw new IllegalArgumentException("GET needs key");
                    return b.redisCommand(new GetCommand(args[1]))
                            .content("GET " + args[1]).build();

                default:
                    throw new UnsupportedOperationException("Unknown command: " + cmd);
            }

        } catch (IOException e) {
            throw new RuntimeException("RESP parse failed", e);
        }
    }

    private static String readLine(DataInputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                if (in.read() == '\n') break;
            } else {
                baos.write(b);
            }
        }
        return baos.toString("UTF-8");
    }
}

