package org.asourcious.plusbot;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.asourcious.plusbot.commands.config.AutoRole;
import org.asourcious.plusbot.commands.config.Blacklist;
import org.asourcious.plusbot.commands.config.CommandToggle;
import org.asourcious.plusbot.commands.config.Prefix;
import org.asourcious.plusbot.commands.fun.Google;
import org.asourcious.plusbot.commands.fun.RIP;
import org.asourcious.plusbot.commands.fun.Triggered;
import org.asourcious.plusbot.commands.info.*;
import org.asourcious.plusbot.commands.maintenance.*;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.handle.CommandHandler;
import org.asourcious.plusbot.handle.ShardHandler;
import org.asourcious.plusbot.handle.web.GoogleSearchHandler;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlusBot {

    public static final SimpleLog LOG = SimpleLog.getLog("PlusBot");

    private Settings settings;
    private ShardHandler shardHandler;
    private CommandHandler commandHandler;

    private GoogleSearchHandler googleSearchHandler;

    private ScheduledExecutorService cacheCleaner;

    public PlusBot() throws LoginException, IOException {
        this.settings = new Settings();
        this.commandHandler = new CommandHandler(this);
        this.shardHandler = new ShardHandler(this, commandHandler, 1);
        this.googleSearchHandler = new GoogleSearchHandler();
        this.cacheCleaner = Executors.newSingleThreadScheduledExecutor();

        commandHandler.registerCommand(new AutoRole(this));
        commandHandler.registerCommand(new Blacklist(this));
        commandHandler.registerCommand(new CommandToggle(this));
        commandHandler.registerCommand(new Prefix(this));

        commandHandler.registerCommand(new Google(this));
        commandHandler.registerCommand(new RIP(this));
        commandHandler.registerCommand(new Triggered(this));

        commandHandler.registerCommand(new ChannelInfo(this));
        commandHandler.registerCommand(new GuildInfo(this));
        commandHandler.registerCommand(new Help(this));
        commandHandler.registerCommand(new RoleInfo(this));
        commandHandler.registerCommand(new UserInfo(this));

        commandHandler.registerCommand(new Clean(this));
        commandHandler.registerCommand(new Eval(this));
        commandHandler.registerCommand(new Ping(this));
        commandHandler.registerCommand(new Restart(this));
        commandHandler.registerCommand(new Shutdown(this));
        commandHandler.registerCommand(new Status(this));

        cacheCleaner.scheduleAtFixedRate(() -> {
            LOG.info("Cleaning cache");
            googleSearchHandler.cleanCache();
        }, 4, 4, TimeUnit.HOURS);
    }

    public void shutdown(boolean free) {
        settings.shutdown();
        cacheCleaner.shutdown();
        shardHandler.shutdown(free);
        commandHandler.shutdown();
    }

    public Settings getSettings() {
        return settings;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public ShardHandler getShardHandler() {
        return shardHandler;
    }

    public GoogleSearchHandler getGoogleSearchHandler() {
        return googleSearchHandler;
    }
}
