package me.aleiv.core.paper.core;

import me.aleiv.core.paper.Core;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramPlayer {

    private final Core plugin;
    private final String template = "§3Jugadores conectados: §f%players%§3/§f150";

    private ArmorStand armorStand;

    public HologramPlayer(Core plugin) {
        this.plugin = plugin;
    }

    public void spawnHologram() {
        if (this.armorStand != null) return;

        World world = this.plugin.getServer().getWorld("world");
        if (world == null) return;

        Location loc = new Location(world, 180, 34, 402);
        this.armorStand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        this.armorStand.setInvulnerable(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setGravity(false);
        this.armorStand.setCustomNameVisible(true);
        updateName(0);
    }

    public void removeHologram() {
        if (this.armorStand == null) return;
        this.armorStand.remove();
        this.armorStand = null;
    }

    public void updateName(int players) {
        if (this.armorStand == null) return;
        this.armorStand.setCustomName(template.replaceAll("%players%", String.valueOf(players)));
    }

    public boolean isSpawned() {
        return this.armorStand != null;
    }
}
