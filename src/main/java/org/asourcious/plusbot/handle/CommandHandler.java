package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandler {

    private PlusBot plusBot;

    private final Map<String, Command> commands;
    private final Map<String, String> aliases;
    private final Map<String, RateLimitHandler> rateLimitHandlers;

    public CommandHandler(PlusBot plusBot) {
        this.plusBot = plusBot;
        this.commands = new ConcurrentHashMap<>();
        this.aliases = new ConcurrentHashMap<>();
        this.rateLimitHandlers = new ConcurrentHashMap<>();
    }

    public void handle(Message message, User author, MessageChannel channel, Guild guild, boolean isPrivate) {

    }

    public Command getCommand(String name) {
        if (aliases.containsKey(name.toLowerCase()))
            return commands.get(aliases.get(name.toLowerCase()));

        return commands.get(name.toLowerCase());
    }

    public boolean hasCommand(String name) {
        return getCommand(name) != null;
    }

    public RateLimitHandler getRateLimitHandler(String name) {
        if (aliases.containsKey(name.toLowerCase()))
            return rateLimitHandlers.get(aliases.get(name.toLowerCase()));

        return rateLimitHandlers.get(name.toLowerCase());
    }

    public List<Command> getRegisteredCommands() {
        return Collections.unmodifiableList(new ArrayList<>(commands.values()));
    }

    public void registerCommand(Command command) {
        String name = command.getName().toLowerCase();
        commands.put(name, command);
        rateLimitHandlers.put(name, new RateLimitHandler(command));

        for (String alias : command.getAliases()) {
            aliases.put(alias.toLowerCase(), name);
        }
    }
}
