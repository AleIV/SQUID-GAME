package me.aleiv.core.paper.Games;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Data;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Participant.Role;

@Data
public class GlobalStage implements Listener{

    Core instance;

    public GlobalStage(Core instance){
        this.instance = instance;

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("cinema", c -> {
            return Arrays.stream(Stage.values()).map(m -> m.toString()).toList();
        });

    }

    public enum Stage{
        PAUSE, INGAME, FINAL, WAITING
    }

    
    @EventHandler
    public void gameTickEvent(GameTickEvent e) {
        var game = instance.getGame();
        Bukkit.getScheduler().runTask(instance, () -> {

            var timer = game.getTimer();
            if (timer.isActive()) {
                var currentTime = (int) game.getGameTime();
                timer.refreshTime(currentTime);
            }

            if (game.getStage() == Stage.PAUSE) {
                var players = Bukkit.getOnlinePlayers();
                players.forEach(player -> {
                    if (!player.hasPermission("admin.perm")) {
                        player.addPotionEffect(
                                new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255, false, false, false));
                        instance.showTitle(player, Character.toString('\u0264') + "", "", 0, 40, 0);
                        instance.sendActionBar(player, Character.toString('\u3400') + "");
                    }
                });
            } else if (game.getStage() == Stage.FINAL) {
                var players = Bukkit.getOnlinePlayers();
                var size = game.getParticipants().entrySet().stream()
                        .filter(entry -> entry.getValue().getRole() == Role.PLAYER).toList().size();
                players.forEach(player -> {
                    if (!player.hasPermission("admin.perm")) {
                        player.addPotionEffect(
                                new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255, false, false, false));
                        instance.showTitle(player, Character.toString('\u0265') + "", size + "", 0, 40, 0);
                        instance.sendActionBar(player, Character.toString('\u3400') + "");
                    }
                });
            }

        });

    }


}
