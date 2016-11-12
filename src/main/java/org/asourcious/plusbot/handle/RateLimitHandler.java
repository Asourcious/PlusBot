package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.Command.RateLimit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RateLimitHandler {

    private Command command;
    private RateLimit rateLimit;

    private Map<String, Integer> useMap;
    private ScheduledExecutorService timer;

    public RateLimitHandler(Command command) {
        this.command = command;
        this.rateLimit = command.getRateLimit();
        this.useMap = new ConcurrentHashMap<>();
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public boolean execute(String id, String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        if (rateLimit != null) {
            if (!useMap.containsKey(id)) {
                useMap.put(id, 1);
                timer.schedule(() -> useMap.remove(id), rateLimit.getAmountTime(), rateLimit.getUnit());
            } else {
                useMap.put(id, useMap.get(id) + 1);
            }

            if (useMap.get(id) > rateLimit.getNumUses())
                return false;
        }

        command.execute(stripped, message, author, channel, guild);
        return true;
    }
}