package me.aleiv.core.paper.detection;

import org.bukkit.Bukkit;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.detection.task.CheckCollisionsTask;

public class CollisionManager {
    private Core instance;

    public CollisionManager(Core instance) {
        this.instance = instance;
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new CheckCollisionsTask(this), 0L, 2L);

    }

}
