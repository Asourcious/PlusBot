package org.asourcious.plusbot.handle;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.source.Mutes;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MuteHandler {

    private PlusBot plusBot;
    private ScheduledExecutorService executorService;

    public MuteHandler(PlusBot plusBot) {
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("MuteHandler Thread %d").build();
        this.plusBot = plusBot;
        executorService = Executors.newSingleThreadScheduledExecutor(threadFactory);

        executorService.scheduleAtFixedRate(() -> plusBot.getSettings().getMutes().checkMutes(plusBot.getShardHandler()), 10, 1, TimeUnit.SECONDS);
    }

    public void handleMemberJoin(Guild guild, Member member) {
        Mutes mutes = plusBot.getSettings().getMutes();
        if (!mutes.has(guild.getId(), member.getUser().getId()))
            return;

        List<Role> roles = guild.getRolesByName("Muted", true);
        if (roles.size() > 1 || roles.isEmpty())
            return;

        if (guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES))
            guild.getController().modifyMemberRoles(member, roles.get(0)).queue();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
