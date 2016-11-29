package org.asourcious.plusbot.config.source;

import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.config.DataSource;
import org.asourcious.plusbot.handle.ShardHandler;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;

public class Mutes extends DataSource<Pair<String, ZonedDateTime>> {

    public Mutes(HikariDataSource connectionPool, ExecutorService executorService) throws SQLException {
        super(connectionPool, executorService, Constants.MUTES);
        this.add    = "INSERT INTO " + table + " (container, entry, time) VALUES (?, ?, ?);";
        this.remove = "DELETE FROM " + table + " WHERE container = ? AND entry = ? AND time = ?;";
        this.clear  = "DELETE FROM " + table + " WHERE container = ?;";
    }

    @Override
    protected Pair<String, Pair<String, ZonedDateTime>> deserializeRow(String[] columns) {
        return new Pair<>(columns[1], new Pair<>(columns[2], ZonedDateTime.parse(columns[3])));
    }

    @Override
    protected String[] serializeRow(String container, Pair<String, ZonedDateTime> entry) {
        return new String[] { container, entry.getKey(), entry.getValue().format(DateTimeFormatter.ISO_ZONED_DATE_TIME) };
    }

    public boolean has(String container, String user) {
        return get(container, user) != null;
    }

    public Pair<String, ZonedDateTime> get(String container, String user) {
        if (!cache.containsKey(container))
            return null;

        return cache.get(container).parallelStream().filter(pair -> pair.getFirst().equals(user)).findAny().orElse(null);
    }

    public void checkMutes(ShardHandler shardHandler) {
        for (String guild : cache.keySet()) {
            if (!shardHandler.getGuildById(guild).getSelfMember().hasPermission(Permission.MANAGE_ROLES))
                continue;

            for (Pair<String, ZonedDateTime> pair : cache.get(guild)) {
                if (pair.getValue().isAfter(ZonedDateTime.now()))
                    return;

                remove(guild, pair);
                Guild target = shardHandler.getGuildById(guild);
                if (target == null)
                    continue;
                if (target.getMemberById(pair.getKey()) == null)
                    continue;
                target.getController().removeRolesFromMember(target.getMemberById(pair.getKey()), target.getRolesByName("muted", true)).queue();
            }
        }
    }
}
