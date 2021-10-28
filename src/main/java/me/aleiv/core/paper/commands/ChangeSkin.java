package me.aleiv.core.paper.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.NonNull;
import me.aleiv.core.paper.Core;

@CommandAlias("changeskin")
public class ChangeSkin extends BaseCommand {

    private @NonNull Core instance;

    public ChangeSkin(Core instance) {
        this.instance = instance;
    }

    @Default
    public void changeSkin(Player player) {
        
    }

}
