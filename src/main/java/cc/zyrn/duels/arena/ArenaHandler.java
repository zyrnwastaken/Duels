package cc.zyrn.duels.arena;

import cc.zyrn.duels.Duels;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ArenaHandler {

    private final Duels duels;
    private final List<Arena> arenas;

    public ArenaHandler(Duels duels) {
        this.duels = duels;
        this.arenas = new ArrayList<>();
    }

    public final void createArena(String name) {
        final Arena arena = new Arena(name);

        arenas.add(arena);
        this.save(arena, true);
    }

    public final Optional<Arena> getArena(String name) {
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public final void removeArena(Arena arena, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> removeArena(arena, false));
            return;
        }

        arenas.remove(arena);

        final Document document = duels.getMongoHandler().getArenas().find(Filters.eq("_id", arena.getName())).first();

        if (document == null)
            return;

        duels.getMongoHandler().getArenas().deleteOne(document);
    }

    public void save(Arena arena, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> save(arena, false));
            return;
        }

        Document document = duels.getMongoHandler().getArenas().find(Filters.eq("_id", arena.getName())).first();

        if (document == null) {
            duels.getMongoHandler().getArenas().insertOne(arena.toBson());
            return;
        }

        duels.getMongoHandler().getArenas().replaceOne(document, arena.toBson(), new ReplaceOptions().upsert(true));
    }


}
