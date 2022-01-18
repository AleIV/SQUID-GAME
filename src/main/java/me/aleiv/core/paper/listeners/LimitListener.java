package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LimitListener implements Listener {

    private final Core instance;
    private int counter = 0;
    private int limit = -1;

    public LimitListener(Core instance) {
        this.instance = instance;

        this.counter = 0;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (instance.getGame().isPlayer(player)) {
            this.addKill();
        }

        if (this.limit != -1 && this.counter >= this.limit) {
            this.instance.getGame().setPvp(Game.PvPType.ONLY_GUARDS);
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (instance.getGame().isGuard(p)) {
                    p.sendMessage("§3Limite alcanzado!!! §7PVP cambiado a ONLY_GUARDS.");
                }
            });
        }
    }

    public void addKill() {
        this.counter++;
    }

    public int getKills() {
        return this.counter;
    }

    public int getLimit() {
        return this.limit;
    }

    public int setLimit(int limit) {
        this.limit = limit+counter;
        return this.limit;
    }
}
