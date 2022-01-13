package me.aleiv.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import lombok.NonNull;
import me.aleiv.core.paper.Core;

@CommandAlias("days")
@CommandPermission("admin.perm")
public class DaysCMD extends BaseCommand {

    private @NonNull Core instance;

    public DaysCMD(Core instance) {
        this.instance = instance;

    }
}
