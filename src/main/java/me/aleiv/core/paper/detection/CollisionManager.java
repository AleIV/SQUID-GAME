package me.aleiv.core.paper.detection;

import lombok.Getter;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.detection.commands.PolygonCommand;
import me.aleiv.core.paper.detection.listeners.PolygonListener;
import me.aleiv.core.paper.detection.objects.Polygon;
import me.aleiv.core.paper.detection.task.CheckCollisionsTask;
import org.bukkit.Bukkit;

import java.util.*;

public class CollisionManager {
    private Core instance;
    private @Getter List<Polygon> polygonList;
    private @Getter Map<UUID, Polygon> playerPolygonMap;

    public CollisionManager(Core instance) {
        this.instance = instance;
        this.playerPolygonMap = new HashMap<>();
        this.polygonList = new ArrayList<>();
        this.instance.getCommandManager().registerCommand(new PolygonCommand(this));
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new CheckCollisionsTask(this), 0L, 1L);
        Bukkit.getPluginManager().registerEvents(new PolygonListener(), instance);

    }

}
