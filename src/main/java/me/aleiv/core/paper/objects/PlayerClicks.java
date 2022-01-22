package me.aleiv.core.paper.objects;

import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;

public class PlayerClicks {

    private int leftClicks;
    private int rightClicks;

    public PlayerClicks() {
        this.leftClicks = 0;
        this.rightClicks = 0;
    }

    public PlayerClicks(int leftClicks, int rightClicks) {
        this.leftClicks = leftClicks;
        this.rightClicks = rightClicks;
    }

    public void addLeftClick() {
        this.leftClicks++;
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> this.leftClicks--, 20L);
    }

    public void addRightClick() {
        this.rightClicks++;
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> this.rightClicks--, 20L);
    }

    public int getLeftClicks() {
        return this.leftClicks;
    }

    public int getRightClicks() {
        return this.rightClicks;
    }

    public int getTotalClicks() {
        return this.leftClicks + this.rightClicks;
    }

    @Override
    public PlayerClicks clone() {
        return new PlayerClicks(this.leftClicks, this.rightClicks);
    }

}
