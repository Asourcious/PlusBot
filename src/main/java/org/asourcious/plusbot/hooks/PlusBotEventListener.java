package org.asourcious.plusbot.hooks;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.handle.*;

public class PlusBotEventListener extends ListenerAdapter {

    private WelcomeHandler welcomeHandler;
    private MuteHandler muteHandler;
    private AutoRoleHandler roleHandler;
    private CommandHandler commandHandler;
    private TagHandler tagHandler;

    public PlusBotEventListener(PlusBot plusBot) {
        this.commandHandler = new CommandHandler(plusBot);
        this.muteHandler = new MuteHandler(plusBot);
        this.roleHandler = new AutoRoleHandler(plusBot);
        this.welcomeHandler = new WelcomeHandler(plusBot);
        this.tagHandler = new TagHandler(plusBot);
    }

    @Override
    public void onReady(ReadyEvent event) {
        PlusBot.LOG.info(Constants.READY_MESSAGE);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        welcomeHandler.handleMemberJoin(event.getGuild(), event.getMember());
        roleHandler.handleMemberJoin(event.getGuild(), event.getMember());
        muteHandler.handleMemberJoin(event.getGuild(), event.getMember());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Statistics.numMessages++;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        commandHandler.handleMessage(event.getMessage(), event.getAuthor(), event.getChannel(), event.getGuild());
        tagHandler.handleMessage(event.getMessage(), event.getAuthor(), event.getChannel(), event.getGuild());
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void shutdown() {
        commandHandler.shutdown();
        muteHandler.shutdown();
    }
}
