package me.aleiv.core.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.Fupery.ArtMap.Event.PlayerPaintedEvent;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Games.CookieGame;
import me.aleiv.core.paper.objects.CookieCapsule;

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
            if (capsule.isOnError()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> e.getDismounted().addPassenger(player), 3L);
            } else {
                capsule.unmount(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        CookieCapsule capsule = plugin.getGame().getCookieGame().getCapsule(e.getPlayer());
        if (capsule != null && capsule.isMounted()) {
            capsule.unmount(true);
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        this.plugin.getGame().getCookieGame().stop();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!plugin.getGame().getCookieGame().isStarted()) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (item.getType() == Material.FERMENTED_SPIDER_EYE && meta.hasCustomModelData()) {
            e.setCancelled(true);

            CookieCapsule cookieCapsule = plugin.getGame().getCookieGame().getCapsule(e.getPlayer());
            if (cookieCapsule == null) {
                CookieGame.CookieType type = switch (meta.getCustomModelData()) {
                    case 3 -> CookieGame.CookieType.CREEPER;
                    case 1 -> CookieGame.CookieType.EYE;
                    case 4 -> CookieGame.CookieType.RODOLFO;
                    case 2 -> CookieGame.CookieType.SQUID;
                    default -> null;
                };
                if (type == null) return;

                cookieCapsule = plugin.getGame().getCookieGame().createCapsule(player, type);
            }
            if (!cookieCapsule.isMounted()) {
                cookieCapsule.mount();
            }

        }
    }

}
