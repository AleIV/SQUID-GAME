package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import org.bukkit.event.Listener;

public class HologramListener implements Listener {

    public Core plugin;

    /*public HologramListener(Core plugin) {
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
    }*/
}
