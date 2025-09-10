package cc.zyrn.duels.match.listener;

import cc.zyrn.duels.Duels;
import cc.zyrn.duels.match.Match;
import cc.zyrn.duels.match.MatchState;
import cc.zyrn.duels.profile.Profile;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

@AllArgsConstructor
public class MatchListener implements Listener {

    private final Duels duels;

    @EventHandler
    public final void onPlayerMoveEvent(PlayerMoveEvent event) {
        duels.getProfileHandler().getProfile(event.getPlayer().getUniqueId()).ifPresent(profile -> {
            if (profile.getMatch() == null || !profile.getMatch().getMatchState().equals(MatchState.STARTING))
                return;

            if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() ||
                    event.getTo().getZ() != event.getFrom().getZ())
                event.setCancelled(true);
        });
    }

    @EventHandler
    public final void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        duels.getProfileHandler().getProfile(player.getUniqueId()).ifPresent(profile -> {
            if (profile.getMatch() == null)
                return;

            final Match match = profile.getMatch();

            player.spigot().respawn();
            match.cancel(player.getUniqueId());
        });
    }

    @EventHandler
    public final void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        final Optional<Profile> optionalEntityProfile = duels.getProfileHandler().getProfile(event.getEntity().getUniqueId());
        final Optional<Profile> optionalDamagerProfile = duels.getProfileHandler().getProfile(event.getDamager().getUniqueId());

        if (optionalEntityProfile.isEmpty()) {
            if (optionalDamagerProfile.isEmpty()) {
                return;
            }

            if (optionalDamagerProfile.get().getMatch() != null) {
                event.setCancelled(true);
                return;
            }

            return;
        }

        if (optionalDamagerProfile.isEmpty()) {
            if (optionalEntityProfile.get().getMatch() != null) {
                event.setCancelled(true);

                return;
            }

            if (optionalEntityProfile.get().getMatch() != null || optionalDamagerProfile.get().getMatch() != null) {
                if (optionalEntityProfile.get().getMatch() == optionalDamagerProfile.get().getMatch())
                    return;

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onPlayerBreakBlockEvent(BlockBreakEvent event) {
        duels.getProfileHandler().getProfile(event.getPlayer().getUniqueId()).ifPresent(profile -> {
            if (profile.getMatch() != null)
                event.setCancelled(true);
        });
    }

    @EventHandler
    public final void onPlayerBuildEvent(BlockCanBuildEvent event) {
        duels.getProfileHandler().getProfile(event.getPlayer().getUniqueId()).ifPresent(profile -> {
            if (profile.getMatch() != null)
                event.setBuildable(false);
        });
    }

}
