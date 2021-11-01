package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.DeathReason;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Game.PvPType;
import me.aleiv.core.paper.Game.Role;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

public class GlobalListener implements Listener {

    Core instance;

    public GlobalListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        var player = e.getEntity();
        var game = instance.getGame();
        var roles = game.getRoles();
        var uuid = player.getUniqueId().toString();
        var role = roles.get(uuid);
        var gamemode = player.getGameMode();
        if(role == Role.PLAYER && gamemode == GameMode.ADVENTURE){
            var damageEvent = player.getLastDamageCause();
            if(damageEvent instanceof EntityDamageByEntityEvent damageEntity && damageEntity.getDamager() instanceof Projectile projectile){
                AnimationTools.summonDeadBody(player, DeathReason.PROJECTILE, projectile);
            }else{
                var cause = damageEvent.getCause();
                if(cause == DamageCause.BLOCK_EXPLOSION || cause == DamageCause.ENTITY_EXPLOSION){
                    AnimationTools.summonDeadBody(player, DeathReason.EXPLOSION, null);

                }else{
                    AnimationTools.summonDeadBody(player, DeathReason.NORMAL, null);
                }
            }
            roles.put(uuid, Role.DEAD);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var game = instance.getGame();
        var roles = game.getRoles();
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();

        if (!roles.containsKey(uuid)) {
            if (player.hasPermission("admin.perm")) {
                roles.put(uuid, Role.GUARD);
            } else {
                roles.put(uuid, Role.PLAYER);
            }
        }

        var timer = game.getTimer();
        timer.getBossBar().addPlayer(player);

        var city = new Location(Bukkit.getWorld("world"), 180.5, 35, 401.5);
        if (game.getGameStage() == GameStage.LOBBY) {
            player.teleport(city);

        } else if(!player.hasPlayedBefore()){
            player.teleport(city);
        }

        if (game.getLights()) {
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 1000000, 100, false, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }

    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent e) {
        var player = e.getEntity();

        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);

        // player.getActivePotionEffects().forEach(all ->
        // player.removePotionEffect(all.getType()));
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason().toString().contains("NATURAL")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void gameTickEvent(GameTickEvent e) {
        var game = instance.getGame();
        Bukkit.getScheduler().runTask(instance, () -> {

            var timer = game.getTimer();
            if (timer.isActive()) {
                var currentTime = (int) game.getGameTime();
                timer.refreshTime(currentTime);
            }

        });

    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        var game = instance.getGame();
        if (game.getGameStage() != GameStage.LOBBY)
            return;

        var player = e.getPlayer();

        if (game.isPlayer(player)) {
            var from = e.getFrom();
            var to = e.getTo();
            var x1 = from.getX();
            var z1 = from.getZ();
            var x2 = to.getX();
            var z2 = to.getZ();
            if (x1 != x2 || z1 != z2) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent e) {
        var entity = e.getEntity();
        var damager = e.getDamager();
        if (entity instanceof Player player) {

            var game = instance.getGame();
            var roles = game.getRoles();
            var uuid = player.getUniqueId().toString();
            var role = roles.get(uuid);
            var pvp = game.getPvp();
            var loc = player.getLocation().clone().add(0, 1, 0);

            var animation = 0;

            if (damager instanceof Player playerDamager && pvp != PvPType.ALL) {
                var damagerRole = roles.get(playerDamager.getUniqueId().toString());

                if (role == Role.GUARD && role == damagerRole && !playerDamager.hasPermission("admin.perm")) {
                    // GUARD TO GUARD CASE
                    e.setCancelled(true);
                    return;

                } else if (role == Role.GUARD && damagerRole == Role.PLAYER){
                    // PLAYER TO GUARD CASE
                    e.setCancelled(true);
                    return;

                }else if (role == Role.PLAYER && role == damagerRole && pvp == PvPType.ONLY_GUARDS) {
                    // PLAYER TO PLAYER CASE
                    e.setCancelled(true);
                    return;
                }

            } else if (damager instanceof Projectile) {
                animation = 2;
                e.setDamage(30);
            }

            var inv = player.getInventory();
            var item = inv.getItemInMainHand();
            if (item != null && item.getType().toString().contains("SWORD")) {
                animation = 1;
            }

            if (animation == 0) {
                new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true).count(20)
                        .offset(0.0001, 0.0001, 0.0001).extra(0.05).spawn();
            } else {

                var task = new BukkitTCT();
                final var a = animation;

                for (int i = 0; i < 5; i++) {
                    task.addWithDelay(new BukkitRunnable() {
                        @Override
                        public void run() {
                            var loc = player.getLocation();
                            switch (a) {
                            case 1: {
                                // KNIFE CASE
                                new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true).count(100)
                                        .offset(0.1, 0.5, 0.1).extra(0.05).spawn();
                            }
                                break;

                            case 2: {
                                // SHOOT CASE
                                new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true).count(100)
                                        .offset(0.1, 0.5, 0.1).extra(0.05).spawn();
                            }
                                break;

                            default:
                                break;
                            }

                        }
                    }, 50 * 2);
                }

                task.execute();
            }

        }
    }

}
