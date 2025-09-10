package cc.zyrn.duels.match;

import cc.zyrn.duels.Duels;
import cc.zyrn.duels.arena.Arena;
import cc.zyrn.duels.kit.Kit;
import cc.zyrn.duels.match.listener.MatchListener;
import cc.zyrn.duels.match.task.MatchCountdownTask;
import cc.zyrn.duels.profile.Profile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MatchHandler {

    private final Duels duels;
    private final List<Match> matches;

    public MatchHandler(Duels duels) {
        this.duels = duels;
        this.matches = new ArrayList<>();

        duels.getServer().getScheduler().runTaskTimer(duels, new MatchCountdownTask(this), 20L, 20L);
        duels.getServer().getPluginManager().registerEvents(new MatchListener(duels), duels);
    }

    public final void createMatch(Profile playerOne, Profile playerTwo, Arena arena, Kit kit) {
        matches.add(new Match(playerOne, playerTwo, arena, kit));
    }

    public final void removeMatch(Match match) {
        matches.remove(match);
    }

}
