package cc.zyrn.duels.match.task;

import cc.zyrn.duels.match.MatchHandler;
import cc.zyrn.duels.match.MatchState;
import cc.zyrn.duels.util.CC;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class MatchCountdownTask extends BukkitRunnable {

    private final MatchHandler matchHandler;

    @Override
    public void run() {
        matchHandler.getMatches().forEach(match -> {
            if (!match.getMatchState().equals(MatchState.STARTING)) {
                matchHandler.removeMatch(match);
                return;
            }

            if (match.getCountdown() <= 0) {
                match.setMatchState(MatchState.STARTED);
                return;
            }

            match.getPlayerOne().sendMessage(CC.translate("&fMatch starting in &e" + match.getCountdown()));
            match.getPlayerTwo().sendMessage(CC.translate("&fMatch starting in &e" + match.getCountdown()));

            match.setCountdown(match.getCountdown() - 1);
        });
    }
}
