package me.aleiv.core.paper.Games;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.Frames;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class GlobalGame {
    Core instance;
    
    public GlobalGame(Core instance){
        this.instance = instance;

    }

    public CompletableFuture<Boolean> applyGas(List<Player> players){ 
        return playAnimation(players, 3402, 3601);
    }

    public CompletableFuture<Boolean> sendCountDown(List<Player> players){ 
        return playAnimation(players, 3602, 4392);
    }

    public CompletableFuture<Boolean> playAnimation(List<Player> players, Integer from, Integer until){
        
        var task = new BukkitTCT();

        var animation = Frames.getFramesCharsIntegersAll(from, until);

        animation.forEach(frame -> {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        instance.showTitle(player, frame + "", "", 0, 20, 0);
                    });

                }

            }, 50);
        });

        return task.execute();
    }
}
