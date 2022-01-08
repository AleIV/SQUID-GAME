package me.aleiv.core.paper.listeners;

import me.Fupery.ArtMap.ArtMap;
import me.Fupery.ArtMap.Event.PlayerPaintedEvent;
import me.Fupery.ArtMap.Event.PlayerUnmountEaselEvent;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.CookieCapsule;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class CookieListener implements Listener {

    private final Core plugin;

    public CookieListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPixelClick(PlayerPaintedEvent e) {
        // TODO: Make the checks
        System.out.println("x = " + e.getX() + " | y = " + e.getY());
    }

    @EventHandler
    public void onUnmountEasel(EntityDismountEvent e) {
        if (e.getDismounted().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getDismounted();

        CookieCapsule capsule = plugin.getGame().getCookieGame().getCapsule(player);
        if (capsule != null && capsule.isMounted()) {
            capsule.unmount(true);
        }
    }

}
