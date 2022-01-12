package me.aleiv.core.paper.core;

import fi.iki.elonen.NanoHTTPD;
import me.aleiv.core.paper.Core;

import java.util.Map;

public class WebServer extends NanoHTTPD {

    private final Core instance;

    private boolean enabled;

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
        session.getHeaders().put("Content-Type", "application/json");
        // TODO: Get JSON
        return newFixedLengthResponse("{}");
    }

}
