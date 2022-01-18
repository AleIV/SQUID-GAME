package me.aleiv.core.paper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.Participant;
import me.aleiv.core.paper.objects.Participant.Role;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("players")
@CommandPermission("admin.perm")
public class PlayersCMD extends BaseCommand {

    private @NonNull Core instance;

    public PlayersCMD(Core instance) {
        this.instance = instance;
    }

    @Subcommand("role")
    @CommandCompletion("@players")
    public void role(CommandSender sender, @Flags("other") Player player, Role role) {
        var game = instance.getGame();
        var participants = game.getParticipants();
        var uuid = player.getUniqueId().toString();

        if(participants.containsKey(uuid)){
            var participant = participants.get(uuid);
            participant.setRole(role);

            if(role == Role.PLAYER){
                participant.chooseNumber();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set " + participant.getName() + " number " + participant.getNumber());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join PARTICIPANT " + participant.getName());

            }else{
                participant.setNumber(0);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set " + participant.getName() + " number " + 0);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join funny " + participant.getName());
            }

            player.setLevel(participant.getNumber());
            
            sender.sendMessage(ChatColor.DARK_AQUA + player.getName() + " role set to " + role.toString().toLowerCase());

            instance.saveParticipantJson();
        }
    }

    @Subcommand("dead")
    @CommandCompletion("@players @bool")
    public void dead(CommandSender sender, @Flags("other") Player player, Boolean dead) {
        var game = instance.getGame();
        var participants = game.getParticipants();
        var uuid = player.getUniqueId().toString();

        if(participants.containsKey(uuid)){
            var participant = participants.get(uuid);
            participant.setDead(dead);
            
            sender.sendMessage(ChatColor.DARK_AQUA + player.getName() + " dead mode set to " + dead);

            instance.saveParticipantJson();
        }
    
    }

    @Subcommand("reset-roles")
    public void roleGlobal(CommandSender sender) {
        var game = instance.getGame();
        var participants = game.getParticipants();

        participants.clear();

        Bukkit.getOnlinePlayers().forEach(player -> {
            var uuid = player.getUniqueId().toString();
            participants.put(uuid, new Participant(uuid, player.getName()));
        });

        instance.saveParticipantJson();

        sender.sendMessage(ChatColor.DARK_AQUA + "Reset roles");
    }

    @Subcommand("alive")
    public void checkAlive(CommandSender sender) {
        var game = instance.getGame();
        var participants = game.getParticipants();

        var alive = participants.entrySet().stream().filter(entry-> entry.getValue().getRole() ==  Role.PLAYER && !entry.getValue().isDead()).toList();
        var names = alive.stream().map(p -> p.getValue().getName()).toList().toString();
            
        sender.sendMessage(ChatColor.AQUA + "There are " + alive.size() + " alive players.");
        sender.sendMessage(ChatColor.YELLOW + names);
        
    }

    @Subcommand("dead")
    public void checkDead(CommandSender sender) {
        var game = instance.getGame();
        var participants = game.getParticipants();

        var dead = participants.entrySet().stream().filter(entry-> entry.getValue().getRole() == Role.PLAYER && entry.getValue().isDead()).toList();
        var names = dead.stream().map(p -> p.getValue().getName()).toList().toString();
            
        sender.sendMessage(ChatColor.DARK_RED + "There are " + dead.size() + " dead players.");
        sender.sendMessage(ChatColor.YELLOW + names);
        
    }

    //@Subcommand("count")
    public void getCount(CommandSender sender, Integer i) {

        var to = 0;
        var count = 1;
        while(count < i){
            var participant = getNum(count);
            if(participant == null) break;
            if(!participant.isDead()){
                to = count;
                count++;
            }
        }
        

        sender.sendMessage(ChatColor.DARK_RED + "There are " + i + " players from 1 to " + to);
        
    }

    public Participant getNum(int i){
        var game = instance.getGame();
        var participantsList = game.getParticipants();
        return participantsList.values().stream().filter(v-> v.getNumber() == i).findAny().orElse(null);
    }

    @Subcommand("guards")
    public void checkGuards(CommandSender sender) {
        var game = instance.getGame();
        var participants = game.getParticipants();

        var guards = participants.entrySet().stream().filter(entry-> entry.getValue().getRole() == Role.GUARD).toList();
        var names = guards.stream().map(p -> p.getValue().getName()).toList().toString();
            
        sender.sendMessage(ChatColor.RED + "There are " + guards.size() + " guards.");
        sender.sendMessage(ChatColor.YELLOW + names);
        
    }

    @Subcommand("info")
    public void checkall(CommandSender sender) {
        var game = instance.getGame();
        var participants = game.getParticipants();

        var registed = participants.entrySet();
        var players = registed.stream().filter(entry-> entry.getValue().getRole() == Role.PLAYER).toList();
        var guards = registed.stream().filter(entry-> entry.getValue().getRole() == Role.GUARD).toList();
        var alive = registed.stream().filter(entry-> entry.getValue().getRole() ==  Role.PLAYER && !entry.getValue().isDead()).toList();
        var dead = registed.stream().filter(entry-> entry.getValue().getRole() == Role.PLAYER && entry.getValue().isDead()).toList();
            
        sender.sendMessage(ChatColor.GOLD + "There are " + registed.size() + " registed players.");
        sender.sendMessage(ChatColor.GOLD + "There are " + guards.size() + " guards.");
        sender.sendMessage(ChatColor.GOLD + "There are " + players.size() + " participants.");
        sender.sendMessage(ChatColor.YELLOW + "There are " + alive.size() + " alive players.");
        sender.sendMessage(ChatColor.YELLOW + "There are " + dead.size() + " dead players.");
        
    }
    
}
