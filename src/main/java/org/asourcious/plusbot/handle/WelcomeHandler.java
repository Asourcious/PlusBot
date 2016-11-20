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
            if (profile.hasProperty(GuildProfile.WELCOME_CHANNEL)) {
                TextChannel channel = guild.getTextChannelById(profile.getProperty(GuildProfile.WELCOME_CHANNEL));

                channel.sendMessage(profile.getProperty(GuildProfile.WELCOME_MESSAGE)).queue();
            }

            if (profile.hasProperty(GuildProfile.WELCOME_DM_MESSAGE)) {
                member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(profile.getProperty(GuildProfile.WELCOME_DM_MESSAGE)).queue());
            }
        } catch (Exception ignored) {}
    }
}
