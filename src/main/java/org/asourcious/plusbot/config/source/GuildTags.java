package org.asourcious.plusbot.config.source;

import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.config.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class GuildTags extends DataSource<Pair<String, String>> {
    public GuildTags(Connection connection, ExecutorService executorService) throws SQLException {
        super(connection, executorService, Constants.TAGS);
        this.add    = connection.prepareStatement("INSERT INTO " + table + " (container, tag_name, tag_text) VALUES (?, ?, ?);");
        this.remove = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ? AND tag_name = ? AND tag_text = ?;");
        this.clear  = connection.prepareStatement("DELETE FROM " + table + " WHERE container = ?;");
    }

    @Override
    protected Pair<String, Pair<String, String>> deserializeRow(String[] columns) {
        return new Pair<>(columns[1], new Pair<>(columns[2], columns[3]));
    }

    @Override
    protected String[] serializeRow(String container, Pair<String, String> entry) {
        return new String[] { container, entry.getKey(), entry.getValue() };
    }

    public boolean existsWithName(String container, String name) {
        return getByName(container, name) != null;
    }

    public String getByName(String container, String name) {
        Pair<String, String> pair = getTagByName(container, name);
        return pair == null ? null : pair.getValue();
    }

    public void removeByName(String container, String name) {
        remove(container, getTagByName(container, name));
    }

    public void edit(String container, String tag, String newContent) {
        Pair<String, String> oldTag = cache.get(container).parallelStream().filter(pair -> pair.getKey().equals(tag)).findAny().orElse(null);
        remove(container, oldTag);
        add(container, new Pair<>(tag, newContent));
    }

    private Pair<String, String> getTagByName(String container, String name) {
        if (!cache.containsKey(container))
            return null;

        return cache.get(container).parallelStream().filter(pair -> pair.getKey().equals(name)).findAny().orElse(null);
    }
}
