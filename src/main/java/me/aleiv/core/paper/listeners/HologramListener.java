package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class HologramListener implements Listener {

    private final Core plugin;

    public HologramListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.updateCounter();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.updateCounter();
    }

    @EventHandler
    public void onDisablePlugin(PluginDisableEvent e) {
        plugin.getHologramPlayer().removeHologram();
    }

    private void updateCounter() {
        if (!plugin.getHologramPlayer().isSpawned()) return;

        int online = (int) Bukkit.getOnlinePlayers().stream().filter(p -> plugin.getGame().isPlayer(p)).count();
        plugin.getHologramPlayer().updateName(online);
    }
}
