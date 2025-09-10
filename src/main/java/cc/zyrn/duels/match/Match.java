package cc.zyrn.duels.match;

import cc.zyrn.duels.arena.Arena;
import cc.zyrn.duels.kit.Kit;
import cc.zyrn.duels.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        if (playerOne == null || playerTwo == null) {
            this.cancel();
            return;
        }

        playerOne.teleport(arena.getPlayerOneLocation());
        playerTwo.teleport(arena.getPlayerTwoLocation());

        kit.apply(playerOne);
        kit.apply(playerTwo);
    }

    public final void cancel() {
        if (this.playerOneProfile != null)
            this.playerOneProfile.setMatch(null, playerOne != null);

        if (this.playerTwoProfile != null)
            this.playerTwoProfile.setMatch(null, playerTwo != null);

        arena.setInUse(false);
        this.matchState = MatchState.ENDED;
    }

}
