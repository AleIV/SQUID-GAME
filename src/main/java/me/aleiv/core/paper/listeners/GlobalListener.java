package me.aleiv.core.paper.listeners;

import com.destroystokyo.paper.ParticleBuilder;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.aleiv.core.paper.AnimationTools;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.Game.DeathReason;
import me.aleiv.core.paper.Game.GameStage;
import me.aleiv.core.paper.Game.HideMode;
import me.aleiv.core.paper.Game.PvPType;
import me.aleiv.core.paper.Games.GlobalGame.Clothe;
import me.aleiv.core.paper.events.GameStartedEvent;
import me.aleiv.core.paper.events.GameTickEvent;
import me.aleiv.core.paper.objects.Participant;
import me.aleiv.core.paper.objects.Participant.Role;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import us.jcedeno.libs.rapidinv.ItemBuilder;

public class GlobalListener implements Listener {

    Core instance;

    String RED = "<#c11f27>";
    String CYAN = "<#4be2ba>";

    public GlobalListener(Core instance) {
        this.instance = instance;

        /*
         * instance.getProtocolManager().addPacketListener(
         * new PacketAdapter(instance, ListenerPriority.NORMAL,
         * PacketType.Play.Server.GAME_STATE_CHANGE) {
         * 
         * @Override
         * public void onPacketSending(PacketEvent event) {
         * var floats = event.getPacket().getFloat();
         * instance.broadcastMessage(ChatColor.RED + "111");
         * instance.broadcastMessage(ChatColor.GREEN +
         * event.getPacketType().toString());
         * instance.broadcastMessage(floats.getValues().toString());
         * instance.broadcastMessage(ChatColor.GREEN +
         * event.getPacket().getBytes().toString());
         * 
         * 
         * }
         * });
         */
    }

