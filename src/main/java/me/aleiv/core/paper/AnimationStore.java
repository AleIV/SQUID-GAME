package me.aleiv.core.paper;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class AnimationStore {

    Core instance;

    public AnimationStore(Core instance) {
        this.instance = instance;

    }

    public static void forceSleep(Player player, Location loc){
        player.sleep(loc, true);
    }

    public static void move(ArmorStand stand, Integer value, Integer tickSpeed, char pos){
        var task = new BukkitTCT();
        
        var v = Math.abs(value);
        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable(){
                @Override
                public void run() {
                    var loc = stand.getLocation();
                    var x = loc.getX();
                    var y = loc.getY();
                    var z = loc.getZ();
                    var v = value < 0 ? -0.1 : 0.1;
                    var l = loc.clone();
                    switch (pos) {
                        case 'x': l.setX(x+v); break;
                        case 'y': l.setY(y+v); break;
                        case 'z': l.setZ(z+v); break;
                    
                        default: break;
                    }
                    stand.teleport(l);
                }
            }, 50*tickSpeed);
        }
        task.execute();
    }

    public static void rotate(ArmorStand stand, Integer value, Integer tickSpeed) {
        var task = new BukkitTCT();

        var v = Math.abs(value);
        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    var part = stand.getHeadPose();
                    var x = part.getX();
                    var y = part.getY();
                    var z = part.getZ();
                    var v = value < 0 ? y - 0.1 : y + 0.1;

                    stand.setHeadPose(new EulerAngle(x, v, z));
                }
            }, 50 * tickSpeed);
        }
        task.execute();
    }
}
