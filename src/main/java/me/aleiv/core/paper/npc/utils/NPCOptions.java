package me.aleiv.core.paper.npc.utils;

import org.bukkit.Location;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NPCOptions {
    private final String name;
    private final String texture;
    private final String signature;
    private final Location location;
    private final boolean hideNametag;
}