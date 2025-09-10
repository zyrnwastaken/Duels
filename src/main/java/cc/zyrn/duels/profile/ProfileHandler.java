package cc.zyrn.duels.profile;

import cc.zyrn.duels.Duels;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ProfileHandler {

    private final Duels duels;
    private final Map<UUID, Profile> profileMap;

    public ProfileHandler(Duels duels) {
        this.duels = duels;
        this.profileMap = new HashMap<>();
    }

    public final void loadProfile(UUID uuid, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> loadProfile(uuid, false));
            return;
        }

        Document document = duels.getMongoHandler().getProfiles().find(Filters.eq("_id", uuid.toString())).first();

        if (document == null) {
            final Profile profile = new Profile(uuid);
            this.save(profile, false);
            profileMap.put(uuid, profile);
            return;
        }

        profileMap.put(uuid, new Profile(document));
    }

    public final void handleRemoval(Profile profile, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> handleRemoval(profile, false));
            return;
        }

        this.save(profile, false);
        profileMap.remove(profile.getUuid());
    }

    public void save(Profile profile, boolean async) {
        if (async) {
            duels.getServer().getScheduler().runTaskAsynchronously(duels, () -> save(profile, false));
            return;
        }

        Document document = duels.getMongoHandler().getProfiles().find(Filters.eq("_id", profile.getUuid().toString())).first();

        if (document == null) {
            duels.getMongoHandler().getProfiles().insertOne(profile.toBson());
            return;
        }

        duels.getMongoHandler().getProfiles().replaceOne(document, profile.toBson(), new ReplaceOptions().upsert(true));
    }

    public final Optional<Profile> getProfile(UUID uuid) {
        return Optional.ofNullable(profileMap.get(uuid));
    }


}
