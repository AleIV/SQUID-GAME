package me.aleiv.core.paper.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;
import lombok.NonNull;
import me.aleiv.core.paper.Core;

public class WebServer extends NanoHTTPD {

    private final @NonNull Core instance;

    boolean enabled;

    public WebServer(Core instance, int port) {
        super(port);
        this.instance = instance;
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            this.enabled = true;
        } catch (Exception e) {
            this.enabled = false;
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        // Set application sending json
        File file = new File(System.getProperty("user.dir") + File.separatorChar + "secrets/participants.json");
        session.getHeaders().put("Content-Type", "application/json");
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()),
                    Charset.defaultCharset());
            StringBuilder json = new StringBuilder();
            for (String line : lines) {
                json.append(line);
            }
            return newFixedLengthResponse(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse("{\"error\": true}");
    }

}
