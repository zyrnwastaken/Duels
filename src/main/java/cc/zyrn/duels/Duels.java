package cc.zyrn.duels;

import cc.zyrn.duels.arena.ArenaHandler;
import cc.zyrn.duels.kit.KitHandler;
import cc.zyrn.duels.match.MatchHandler;
import cc.zyrn.duels.mongo.MongoHandler;
import cc.zyrn.duels.profile.ProfileHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Duels extends JavaPlugin {

    @Getter
    private static Duels instance;

    private ProfileHandler profileHandler;
    private MongoHandler mongoHandler;
    private KitHandler kitHandler;
    private ArenaHandler arenaHandler;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.mongoHandler = new MongoHandler(this);
        this.kitHandler = new KitHandler(this);
        this.arenaHandler = new ArenaHandler(this);
        this.profileHandler = new ProfileHandler(this);

        new MatchHandler(this);
    }

    @Override
    public void onDisable() {
        kitHandler.getKitMap().values().forEach(kit -> kitHandler.save(kit, false));
        arenaHandler.getArenas().forEach(arena -> arenaHandler.save(arena, false));
    }
}
