package me.aleiv.core.paper.listeners;

import me.Fupery.ArtMap.ArtMap;
import me.Fupery.ArtMap.Event.PlayerPaintedEvent;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.CookieCapsule;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class CookieListener implements Listener {

    private final Core plugin;

    public CookieListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPixelClick(PlayerPaintedEvent e) {
        Player player = e.getPlayer();
        CookieCapsule capsule = plugin.getGame().getCookieGame().getCapsule(player);
        if (capsule == null) return;
        capsule.processEvent(e);
    }

    @EventHandler
    public void onUnmountEasel(EntityDismountEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();

        CookieCapsule capsule = plugin.getGame().getCookieGame().getCapsule(player);
        if (capsule != null && capsule.isMounted()) {
            capsule.unmount(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        CookieCapsule capsule = plugin.getGame().getCookieGame().getCapsule(e.getPlayer());
        if (capsule != null && capsule.isMounted()) {
            capsule.unmount(true);
        }
    }

}
