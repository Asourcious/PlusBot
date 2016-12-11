package org.asourcious.plusbot;

import org.asourcious.plusbot.commands.admin.*;
import org.asourcious.plusbot.commands.audio.*;
import org.asourcious.plusbot.commands.config.*;
import org.asourcious.plusbot.commands.fun.*;
import org.asourcious.plusbot.commands.fun.Math;
import org.asourcious.plusbot.commands.info.*;
import org.asourcious.plusbot.commands.maintenance.*;
import org.asourcious.plusbot.config.Settings;
import org.asourcious.plusbot.handle.CommandHandler;
import org.asourcious.plusbot.handle.ShardHandler;
import org.asourcious.plusbot.handle.audio.PlayerHandler;
import org.asourcious.plusbot.handle.web.GoogleSearchHandler;
import org.asourcious.plusbot.handle.web.WeatherHandler;
import org.asourcious.plusbot.hooks.PlusBotEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlusBot {

    public static final Logger LOG = LoggerFactory.getLogger(PlusBot.class);

    private Settings settings;
    private ShardHandler shardHandler;
    private PlusBotEventListener eventListener;

    private GoogleSearchHandler googleSearchHandler;
    private PlayerHandler playerHandler;
    private WeatherHandler weatherHandler;

    private ScheduledExecutorService cacheCleaner;

    public PlusBot() throws LoginException, IOException {
        this.settings = new Settings();
        this.eventListener = new PlusBotEventListener(this);
        this.shardHandler = new ShardHandler(this, eventListener, 1);
        this.googleSearchHandler = new GoogleSearchHandler();
        this.playerHandler = new PlayerHandler();
        this.weatherHandler = new WeatherHandler(settings);
        this.cacheCleaner = Executors.newSingleThreadScheduledExecutor();
        CommandHandler commandHandler = eventListener.getCommandHandler();

        commandHandler.registerCommand(new Ban(this));
        commandHandler.registerCommand(new Kick(this));
        commandHandler.registerCommand(new Mute(this));
        commandHandler.registerCommand(new Unban(this));
        commandHandler.registerCommand(new Unmute(this));

        commandHandler.registerCommand(new Clear(this));
        commandHandler.registerCommand(new ForceSkip(this));
        commandHandler.registerCommand(new Join(this));
        commandHandler.registerCommand(new Leave(this));
        commandHandler.registerCommand(new NowPlaying(this));
        commandHandler.registerCommand(new Pause(this));
        commandHandler.registerCommand(new Play(this));
        commandHandler.registerCommand(new Repeat(this));
        commandHandler.registerCommand(new Resume(this));
        commandHandler.registerCommand(new Shuffle(this));
        commandHandler.registerCommand(new Skip(this));
        commandHandler.registerCommand(new Stop(this));
        commandHandler.registerCommand(new Volume(this));

        commandHandler.registerCommand(new AutoRole(this));
        commandHandler.registerCommand(new Blacklist(this));
        commandHandler.registerCommand(new CommandToggle(this));
        commandHandler.registerCommand(new Prefix(this));
        commandHandler.registerCommand(new Tag(this));
        commandHandler.registerCommand(new Welcome(this));
        commandHandler.registerCommand(new WelcomeDM(this));

        commandHandler.registerCommand(new Google(this));
        commandHandler.registerCommand(new Math(this));
        commandHandler.registerCommand(new NeedsMoreJPEG(this));
        commandHandler.registerCommand(new RIP(this));
        commandHandler.registerCommand(new Triggered(this));
        commandHandler.registerCommand(new UrbanDictionary(this));
        commandHandler.registerCommand(new Weather(this));
        commandHandler.registerCommand(new YodaSpeak(this));

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

        cacheCleaner.scheduleAtFixedRate(() -> googleSearchHandler.cleanCache(), 4, 4, TimeUnit.HOURS);
        cacheCleaner.scheduleAtFixedRate(() -> weatherHandler.cleanCache(), 10, 10, TimeUnit.MINUTES);
    }

    public void shutdown(boolean free) {
        settings.shutdown();
        cacheCleaner.shutdown();
        shardHandler.shutdown(free);
        eventListener.shutdown();
    }

    public Settings getSettings() {
        return settings;
    }

    public CommandHandler getCommandHandler() {
        return eventListener.getCommandHandler();
    }

    public ShardHandler getShardHandler() {
        return shardHandler;
    }

    public GoogleSearchHandler getGoogleSearchHandler() {
        return googleSearchHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public WeatherHandler getWeatherHandler() {
        return weatherHandler;
    }
}
