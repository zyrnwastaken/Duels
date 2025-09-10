package cc.zyrn.duels.match;

import cc.zyrn.duels.arena.Arena;
import cc.zyrn.duels.kit.Kit;
import cc.zyrn.duels.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Match {

    private final Profile playerOneProfile, playerTwoProfile;

    private final Arena arena;
    private final Kit kit;

    protected final Player playerOne, playerTwo;

    private MatchState matchState;
    private int countdown;

    public Match(Profile playerOne, Profile playerTwo, Arena arena, Kit kit) {
        this.playerOneProfile = playerOne;
        this.playerTwoProfile = playerTwo;

        this.playerOne = Bukkit.getPlayer(playerOne.getUuid());
        this.playerTwo = Bukkit.getPlayer(playerTwo.getUuid());

        this.arena = arena;
        this.kit = kit;

        this.matchState = MatchState.STARTING;
        this.countdown = 5;

        arena.setInUse(true);
    }

    public final void start() {
        if (playerOne == null && playerTwo == null) {
            this.cancel(null);
            return;
        }

        if (playerOne == null) {
            this.cancel(playerOneProfile.getUuid());
            return;
        }

        if (playerTwo == null) {
            this.cancel(playerTwoProfile.getUuid());
            return;
        }

        playerOne.teleport(arena.getPlayerOneLocation());
        playerTwo.teleport(arena.getPlayerTwoLocation());

        kit.apply(playerOne);
        kit.apply(playerTwo);
    }

    public final void cancel(UUID loser) {
        arena.setInUse(false);
        this.matchState = MatchState.ENDED;

        if (loser == null)
            return;

        final Profile winnerProfile = loser == playerOneProfile.getUuid() ?
                playerTwoProfile : playerOneProfile;

        final Profile loserProfile = winnerProfile == playerOneProfile ?
                playerTwoProfile : playerOneProfile;

        if (winnerProfile != null) {
            winnerProfile.setMatch(null, playerOne != null);
            winnerProfile.setWins(winnerProfile.getWins() + 1);
            winnerProfile.setMatches(winnerProfile.getMatches() + 1);
        }

        if (loserProfile != null) {
            loserProfile.setMatch(null, playerTwo != null);
            loserProfile.setLosses(loserProfile.getLosses() + 1);
            loserProfile.setMatches(loserProfile.getMatches() + 1);
        }
    }

}