    @EventHandler
    public void onStart(GameStartedEvent e) {
        var game = instance.getGame();
        var mainRoom = game.getMainRoom();
        var globalGame = game.getGlobalGame();
        game.setHideMode(HideMode.INGAME);
        game.setGameStage(GameStage.INGAME);
        game.refreshHide();

        Bukkit.getWorlds().forEach(world -> {
            world.setTime(20000);
        });

        var allPlayers = Bukkit.getOnlinePlayers();

        allPlayers.forEach(player -> {
            instance.sendActionBar(player, Character.toString('\u3400') + "");
            var inv = player.getInventory();
            inv.clear();

            if(game.isPlayer(player))
                globalGame.clothes(player, Clothe.UNIFORM);

        });

        try {
            globalGame.makeAllSleep();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mainRoom.lights(true);

    }

    @EventHandler
    public void onCredits(PlayerRespawnEvent e) {
        var flags = e.getRespawnFlags();
        var player = e.getPlayer();

        player.setGameMode(GameMode.SPECTATOR);

        if (flags.contains(RespawnFlag.END_PORTAL)) {
            // end credits

            Bukkit.getScheduler().runTaskLater(instance, task -> {
                player.kick(MiniMessage.get().parse(RED + "¡Gracias por jugar!"));
            }, 1);

        } else if (!player.hasPermission("admin.perm")) {
            // just died

            e.setRespawnLocation(new Location(Bukkit.getWorld("world"), 18, 69, -6));

            Bukkit.getScheduler().runTaskLater(instance, task -> {
                AnimationTools.sendCredits(player);

            }, 1);

        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        // Cancelled dropping experience
        e.setShouldDropExperience(false);

        var player = e.getEntity();
        var game = instance.getGame();
        var participants = game.getParticipants();
        var uuid = player.getUniqueId().toString();
        var participant = participants.get(uuid);
        var gamemode = player.getGameMode();

        if (participant.getRole() == Role.PLAYER && gamemode == GameMode.ADVENTURE && !participant.isDead()) {
            var damageEvent = player.getLastDamageCause();
            if (damageEvent instanceof EntityDamageByEntityEvent damageEntity
                    && damageEntity.getDamager() instanceof Projectile projectile) {
                AnimationTools.summonDeadBody(player, DeathReason.PROJECTILE, projectile);
            } else {
                var cause = damageEvent.getCause();
                if (cause == DamageCause.BLOCK_EXPLOSION || cause == DamageCause.ENTITY_EXPLOSION) {
                    AnimationTools.summonDeadBody(player, DeathReason.EXPLOSION, null);
                    var effects = instance.getGame().getEffects();
                    var targetLoc = player.getLocation();
                    var players = targetLoc.getNearbyPlayers(15).stream().toList();
                    effects.blood(players);

                } else {
                    AnimationTools.summonDeadBody(player, DeathReason.NORMAL, null);
                }
            }
            participant.setDead(true);

            instance.saveParticipantJson();

        }

        if (player.hasPermission("admin.perm") || game.isGuard(player) || player.getGameMode() != GameMode.ADVENTURE) {
            e.deathMessage(MiniMessage.get().parse(""));

        } else {

            e.deathMessage(MiniMessage.get().parse(CYAN + "Player " + ChatColor.WHITE + "#" + participant.getNumber()
                    + " " + player.getName() + CYAN + " eliminated."));
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        e.quitMessage(MiniMessage.get().parse(""));
        instance.adminMessage(ChatColor.LIGHT_PURPLE + player.getName() + " left the game");
    }

    @EventHandler
    public void onNote(NotePlayEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var game = instance.getGame();
        var participants = game.getParticipants();
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();

        player.setCollidable(false);

        e.joinMessage(MiniMessage.get().parse(""));
        instance.adminMessage(ChatColor.YELLOW + player.getName() + " joined the game");

        if (!participants.containsKey(uuid)) {
            participants.put(uuid, new Participant(uuid, player.getName()));
        }

        var participant = participants.get(uuid);
        player.setLevel(participant.getNumber());

        instance.saveParticipantJson();

        var timer = game.getTimer();
        timer.getBossBar().addPlayer(player);

        var city = game.getCity();
        var whiteLobby = game.getWhiteLobby();

        var world = Bukkit.getWorld("world");
        if (city == null)
            game.setCity(new Location(world, 180.5, 35, 401.5));

        if (whiteLobby == null)
            game.setWhiteLobby(new Location(world, 18, 69, -6));

        if (game.getGameStage() == GameStage.LOBBY) {
            player.teleport(city);

            if (game.isPlayer(player)) {
                var inv = player.getInventory();
                inv.clear();

                var card = new ItemBuilder(Material.BRICK).meta(meta -> meta.setCustomModelData(25)).name("●▲■")
                        .build();
                inv.addItem(card);

            }

        } else if (game.getGameStage() == GameStage.INGAME) {

            if (!player.hasPlayedBefore()) {

                player.teleport(whiteLobby);
            }
        }

        if (game.getLights()) {
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 1000000, 100, false, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
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

            if (game.getGameStage() == GameStage.PAUSE) {
                var players = Bukkit.getOnlinePlayers();
                players.forEach(player -> {
                    if (!player.hasPermission("admin.perm")) {
                        player.addPotionEffect(
                                new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255, false, false, false));
                        instance.showTitle(player, Character.toString('\u0264') + "", "", 0, 40, 0);
                        instance.sendActionBar(player, Character.toString('\u3400') + "");
                    }
                });
            } else if (game.getGameStage() == GameStage.STARTING) {
                var players = Bukkit.getOnlinePlayers();
                var size = game.getParticipants().entrySet().stream()
                        .filter(entry -> entry.getValue().getRole() == Role.PLAYER).toList().size();
                players.forEach(player -> {
                    if (!player.hasPermission("admin.perm")) {
                        player.addPotionEffect(
                                new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255, false, false, false));
                        instance.showTitle(player, Character.toString('\u0265') + "", size + "", 0, 40, 0);
                        instance.sendActionBar(player, Character.toString('\u3400') + "");
                    }
                });
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
            var participants = game.getParticipants();
            var uuid = player.getUniqueId().toString();
            var role = participants.get(uuid).getRole();
            var pvp = game.getPvp();
            var loc = player.getLocation().clone().add(0, 1, 0);

            var animation = 0;

            if (damager instanceof Player playerDamager && pvp != PvPType.ALL) {
                var damagerRole = participants.get(playerDamager.getUniqueId().toString()).getRole();

                if (role == Role.GUARD && role == damagerRole && !playerDamager.hasPermission("admin.perm")) {
                    // GUARD TO GUARD CASE
                    e.setCancelled(true);
                    return;

                } else if (role == Role.GUARD && damagerRole == Role.PLAYER) {
                    // PLAYER TO GUARD CASE
                    e.setCancelled(true);
                    return;

                } else if (role == Role.PLAYER && role == damagerRole && pvp == PvPType.ONLY_GUARDS) {
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
                                    new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true)
                                            .count(100)
                                            .offset(0.1, 0.5, 0.1).extra(0.05).spawn();
                                }
                                    break;

                                case 2: {
                                    // SHOOT CASE
                                    new ParticleBuilder(Particle.TOTEM).location(loc).receivers(20).force(true)
                                            .count(100)
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
