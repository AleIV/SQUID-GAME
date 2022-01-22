package me.aleiv.core.paper.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import me.aleiv.cinematicCore.paper.events.LiveCinematicPlayerRegisterEvent;
import me.aleiv.cinematicCore.paper.events.LiveCinematicPlayerRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import lombok.NonNull;
import me.aleiv.cinematicCore.paper.events.CinematicFinishEvent;
import me.aleiv.cinematicCore.paper.events.CinematicStartEvent;
import me.aleiv.core.paper.Core;

public class CinematicListener implements Listener {

    private @NonNull Core plugin;

    private HashMap<UUID, ItemStack> headItems;

    public CinematicListener(Core plugin) {
        this.plugin = plugin;
        this.headItems = new HashMap<>();
    }

    @EventHandler
    public void onCinematicStart(CinematicStartEvent e) {
        List<Player> playerInCinematic = e.getCinematicProgress().getPlayerInfo().values().parallelStream().map(i -> Bukkit.getPlayer(i.getUuid())).filter(Objects::nonNull).toList();

        playerInCinematic.forEach(p -> {
            headItems.put(p.getUniqueId(), p.getInventory().getHelmet());
            p.getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
        });
    }

    @EventHandler
    public void onCinematicEnd(CinematicFinishEvent e) {
        List<Player> playerInCinematic = e.getCinematicProgress().getPlayerInfo().values().parallelStream().map(i -> Bukkit.getPlayer(i.getUuid())).filter(Objects::nonNull).toList();

        playerInCinematic.forEach(p -> p.getInventory().setHelmet(headItems.remove(p.getUniqueId())));
    }

    @EventHandler
    public void onLiveCinematicRegisterPlayer(LiveCinematicPlayerRegisterEvent e) {
        Player player = e.getPlayer();

        headItems.put(player.getUniqueId(), player.getInventory().getHelmet());
        player.getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
    }

    @EventHandler
    public void onPlayerRemoveLiveCinematic(LiveCinematicPlayerRemoveEvent e) {
        Player player = e.getPlayer();

        if (headItems.containsKey(player.getUniqueId())) {
            ItemStack item = headItems.remove(player.getUniqueId());
            player.getInventory().setHelmet(item);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (headItems.containsKey(player.getUniqueId())) {
            ItemStack item = headItems.remove(player.getUniqueId());
            player.getInventory().setHelmet(item);
        }
    }

}
