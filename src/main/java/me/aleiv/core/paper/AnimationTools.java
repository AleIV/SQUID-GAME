package me.aleiv.core.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.aleiv.core.paper.objects.NoteBlockData;
import me.aleiv.core.paper.utilities.LineVector;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class AnimationTools {

    public static HashMap<String, String> specialObjects = new HashMap<>();
    public static HashMap<String, NoteBlockData> noteBlocksMain = new HashMap<String, NoteBlockData>() {{
        put("P", new NoteBlockData(0, 0, Instrument.BASS_DRUM));
        put("L", new NoteBlockData(0, 1, Instrument.BASS_DRUM));
        put("A", new NoteBlockData(0, 2, Instrument.BASS_DRUM));
        put("Y", new NoteBlockData(0, 3, Instrument.BASS_DRUM));
        put("E", new NoteBlockData(0, 4, Instrument.BASS_DRUM));
        put("R", new NoteBlockData(0, 5, Instrument.BASS_DRUM));
        put("S", new NoteBlockData(0, 6, Instrument.BASS_DRUM));
        put("$", new NoteBlockData(1, 0, Instrument.BASS_DRUM));
        put("I", new NoteBlockData(1, 1, Instrument.BASS_DRUM));
        put("Z", new NoteBlockData(1, 2, Instrument.BASS_DRUM));
        put("0", new NoteBlockData(1, 3, Instrument.BASS_DRUM));
        put("1", new NoteBlockData(1, 4, Instrument.BASS_DRUM));
        put("2", new NoteBlockData(1, 5, Instrument.BASS_DRUM));
        put("3", new NoteBlockData(1, 6, Instrument.BASS_DRUM));

        put("4", new NoteBlockData(0, 0, Instrument.GUITAR));
        put("5", new NoteBlockData(0, 1, Instrument.GUITAR));
        put("6", new NoteBlockData(0, 2, Instrument.GUITAR));
        put("7", new NoteBlockData(0, 3, Instrument.GUITAR));
        put("8", new NoteBlockData(0, 4, Instrument.GUITAR));
        put("9", new NoteBlockData(0, 5, Instrument.GUITAR));

    }};

    public static HashMap<String, NoteBlockData> noteBlocksCount = new HashMap<String, NoteBlockData>() {{
        put(":", new NoteBlockData(0, 0, Instrument.BANJO));
        put("0", new NoteBlockData(0, 1, Instrument.BANJO));
        put("1", new NoteBlockData(0, 2, Instrument.BANJO));
        put("2", new NoteBlockData(0, 3, Instrument.BANJO));
        put("3", new NoteBlockData(0, 4, Instrument.BANJO));
        put("4", new NoteBlockData(0, 5, Instrument.BANJO));
        put("5", new NoteBlockData(0, 6, Instrument.BANJO));
        put("6", new NoteBlockData(1, 0, Instrument.BANJO));
        put("7", new NoteBlockData(1, 1, Instrument.BANJO));
        put("8", new NoteBlockData(1, 2, Instrument.BANJO));
        put("9", new NoteBlockData(1, 3, Instrument.BANJO));

    }};

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
                .map(entry -> parseLocation(entry.getValue(), world)).collect(Collectors.toList());
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
        
        wallGun.getWorld().spawnArrow(wallGun, vector, 20, 0);
        AnimationTools.playSoundDistance(wallGun, 300, "squid:sfx.dramatic_shot", 1f, 1f);
        var origin = wallGun.toVector();
        var target = loc.toVector();
        var vectors = LineVector.of(origin, target).getPointsInBetween();

        vectors.forEach(v ->{
            var l = v.toLocation(Bukkit.getWorld("world"));
            new ParticleBuilder(Particle.COMPOSTER).location(l).receivers(300).force(true).count(100).offset(0.000001, 0.000001, 0.000001).extra(0).spawn();
        });
        

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

    public static List<Location> getBlocksInsideCube(Location loc1, Location loc2) {
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

    public static Location parseLocation(String loc, World world) {
        var positions = loc.split(";");
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

    public static void setScreenValue(List<Location> locations, String str){
        var array = str.toCharArray();
        var count = 0;
        for (char c : array) {
            var loc = locations.get(count);
            setBlockValue(true, loc, String.valueOf(c));
            count++;
        }

    }

    public static void setTimerValue(List<Location> locations, String str){
        var array = str.toCharArray();
        var count = 0;
        for (char c : array) {
            var loc = locations.get(count);
            setBlockValue(false, loc, String.valueOf(c));
            count++;
        }

    }

    public static void setBlockValue(Boolean mainMap, Location location, String note){
        HashMap<String, NoteBlockData> map;
        if(mainMap){
            map = noteBlocksMain;
        }else{
            map = noteBlocksCount;
        }
        var n = map.get(note);
        NoteBlock noteBlock = (NoteBlock) Material.NOTE_BLOCK.createBlockData();
        noteBlock.setNote(n.getNote());
        noteBlock.setInstrument(n.getInstrument());
        location.getBlock().setType(Material.NOTE_BLOCK);
        location.getBlock().setBlockData(noteBlock);

    }

    public static String getFormattedNumber(Integer i, Integer zeros){
        var str = new StringBuilder();
        for (int j = 0; j < zeros; j++) {
            str.append("0");
        }
        str.append(i);
        return str.toString();
    }

}
