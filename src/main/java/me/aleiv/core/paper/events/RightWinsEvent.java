package me.aleiv.core.paper.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RightWinsEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;


    public RightWinsEvent(boolean async) {
        super(async);

    }

    public RightWinsEvent() {
        this(false);
    }

}
