package me.aleiv.core.paper.listeners;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.ServerOperator;

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
            String formattedMessage = ChatColor.DARK_BLUE + "[OPCHAT] " + ChatColor.WHITE + e.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getMessage();
            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> {
                player.sendMessage(formattedMessage);
            });
        }
        e.setMessage(e.getMessage().replace("!global!", ""));
    }

}
