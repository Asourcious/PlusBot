package org.asourcious.plusbot.config;


import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings {

    private JSONObject credentials;

    public Settings() throws IOException {
        credentials = new JSONObject(new String(Files.readAllBytes(Paths.get("credentials.json"))));
    }

    public String getToken() {
        return credentials.getString("token");
    }
}
