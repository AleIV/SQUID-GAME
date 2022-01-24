package me.aleiv.core.paper.listeners;

import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.PlayerClicks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

public class CPSListener implements Listener {

    private final Core plugin;

    private final HashMap<UUID, PlayerClicks> playersCps;
    private @Getter @Setter boolean active;

    public CPSListener(Core plugin) {
        this.plugin = plugin;
        this.playersCps = new HashMap<>();

        this.active = false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!active || e.getHand() == EquipmentSlot.OFF_HAND) return;

        if (e.getAction().toString().contains("LEFT")) {
            this.playersCps.get(e.getPlayer().getUniqueId()).addLeftClick();
        } else if (e.getAction().toString().contains("RIGHT") || e.getAction() == Action.PHYSICAL) {
            this.playersCps.get(e.getPlayer().getUniqueId()).addRightClick();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.playersCps.put(e.getPlayer().getUniqueId(), new PlayerClicks());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.playersCps.remove(e.getPlayer().getUniqueId());
    }

    public PlayerClicks getPlayerClicks(UUID uuid) {
        return this.playersCps.get(uuid);
    }

}
