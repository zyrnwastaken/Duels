package cc.zyrn.duels.kit;

import cc.zyrn.duels.Duels;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class KitHandler {

    private final Duels duels;
    private final Map<String, Kit> kitMap;

    public KitHandler(Duels duels) {
        this.duels = duels;
        this.kitMap = new HashMap<>();
    }

    public final void createKit(String name) {
        final Kit kit = new Kit(name);

        kitMap.put(name, kit);
        this.save(kit, true);
    }

    public final void createKit(String name, Player player) {
        final Kit kit = new Kit(name, player);

        kitMap.put(name, kit);
        this.save(kit, true);
    }

    public final void removeKit(Kit kit, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> removeKit(kit, false));
            return;
        }

        kitMap.remove(kit.getName());

        final Document document = duels.getMongoHandler().getKits().find(Filters.eq("_id", kit.getName())).first();

        if (document == null)
            return;

        duels.getMongoHandler().getKits().deleteOne(document);
    }

    public void save(Kit kit, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> save(kit, false));
            return;
        }

        Document document = duels.getMongoHandler().getProfiles().find(Filters.eq("_id", kit.getName())).first();

        if (document == null) {
            duels.getMongoHandler().getKits().insertOne(kit.toBson());
            return;
        }

        duels.getMongoHandler().getKits().replaceOne(document, kit.toBson(), new ReplaceOptions().upsert(true));
    }

    public final Optional<Kit> getKit(String name) {
        return Optional.ofNullable(kitMap.get(name));
    }


}
