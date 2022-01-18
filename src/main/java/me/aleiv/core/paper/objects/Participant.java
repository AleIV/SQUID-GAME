package me.aleiv.core.paper.objects;

import lombok.Data;
import me.aleiv.core.paper.Core;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.UUID;

@Data   
public class Participant {
    
    String uuid;
    Role role = Role.PLAYER;
    boolean dead = false;
    String name;
    int number;

    public Participant(String uuid, String name){
        this.uuid = uuid;
        this.name = name;
        
        this.number = getAvailableNumber();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set " + name + " number " + number);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join PARTICIPANT " + name);

        var player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player != null){
            player.setLevel(number);
        }
    }

    public Participant(String uuid, Role role, boolean dead, String name, int number){
        this.uuid = uuid;
        this.role = role;
        this.dead = dead;
        this.name = name;
        this.number = number;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set " + name + " number " + number);

        if(role == Role.PLAYER){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join PARTICIPANT " + name);
        }else{
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join funny " + name);
        }
        
    
        var player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player != null){
            player.setLevel(number);
        }
    }

    public void chooseNumber(){
        this.number = getAvailableNumber();
    }

    private int getAvailableNumber(){
        var count = 1;
        var participants = Core.getInstance().getGame().getParticipants().values();
        while(contains(participants, count)){
            count++;
        }
        return count;
    }

    private boolean contains(Collection<Participant> participants, int n){
        return !participants.stream().filter(par -> par.getNumber() == n).toList().isEmpty();
    }

    public enum Role {
        GUARD, PLAYER, VIP, SPEC
    }

}
