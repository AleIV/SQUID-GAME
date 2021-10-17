package me.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class AnimationTools {

    public static Integer speed = 15;

    public static HashMap<String, String> specialObjects = new HashMap<>();

    public static void forceSleep(Player player, Location loc) {
        player.sleep(loc, true);
    }

    public static void forceSleep(List<Player> players, List<Location> beds) {
        var random = new Random();

        for (var player : players) {
            var index = random.nextInt(beds.size());
            var bed = beds.remove(index);
            forceSleep(player, bed);
        }
    }

    public static List<Location> findLocations(String str) {
        var world = Bukkit.getWorld("world");
        return specialObjects.entrySet().stream().filter(entry -> entry.getKey().contains(str))
                .map(entry -> parseLocation(entry.getKey(), world)).collect(Collectors.toList());
    }

    public static Location getNearbyLocation(List<Location> locations, Location location){
        Location nearbyLocation = locations.get(0);
        for (var loc : locations) {
            if(getDistance(nearbyLocation, location) > getDistance(loc, location)){
                nearbyLocation = loc;
            }
        }
        return nearbyLocation;
    }

    public static double getDistance(Location loc1, Location loc2) {
        final int x1 = (int) loc1.getX();
        final int z1 = (int) loc1.getZ();

        final int x2 = (int) loc2.getX();
        final int z2 = (int) loc2.getZ();

        return Math.abs(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2)));
    }

    public static void shootLocation(Location loc) {
        var locations = findLocations("WALL_GUN");
        var wallGun = getNearbyLocation(locations, loc);
        var vector = getVector(loc.add(0, 1.40, 0), wallGun);
        
        wallGun.getWorld().spawnArrow(wallGun, vector, speed, 0);
        

    }

    public static Vector getVector(Location loc1, Location loc2) {
        return loc1.toVector().subtract(loc2.toVector());
    }

    public static void move(String name, Integer value, Integer tickSpeed, char pos, Float distance) {

        var uuid = UUID.fromString(specialObjects.get(name));
        var stand = (ArmorStand) Bukkit.getWorld("world").getEntity(uuid);

        var task = new BukkitTCT();

        var v = Math.abs(value);
        for (int i = 0; i < v; i++) {
            task.addWithDelay(new BukkitRunnable() {
                @Override
                public void run() {
                    var loc = stand.getLocation();
                    var x = loc.getX();
                    var y = loc.getY();
                    var z = loc.getZ();
                    var v = value < 0 ? -distance : distance;
                    var l = loc.clone();
                    switch (pos) {
                        case 'x':
                            l.setX(x + v);
                            break;
                        case 'y':
                            l.setY(y + v);
                            break;
                        case 'z':
                            l.setZ(z + v);
                            break;

                        default:
                            break;
                    }
                    stand.teleport(l);
                }
            }, 50 * tickSpeed);
        }
        task.execute();
    }

    public static void rotate(String name, Integer value, Integer tickSpeed, Float amount) {
        var uuid = UUID.fromString(specialObjects.get(name));
        var stand = (ArmorStand) Bukkit.getWorld("world").getEntity(uuid);

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
                    var v = value < 0 ? y - amount : y + amount;

                    stand.setHeadPose(new EulerAngle(x, v, z));
                }
            }, 50 * tickSpeed);
        }
        task.execute();
    }

    private static List<Location> getBlocksInsideCube(Location loc1, Location loc2) {
        List<Location> locations = new ArrayList<>();

        var xa = (int) (loc1.getX() > loc2.getX() ? loc1.getX() : loc2.getX());
        var ya = (int) (loc1.getY() > loc2.getY() ? loc1.getY() : loc2.getY());
        var za = (int) (loc1.getZ() > loc2.getZ() ? loc1.getZ() : loc2.getZ());

        var xi = (int) (loc1.getX() < loc2.getX() ? loc1.getX() : loc2.getX());
        var yi = (int) (loc1.getY() < loc2.getY() ? loc1.getY() : loc2.getY());
        var zi = (int) (loc1.getZ() < loc2.getZ() ? loc1.getZ() : loc2.getZ());

        for (int x = xi; x <= xa; x++) {
            for (int y = yi; y <= ya; y++) {
                for (int z = zi; z <= za; z++) {
                    locations.add(new Location(loc1.getWorld(), x, y, z));
                }
            }
        }

        return locations;
    }

    public static void fill(Location loc1, Location loc2, Material material) {
        var locations = getBlocksInsideCube(loc1, loc2);
        locations.forEach(loc -> {
            loc.getBlock().setType(material);
        });
    }

    public static ArmorStand getStand(String name) {
        var uuid = UUID.fromString(specialObjects.get(name));
        return (ArmorStand) Bukkit.getWorld("world").getEntity(uuid);
    }

    private static Integer parseInteger(String str) {
        return str.startsWith("-") ? -Integer.parseInt(str.replace("-", "")) : Integer.parseInt(str);
    }

    public static Location parseLocation(String specialObject, World world) {
        var positions = specialObjects.get(specialObject).split(";");
        var x = parseInteger(positions[0]);
        var y = parseInteger(positions[1]);
        var z = parseInteger(positions[2]);

        return new Location(world, x, y, z);
    }

    public static void playSoundDistance(Location loc, Integer distance, String sound, Float volume, Float pitch) {
        loc.getNearbyPlayers(distance).forEach(player -> {
            player.playSound(loc, sound, volume, pitch);
        });
    }

}
