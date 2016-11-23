package org.asourcious.plusbot.handle.web;

import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.PlusBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GoogleSearchHandler {
    private Map<String, Pair<String, ZonedDateTime>> cache;

    public GoogleSearchHandler() {
        cache = new ConcurrentHashMap<>();
    }

    public String search(String query) {
        if (cache.containsKey(query))
            return cache.get(query).getKey();

        String request;
        try {
            request = "https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8") + "&num=10";
        } catch (UnsupportedEncodingException ex) {
            PlusBot.LOG.log(ex);
            return null;
        }

        List<String> results;
        try {
            Document doc = Jsoup
                    .connect(request)
                    .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();

            Elements links = doc.select("a[href]");
            results = new ArrayList<>();
            links.stream().map((link) -> link.attr("href")).filter(temp -> (temp.startsWith("/url?q="))).forEach(temp -> {
                try {
                    String decoded = URLDecoder.decode(temp.substring(7, temp.indexOf("&sa=")), "UTF-8");
                    if(!decoded.equals("/settings/ads/preferences?hl=en"))
                        results.add(decoded);
                } catch (UnsupportedEncodingException ignored) {}
            });
        } catch (IOException e) {
            PlusBot.LOG.log(e);
            return null;
        }

        if (results.isEmpty())
            results.add("No results found!");

        cache.put(query.toLowerCase(), new Pair<>(results.get(0), ZonedDateTime.now()));
        return results.get(0);
    }

    public void cleanCache() {
        cache.keySet().parallelStream()
                .filter(query -> ZonedDateTime.now().isAfter(cache.get(query).getValue().plusHours(4)))
                .forEach(query -> cache.remove(query));
    }
}
