package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.GuildProfile;
import org.asourcious.plusbot.config.Settings;

public class WelcomeHandler {

    private Settings settings;

    public WelcomeHandler(PlusBot plusBot) {
        settings = plusBot.getSettings();
    }

    public void handleMemberJoin(Guild guild, Member member) {
        GuildProfile profile = settings.getProfile(guild);

        try {
            if (profile.getWelcomeChannel() != null) {
                TextChannel channel = guild.getTextChannelById(profile.getWelcomeChannel());

                channel.sendMessage(profile.getWelcomeMessage()).queue();
            }

            if (profile.getWelcomeDMMessage() != null) {
                member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(profile.getWelcomeDMMessage()).queue());
            }
        } catch (Exception ignored) {}
    }
}
