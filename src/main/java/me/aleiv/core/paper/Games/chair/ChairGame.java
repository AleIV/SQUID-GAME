package me.aleiv.core.paper.Games.chair;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import lombok.Getter;
import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;

public class ChairGame {
    Core instance;

    @Getter
    boolean music = false;

    @Getter
    List<Location> discoNotes = new ArrayList<>();

    public ChairGame(Core instance) {
        this.instance = instance;

    }

    public void turnMusic(Boolean bool) {

        if (discoNotes.isEmpty()) {
            discoNotes = AnimationTools.findLocations("DISCO_NOTES");
        }
        this.music = bool;

    }

}
