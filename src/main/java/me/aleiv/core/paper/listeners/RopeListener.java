package me.aleiv.core.paper.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.aleiv.core.paper.Core;

public class RopeListener implements Listener {

    Core instance;

    public RopeListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerInteractAtEntityEvent e) {
        // var game = instance.getGame();
        var entity = e.getRightClicked();
        if (entity instanceof ArmorStand stand) {

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var game = instance.getGame();
        var player = e.getPlayer();

        var rope = game.getRopeGame();
        rope.addBossBars(player);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        var rope = instance.getGame().getRopeGame();
        var moving = rope.getPlayerMoving();
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        if (moving.containsKey(uuid)) {
            moving.remove(uuid);
        }

    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        var rope = instance.getGame().getRopeGame();
        var moving = rope.getPlayerMoving();
        if (moving.isEmpty()) return;

        var player = e.getPlayer();
        var uuid = player.getUniqueId();

        if(!moving.containsKey(uuid)) return;

        var y = player.getLocation().getY();
        var doors = moving.get(uuid);

        if (y >= 62) {
            doors.forEach(door -> {
                var newLoc = door.getLocation();
                newLoc.setY(62);
                door.teleport(newLoc);
            });
        } else if (y <= 34) {
            doors.forEach(door -> {
                var newLoc = door.getLocation();
                newLoc.setY(34);
                door.teleport(newLoc);
            });
        } else {
            doors.forEach(door -> {
                var newLoc = door.getLocation();
                newLoc.setY(y);
                door.teleport(newLoc);
            });
        }

    }

}
