package me.aleiv.core.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.GameType;
import me.aleiv.core.paper.events.GameTickEvent;

public class GlobalListener implements Listener {

    Core instance;

    public GlobalListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onGameTick(GameTickEvent e) {
        Bukkit.getScheduler().runTask(instance, () -> {

        });
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        var games = instance.getGame().getGames();
        if (games.get(GameType.RED_GREEN)) {
            var player = e.getPlayer();
            player.damage(50.0);
        }

    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent e) {
        var player = e.getEntity();

        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.getActivePotionEffects().forEach(all -> player.removePotionEffect(all.getType()));

    }

}
