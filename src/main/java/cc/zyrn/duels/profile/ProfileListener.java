package cc.zyrn.duels.profile;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class ProfileListener {

    private final ProfileHandler profileHandler;

    @EventHandler
    public final void onAsyncPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {
        if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;

        profileHandler.loadProfile(event.getUniqueId(), true);
    }

    @EventHandler
    public final void onPlayerLoginEvent(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        profileHandler.getProfile(player.getUniqueId()).ifPresent(profile -> {
            if (profile.getProfileData() == null)
                return;

            profile.getProfileData().apply(player);
            profile.setProfileData(null);
        });
    }

    @EventHandler
    public final void onPlayerQuitEvent(PlayerQuitEvent event) {
        profileHandler.getProfile(event.getPlayer().getUniqueId())
                .ifPresent(profile -> profileHandler.handleRemoval(profile, true));
    }

    @EventHandler
    public final void onPlayerKickEvent(PlayerKickEvent event) {
        profileHandler.getProfile(event.getPlayer().getUniqueId())
                .ifPresent(profile -> profileHandler.handleRemoval(profile, true));
    }

}
