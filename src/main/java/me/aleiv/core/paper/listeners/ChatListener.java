package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Core instance;

    public ChatListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;

        if (!e.getMessage().startsWith("!global!")) {
            e.setCancelled(true);
            String formattedMessage = ChatColor.RED + "[GUARDS] " + ChatColor.WHITE + e.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getMessage();
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (instance.getGame().isGuard(player)) {
                    player.sendMessage(formattedMessage);
                }
            });
        }
        e.setMessage(e.getMessage().replace("!global!", ""));
    }

}
