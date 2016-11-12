package org.asourcious.plusbot;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.asourcious.plusbot.commands.maintenance.*;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.handle.CommandHandler;
import org.asourcious.plusbot.handle.ShardHandler;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class PlusBot {

    public static final SimpleLog LOG = SimpleLog.getLog("PlusBot");

    private Settings settings;
    private CommandHandler commandHandler;
    private ShardHandler shardHandler;

    public PlusBot() throws LoginException, IOException {
        this.settings = new Settings();
        this.commandHandler = new CommandHandler(this);
        this.shardHandler = new ShardHandler(this, commandHandler, 1);

        commandHandler.registerCommand(new Eval(this));
        commandHandler.registerCommand(new Ping(this));
        commandHandler.registerCommand(new Restart(this));
        commandHandler.registerCommand(new Shutdown(this));
        commandHandler.registerCommand(new Status(this));
    }

    public void shutdown(boolean free) {
        shardHandler.shutdown(free);
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
}
