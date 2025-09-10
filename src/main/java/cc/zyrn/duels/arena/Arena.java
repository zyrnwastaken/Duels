package cc.zyrn.duels.arena;

import cc.zyrn.duels.kit.Kit;
import cc.zyrn.duels.util.LocationUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class Arena {

    private final String name;
    private final List<Kit> kitsAllowed;

    private boolean inUse;
    private Location playerOneLocation, playerTwoLocation, centerLocation;

    public Arena(String name) {
        this.name = name;
        this.kitsAllowed = new ArrayList<>();
    }

    public final void addKit(Kit kit) {
        kitsAllowed.add(kit);
    }

    public final void removeKit(Kit kit) {
        kitsAllowed.remove(kit);
    }

    public final Optional<Kit> getKit(String name) {
        return kitsAllowed.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Document toBson() {
        return new Document("_id", name)
                .append("kits", kitsAllowed.stream().map(Kit::getName).collect(Collectors.toList()))
                .append("playerOne", LocationUtil.LTS(playerOneLocation))
                .append("playerTwo", LocationUtil.LTS(playerTwoLocation))
                .append("center", LocationUtil.LTS(centerLocation));
    }

}
