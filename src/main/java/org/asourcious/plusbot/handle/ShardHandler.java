package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.hooks.PlusBotEventListener;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class ShardHandler {

    private Set<JDA> shards;

    public ShardHandler(PlusBot plusBot, CommandHandler commandHandler, int numShards) throws LoginException {
        this.shards = ConcurrentHashMap.newKeySet();
        AutoRoleHandler roleHandler = new AutoRoleHandler(plusBot);
        WelcomeHandler welcomeHandler = new WelcomeHandler(plusBot);

        if (numShards == 1) {
            try {
                shards.add(new JDABuilder(AccountType.BOT)
                        .setToken(plusBot.getSettings().getToken())
                        .addListener(new PlusBotEventListener(commandHandler, roleHandler, welcomeHandler))
                        .setBulkDeleteSplittingEnabled(false)
                        .buildAsync()
                );
            } catch (RateLimitedException e) {
                PlusBot.LOG.log(e);
                System.exit(Constants.BOT_LOGIN_ERROR);
            }
            return;
        }

        for (int i = 0; i < numShards; i++) {
            try {
                shards.add(new JDABuilder(AccountType.BOT)
                        .setToken(plusBot.getSettings().getToken())
                        .addListener(new PlusBotEventListener(commandHandler, roleHandler, welcomeHandler))
                        .setBulkDeleteSplittingEnabled(false)
                        .useSharding(i, numShards)
                        .buildAsync());
            } catch (RateLimitedException e) {
                PlusBot.LOG.log(e);
                System.exit(Constants.BOT_LOGIN_ERROR);
            }
        }
    }

    public void shutdown(boolean free) {
        shards.forEach(jda -> jda.shutdown(free));
    }

    public String getInformation(JDA shard) {
        return  "Guilds: " + shard.getGuilds().size() + " "
                + "TC's: " + shard.getTextChannels().size() + " "
                + "VC's: " + shard.getVoiceChannels().size() + " "
                + "Status: " + shard.getStatus().toString() + " ";
    }

    public int getId(JDA shard) {
        return shard.getShardInfo().getShardId();
    }

    public int getNumberOfGuilds() {
        return shards.parallelStream().mapToInt(jda -> jda.getGuilds().size()).sum();
    }

    public int getNumberOfTextChannels() {
        return shards.parallelStream().mapToInt(jda -> jda.getTextChannels().size()).sum();
    }

    public int getNumberOfVoiceChannels() {
        return shards.parallelStream().mapToInt(jda -> jda.getVoiceChannels().size()).sum();
    }

    public int getNumberOfUsers() {
        return shards.parallelStream().mapToInt(jda -> jda.getUsers().size()).sum();
    }

    public Set<JDA> getShards() {
        Set<JDA> copy = new TreeSet<>((o1, o2) -> o1.getShardInfo().getShardId() - o2.getShardInfo().getShardId());
        copy.addAll(shards);

        return Collections.unmodifiableSet(copy);
    }
}
