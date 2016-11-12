package org.asourcious.plusbot;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.handle.ShardHandler;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class PlusBot {

    public static final SimpleLog LOG = SimpleLog.getLog("PlusBot");

    private Settings settings;
    private ShardHandler shardHandler;

    public PlusBot() throws LoginException, IOException {
        this.settings = new Settings();
        this.shardHandler = new ShardHandler(this, 1);
    }

    public void shutdown(boolean free) {
        shardHandler.shutdown(free);
    }

    public Settings getSettings() {
        return settings;
    }
}
