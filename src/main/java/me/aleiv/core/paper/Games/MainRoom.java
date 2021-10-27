package me.aleiv.core.paper.Games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class MainRoom {
    Core instance;
    
    public MainRoom(Core instance){
        this.instance = instance;

    }

    public void makeAllSleep(){
        var players = Bukkit.getOnlinePlayers().stream().map(player -> (Player) player).toList();
        var beds = AnimationTools.findLocations("BED");
        AnimationTools.forceSleep(players, beds);
    }

    public void moveTube(Boolean bool){
        var loc = new Location(Bukkit.getWorld("world"), 282, 30, -105);
        if(bool){
            
            AnimationTools.move("TUBE", -100, 1, 'y', 0.2f);
            AnimationTools.playSoundDistance(loc, 100, "squid:sfx.piggybank_tube_down", 1f, 1f);
        }else{
            AnimationTools.move("TUBE", 100, 1, 'y', 0.2f);
            AnimationTools.playSoundDistance(loc, 100, "squid:sfx.piggybank_tube_up", 1f, 1f);
        }
    }
    
    public void lights(Boolean bool){
        instance.getGame().setLights(bool);
        if(bool){
            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*1000000, 100, false, false, false));
                player.playSound(loc, "squid:sfx.main_lights_on", 1, 1);
            });

        }else{
            Bukkit.getOnlinePlayers().forEach(player->{
                var loc = player.getLocation();
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(loc, "squid:sfx.main_lights_off", 1, 1);
            });

        }
    } 

    public void mainElevator(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("MAIN_ELEVATOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("MAIN_ELEVATOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.main_elevator_open", 1f, 1f);

            AnimationTools.move("MAIN_RIGHT_ELEVATOR", 33, 1, 'x', 0.1f);
            AnimationTools.move("MAIN_LEFT_ELEVATOR", -33, 1, 'x', 0.1f);

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 40, "squid:sfx.main_elevator_close", 1f, 1f);

            AnimationTools.move("MAIN_RIGHT_ELEVATOR", -33, 1, 'x', 0.1f);
            AnimationTools.move("MAIN_LEFT_ELEVATOR", 33, 1, 'x', 0.1f);

        }
    }

    public void mainLeftDoor(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("MAIN_LEFT_DOOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("MAIN_LEFT_DOOR_POS2"), Bukkit.getWorld("world"));
        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_open", 1f, 1f);

            AnimationTools.rotate("MAIN_LEFT_DOOR", 25, 1, 0.1f);
        

        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_close", 1f, 1f);

            AnimationTools.rotate("MAIN_LEFT_DOOR", -25, 1, 0.1f);

        }
    }

    public void mainRightDoor(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("MAIN_RIGHT_DOOR_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("MAIN_RIGHT_DOOR_POS2"), Bukkit.getWorld("world"));

        if(bool){

            AnimationTools.fill(loc1, loc2, Material.AIR);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_open", 1f, 1f);

            AnimationTools.rotate("MAIN_RIGHT_DOOR", 25, 1, 0.1f);
        
        }else{

            AnimationTools.fill(loc1, loc2, Material.BARRIER);

            AnimationTools.playSoundDistance(loc1, 15, "squid:sfx.main_door_close", 1f, 1f);

            AnimationTools.rotate("MAIN_RIGHT_DOOR", -25, 1, 0.1f);

        }
    }

    public void screenPlayers(){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PLAYERS_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PLAYERS_POS2"), Bukkit.getWorld("world"));
        var playersText = AnimationTools.getBlocksInsideCube(loc1, loc2);
        AnimationTools.setScreenValue(playersText, "PLAYERS");
    }

    public void screenPrize(){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PRIZE_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PRIZE_POS2"), Bukkit.getWorld("world"));
        var prizeText = AnimationTools.getBlocksInsideCube(loc1, loc2);
        AnimationTools.setScreenValue(prizeText, "PRIZE");
    }

    public void turnScreen(Boolean bool){
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("SCREEN_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("SCREEN_POS2"), Bukkit.getWorld("world"));
        var game = instance.getGame();

        if(bool){
            AnimationTools.fill(loc1, loc2, Material.YELLOW_TERRACOTTA);
            screenPlayers();
            screenPrize();
            var currentPrizeText = game.getTotalPrize();
            var currentPlayersText = game.getTotalPlayers();
            var prize = Integer.parseInt(currentPrizeText);
            var players = Integer.parseInt(currentPlayersText);
            refreshPrize(prize, 1, 1);
            refreshPlayers(players, 1, 1);
            
        }else{
            AnimationTools.fill(loc1, loc2, Material.YELLOW_TERRACOTTA);
            
        }
    }

    public void refreshPrize(Integer newNumber, Integer delay, Integer value){
        if(newNumber < 0) return;
        var specialObjects = AnimationTools.specialObjects;
        var dolar = AnimationTools.parseLocation(specialObjects.get("SCREEN_DOLAR"), Bukkit.getWorld("world"));
        AnimationTools.setBlockValue(true, dolar, "$");

        var loc1 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PRIZE_VALUE_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PRIZE_VALUE_POS2"), Bukkit.getWorld("world"));

        var game = instance.getGame();
        var currentPrizeText = game.getTotalPrize();
        var prizeValueText = AnimationTools.getBlocksInsideCube(loc1, loc2);

        var fromPrize = Integer.parseInt(currentPrizeText);

        var addOrRemove = newNumber > fromPrize;

        var from = fromPrize;
        var to = newNumber;

        if(addOrRemove){
            to++;
        }else{
            to--;
        }

        var task = new BukkitTCT();

        var count = from;
        
        while (count != to) {

            final var j = count;
            task.addWithDelay(new BukkitRunnable(){
                @Override
                public void run() {
                    var st = j + "";
                    var arraySt = st.toCharArray();
                    var formatted = AnimationTools.getFormattedNumber(j, 6-arraySt.length);

                    game.setTotalPrize(formatted);
                    AnimationTools.setScreenValue(prizeValueText, formatted);
                    AnimationTools.playSoundDistance(loc1, 100, "squid:sfx.main_board", 1f, 1f);
                }
            }, 50*delay);

            if(addOrRemove){
                if(count+value > to){
                    count = to;
                }else{
                    count+=value;
                }
                
            }else{
                if(count-value < to){
                    count = to;
                }else{
                    count-=value;
                }
            }

        }

        task.execute();

    }

    public void refreshPlayers(Integer newNumber, Integer delay, Integer value){
        if(newNumber < 0) return;
        var specialObjects = AnimationTools.specialObjects;
        var loc1 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PLAYERS_VALUE_POS1"), Bukkit.getWorld("world"));
        var loc2 = AnimationTools.parseLocation(specialObjects.get("SCREEN_PLAYERS_VALUE_POS2"), Bukkit.getWorld("world"));

        var game = instance.getGame();
        var currentPlayersText = game.getTotalPlayers();
        var playersValueText = AnimationTools.getBlocksInsideCube(loc1, loc2);

        var fromPlayers = Integer.parseInt(currentPlayersText);

        var addOrRemove = newNumber > fromPlayers;

        var from = fromPlayers;
        var to = newNumber;

        if(addOrRemove){
            to++;
        }else{
            to--;
        }

        var task = new BukkitTCT();

        var count = from;
        
        while (count != to) {

            final var j = count;
            task.addWithDelay(new BukkitRunnable(){
                @Override
                public void run() {
                    var st = j + "";
                    var arraySt = st.toCharArray();
                    var formatted = AnimationTools.getFormattedNumber(j, 3-arraySt.length);

                    game.setTotalPlayers(formatted);
                    AnimationTools.setScreenValue(playersValueText, formatted);
                    AnimationTools.playSoundDistance(loc1, 100, "squid:sfx.main_board", 1f, 1f);
                }
            }, 50*delay);

            if(addOrRemove){
                if(count+value > to){
                    count = to;
                }else{
                    count+=value;
                }
                
            }else{
                if(count-value < to){
                    count = to;
                }else{
                    count-=value;
                }
            }

        }

        task.execute();

    }
}
