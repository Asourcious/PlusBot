package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.hooks.PlusBotEventListener;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShardHandler {

    private List<JDA> shards;

    public ShardHandler(PlusBot plusBot, CommandHandler commandHandler, int numShards) throws LoginException {
        this.shards = new ArrayList<>();

        if (numShards == 1) {
            try {
                shards.add(new JDABuilder(AccountType.BOT)
                        .setToken(plusBot.getSettings().getToken())
                        .addListener(new PlusBotEventListener(commandHandler))
                        .setBulkDeleteSplittingEnabled(false)
                        .buildAsync()
                );
            } catch (RateLimitedException e) {
                PlusBot.LOG.log(e);
                System.exit(1);
            }
            return;
        }

        for (int i = 0; i < numShards; i++) {
            try {
                shards.add(new JDABuilder(AccountType.BOT)
                        .setToken(plusBot.getSettings().getToken())
                        .addListener(new PlusBotEventListener(commandHandler))
                        .setBulkDeleteSplittingEnabled(false)
                        .useSharding(i, numShards)
                        .buildAsync());
            } catch (RateLimitedException e) {
                PlusBot.LOG.log(e);
                System.exit(1);
            }
        }
    }

    public void shutdown(boolean free) {
        shards.forEach(jda -> jda.shutdown(free));
    }

    public List<JDA> getShards() {
        return Collections.unmodifiableList(new ArrayList<>(shards));
    }
}
