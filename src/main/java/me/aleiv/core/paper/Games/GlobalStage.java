package me.aleiv.core.paper.Games;

import lombok.Data;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Participant.Role;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Data
public class GlobalStage implements Listener{

    Core instance;

    public GlobalStage(Core instance){
        this.instance = instance;

    }

    public enum Stage{
        PAUSE, INGAME, FINAL, WAITING, KICK
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        var player = e.getPlayer();
        var game = instance.getGame();
        var stage = game.getStage();
        if(stage == Stage.KICK && game.isPlayer(player)){
            player.kick(MiniMessage.get().parse("Zzz"));
        }
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

            if(game.getStage() != Stage.INGAME){

                var participants = game.getParticipants();
                var players = Bukkit.getOnlinePlayers();
                var participantsOnline = participants.values().stream().filter(p -> !p.isDead() && p.isOnline() && p.getRole() == Role.PLAYER).toList();
                var participantsAlive = participants.values().stream().filter(p -> !p.isDead() && p.getRole() == Role.PLAYER).toList();

                if(game.getStage() == Stage.PAUSE){

                    players.forEach(player -> {
                        if (!player.hasPermission("admin.perm") && player.getGameMode() != GameMode.SPECTATOR) {
                            player.addPotionEffect(
                                    new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255, false, false, false));
                            String p = participantsOnline.size() + "/" + "" + participantsAlive.size();
                            instance.showTitle(player, Character.toString('\u0264') + "", p, 0, 40, 0);
                            instance.sendActionBar(player, Character.toString('\u3400') + "");
                        }else{
                            String p = participantsOnline.size() + "/"  + "" + participantsAlive.size();
                            instance.sendActionBar(player, p);
                        }
                    });

                }else if(game.getStage() == Stage.WAITING){
                    players.forEach(player -> {
                        instance.sendActionBar(player, participantsOnline.size() + "/150");
                    });
                }
            }

        });

    }


}
