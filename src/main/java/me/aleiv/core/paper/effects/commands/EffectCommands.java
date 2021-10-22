package me.aleiv.core.paper.effects.commands;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import de.slikey.effectlib.effect.TextEffect;
import me.aleiv.core.paper.Core;

@CommandAlias("create-effect")
public class EffectCommands extends BaseCommand {

    private Core instance;

    public EffectCommands(Core instance) {
        this.instance = instance;
    }

    @Default
    public void effectDemo(Player sender, Integer seconds) {

        var effect = new TextEffect(instance.getEffectManager());
        effect.text = "Hello World";
        effect.setLocation(sender.getLocation());
        effect.particle = Particle.COMPOSTER;
        effect.period = 1;
        effect.iterations = 20 * seconds;
        effect.start();
    }
}
