package cc.zyrn.duels.profile;

import cc.zyrn.duels.match.Match;
import cc.zyrn.duels.profile.data.ProfileData;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private final UUID uuid;
    private int matches, wins, losses, coins;

    private ItemStack[] contents, armorContents;
    private Location location;

    private Match match;
    private ProfileData profileData;

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public Profile(Document document) {
        this.uuid = UUID.fromString(document.getString("_id"));

        this.matches = document.getInteger("matches");
        this.wins = document.getInteger("wins");
        this.losses = document.getInteger("losses");
        this.coins = document.getInteger("coins");

        final String s = document.getString("data");

        if (s.equalsIgnoreCase("null"))
            profileData = null;
        else profileData = new ProfileData(s);
    }

    public final void setMatch(Match match, boolean apply) {
        final Player player = Bukkit.getPlayer(uuid);

        if (match == null && !apply)
            return;

        if (match == null) {
            profileData.apply(player);
            this.profileData = null;
            return;
        }

        this.profileData = new ProfileData(player);
    }

    public Document toBson() {
        return new Document("_id", uuid.toString())
                .append("data", profileData.toString())
                .append("matches", matches)
                .append("wins", wins)
                .append("losses", losses)
                .append("coins", coins);
    }

}
